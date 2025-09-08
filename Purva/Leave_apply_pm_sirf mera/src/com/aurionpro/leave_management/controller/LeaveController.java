package com.aurionpro.leave_management.controller;

import com.aurionpro.leave_management.model.Leave;
import com.aurionpro.leave_management.model.LeaveBalance;
import com.aurionpro.leave_management.model.User;
import com.aurionpro.leave_management.service.LeaveBalanceService;
import com.aurionpro.leave_management.service.LeaveService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebServlet("/manager/leave")
public class LeaveController extends HttpServlet {

    private LeaveService leaveService;
    private LeaveBalanceService leaveBalanceService;

    @Override
    public void init() throws ServletException {
        this.leaveService = new LeaveService();
        this.leaveBalanceService = new LeaveBalanceService();
    }

    //visiting leaveApply page request
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalance(currentUser.getId());
            List<Leave> pastLeaves = leaveService.getLeavesByUserId(currentUser.getId());

            String adminName = "Admin"; 

            if (leaveBalance == null) {
                // Create a default leave balance if none exists for the user
                leaveBalance = new LeaveBalance();
                leaveBalance.setYearlyLeaveTaken(0);
                leaveBalance.setTotalYearlyLeave(24);
            }

            //now visisble in page
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("leaveBalance", leaveBalance);
            request.setAttribute("adminName", adminName);
            request.setAttribute("pastLeaves", pastLeaves);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/leaveApply.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/leaveApply.jsp");
            dispatcher.forward(request, response);
        }
    }

    //submson 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("search".equals(action)) {
            handleSearch(request, response, currentUser);
        } else {
            handleApplyLeave(request, response, currentUser);
        }
    }

    private void handleApplyLeave(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String reason = request.getParameter("reason");

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Calculate total leave days requested (inclusive of start and end dates)
            //ChronoUnit enum from java.time.temporal that represents units of time (DAYS, MONTHS, YEARS, etc.).
            long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

            //  validation: end date must not be before start date
            //< 0 -> end date is before the start date
            if (days <= 0) {
                request.setAttribute("error", "End date must be on or after the start date.");
                doGet(request, response);
                return;
            }

            //vapas leavebalance fetch karna 
            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalance(currentUser.getId());

            if (leaveBalance == null) {
                leaveBalance = new LeaveBalance();
                leaveBalance.setYearlyLeaveTaken(0);
                leaveBalance.setTotalYearlyLeave(24);
            }

            int yearlyLeaveTaken = leaveBalance.getYearlyLeaveTaken();
            int pendingLeaves = leaveService.getPendingLeavesCount(currentUser.getId());

            // Validate against yearly leave balance
            // Using startDate.getYear() to ensure validation is against the year of the leave request
            //.getYear() -> returns the year as an int (e.g., 2025).
            if (startDate.getYear() == LocalDate.now().getYear()) { // Assuming balance is for the current year
                if ((yearlyLeaveTaken + days + pendingLeaves) > leaveBalance.getTotalYearlyLeave()) {
                    request.setAttribute("error", "The applied leave duration exceeds the available yearly leave balance. You have " + (leaveBalance.getTotalYearlyLeave() - yearlyLeaveTaken - pendingLeaves) + " days remaining this year.");
                    doGet(request, response);
                    return;
                }
            }

            Leave leave = new Leave();
            leave.setUserId(currentUser.getId());
            leave.setManagerId(currentUser.getManagerId());
            leave.setStartDate(startDate); // Set LocalDate directly
            leave.setEndDate(endDate);     // Set LocalDate directly
            leave.setReason(reason);

            leaveService.applyLeave(leave);// data saved in db

            // Redirect to a GET request to prevent form resubmission on refresh
            response.sendRedirect(request.getContextPath() + "/manager/leave?status=success");

        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error submitting leave: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
    	//Reads a request parameter named
        String startDateStr = request.getParameter("searchStartDate");
        String endDateStr = request.getParameter("searchEndDate");

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            List<Leave> searchResults = leaveService.getLeavesByDateRange(currentUser.getId(), startDate, endDate);

            request.setAttribute("searchResults", searchResults);
            doGet(request, response);

        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Invalid date format for search. Please use YYYY-MM-DD.");
            doGet(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error during search: " + e.getMessage());
            doGet(request, response);
        }
    }
}

