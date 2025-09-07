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

            String adminName = "Admin"; // This could be fetched from a service or config

            if (leaveBalance == null) {
                // Create a default leave balance if none exists for the user
                leaveBalance = new LeaveBalance();
                leaveBalance.setYearlyLeaveTaken(0);
                leaveBalance.setTotalYearlyLeave(24);
            }

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
            long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

            // Basic validation: end date must not be before start date
            if (days <= 0) {
                request.setAttribute("error", "End date must be on or after the start date.");
                doGet(request, response);
                return;
            }

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

            leaveService.applyLeave(leave);

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

//package com.aurionpro.leave_management.controller;
//
//import com.aurionpro.leave_management.model.Leave;
//import com.aurionpro.leave_management.model.LeaveBalance;
//import com.aurionpro.leave_management.model.User;
//import com.aurionpro.leave_management.service.LeaveBalanceService;
//import com.aurionpro.leave_management.service.LeaveService;
//import jakarta.ejb.Local;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//
////import com.aurionpro.service.UserService;
//
///**
// * Servlet implementation class LeaveController
// */
//
//@WebServlet("/manager/leave")
//public class LeaveController extends HttpServlet {
//
//    private LeaveService leaveService;
//    private LeaveBalanceService leaveBalanceService;
//
//    public void init() throws ServletException {
//        this.leaveService = new LeaveService();
//        this.leaveBalanceService = new LeaveBalanceService();
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        User currentUser = (User) session.getAttribute("user");
//
//        if (currentUser == null) {
//            response.sendRedirect(request.getContextPath() + "/login.jsp");
//            return;
//        }
//
//        try {
//            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalance(currentUser.getId());
//            List<Leave> pastLeaves = leaveService.getLeavesByUserId(currentUser.getId());
//
//            String adminName = "Admin";
//
//            if (leaveBalance == null) {
//                leaveBalance = new LeaveBalance();
//                leaveBalance.setYearlyLeaveTaken(0);
//                leaveBalance.setTotalYearlyLeave(24);
////                leaveBalance.setMonthlyLeaveTaken(0);
////                leaveBalance.setTotalMonthlyLeave(2);
//            }
//
//            request.setAttribute("currentUser", currentUser);
//            request.setAttribute("leaveBalance", leaveBalance);
//            request.setAttribute("adminName", adminName);
//            request.setAttribute("pastLeaves", pastLeaves);
//
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/leaveApply.jsp");
//            dispatcher.forward(request, response);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            request.setAttribute("error", "Database error: " + e.getMessage());
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/leaveApply.jsp");
//            dispatcher.forward(request, response);
//        }
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        User currentUser = (User) session.getAttribute("user");
//
//        if (currentUser == null) {
//            response.sendRedirect(request.getContextPath() + "/login.jsp");
//            return;
//        }
//
//        String action = request.getParameter("action");
//        if ("search".equals(action)) {
//            handleSearch(request, response, currentUser);
//        } else {
//            handleApplyLeave(request, response, currentUser);
//        }
//    }
//
//    private void handleApplyLeave(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
//        String startDateStr = request.getParameter("startDate");
//        String endDateStr = request.getParameter("endDate");
//        String reason = request.getParameter("reason");
//
//        try {
////            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            LocalDate utilStartDate = LocalDate.parse(startDateStr);
//            LocalDate utilEndDate = LocalDate.parse(endDateStr);
//
//            // Calculate total days for validation
//            long diffInMillies = Math.abs(utilEndDate - utilStartDate).toEpochDay() * 24 * 60 * 60 * 1000;
//            long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
//
//            Calendar startCal = Calendar.getInstance();
//            startCal.setTime(utilStartDate);
//           // int startMonth = startCal.get(Calendar.MONTH) + 1;
//            int startYear = startCal.get(Calendar.YEAR);
//
//            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalance(currentUser.getId());
//
//            if (leaveBalance == null) {
//                leaveBalance = new LeaveBalance();
//                leaveBalance.setYearlyLeaveTaken(0);
//                leaveBalance.setTotalYearlyLeave(24);
//                //leaveBalance.setMonthlyLeaveTaken(0);
//               // leaveBalance.setTotalMonthlyLeave(2);
//            }
//
//           // int monthlyLeaveTaken = leaveBalance.getMonthlyLeaveTaken();
//            int yearlyLeaveTaken = leaveBalance.getYearlyLeaveTaken();
//            int pendingLeaves = leaveService.getPendingLeavesCount(currentUser.getId());
//
//            // New validation to check if a single leave application exceeds the monthly limit
////            if (days > leaveBalance.getTotalMonthlyLeave()) {
////                request.setAttribute("error", "You cannot apply for more than " + leaveBalance.getTotalMonthlyLeave() + " days at once. Please adjust your date selection.");
////                doGet(request, response);
////                return;
////            }
//
//            // Validate based on the month and year of the leave request
//            if (startYear == leaveBalance.getYear()) {
//                if ((yearlyLeaveTaken + days + pendingLeaves) > leaveBalance.getTotalYearlyLeave()) {
//                    request.setAttribute("error", "The applied leave duration exceeds the available yearly leave balance. You have " + (leaveBalance.getTotalYearlyLeave() - yearlyLeaveTaken - pendingLeaves) + " days remaining this year.");
//                    doGet(request, response);
//                    return;
//                }
//            }
//            // Check if the leave is for the current month and if it exceeds the monthly limit
////            Calendar currentCal = Calendar.getInstance();
////            if (startMonth == (currentCal.get(Calendar.MONTH) + 1) && startYear == currentCal.get(Calendar.YEAR)) {
////                if ((monthlyLeaveTaken + days + pendingLeaves) > leaveBalance.getTotalMonthlyLeave()) {
////                    request.setAttribute("error", "The applied leave duration exceeds the available monthly leave balance. You have " + (leaveBalance.getTotalMonthlyLeave() - monthlyLeaveTaken - pendingLeaves) + " days remaining this month.");
////                    doGet(request, response);
////                    return;
////                }
////            }
//
//
//            java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
//            java.sql.Date sqlEndDate = new java.sql.Date(utilEndDate.getTime());
//
//            Leave leave = new Leave();
//            leave.setUserId(currentUser.getId());
//            leave.setManagerId(currentUser.getManagerId());
//            leave.setStartDate(sqlStartDate.toLocalDate());
//            leave.setEndDate(sqlEndDate.toLocalDate());
//            leave.setReason(reason);
//
//            leaveService.applyLeave(leave);
//            request.setAttribute("message", "Leave application submitted successfully!");
//            // Redirect to a GET URL to prevent duplicate form submission
//            response.sendRedirect(request.getContextPath() + "/manager/leave?status=success");
//
//        } catch (ParseException e) {
//            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
//            doGet(request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "Error submitting leave: " + e.getMessage());
//            doGet(request, response);
//        }
//    }
//
//    private void handleSearch(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
//        String startDateStr = request.getParameter("searchStartDate");
//        String endDateStr = request.getParameter("searchEndDate");
//
//        try {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//         // Correctly convert java.util.Date to java.sql.Date
//            java.sql.Date sqlStartDate = new java.sql.Date(formatter.parse(startDateStr).getTime());
//            java.sql.Date sqlEndDate = new java.sql.Date(formatter.parse(endDateStr).getTime());
//
//            List<Leave> searchResults = leaveService.getLeavesByDateRange(currentUser.getId(), sqlStartDate.toLocalDate(), sqlEndDate.toLocalDate());
//
//            request.setAttribute("searchResults", searchResults);
//            doGet(request, response);
//
//        } catch (ParseException e) {
//            request.setAttribute("error", "Invalid date format for search. Please use YYYY-MM-DD.");
//            doGet(request, response);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            request.setAttribute("error", "Database error during search: " + e.getMessage());
//            doGet(request, response);
//        }
//    }
//}