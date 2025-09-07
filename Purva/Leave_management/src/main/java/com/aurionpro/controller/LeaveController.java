package com.aurionpro.controller;

import com.aurionpro.model.LeaveBalance;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.aurionpro.model.Leave;
import com.aurionpro.model.LeaveBalance;
import com.aurionpro.model.User;
import com.aurionpro.service.LeaveBalanceService;
import com.aurionpro.service.LeaveService;
//import com.aurionpro.service.UserService;

/**
 * Servlet implementation class LeaveController
 */

@WebServlet("/manager/leave")
public class LeaveController extends HttpServlet {

    private LeaveService leaveService;
    private LeaveBalanceService leaveBalanceService;

    public void init() throws ServletException {
        this.leaveService = new LeaveService();
        this.leaveBalanceService = new LeaveBalanceService();
    }

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
                leaveBalance = new LeaveBalance();
                leaveBalance.setYearlyLeaveTaken(0);
                leaveBalance.setTotalYearlyLeave(24);
//                leaveBalance.setMonthlyLeaveTaken(0);
//                leaveBalance.setTotalMonthlyLeave(2);
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
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/leaveApply.jsp");
            dispatcher.forward(request, response);
        }
    }

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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilStartDate = formatter.parse(startDateStr);
            java.util.Date utilEndDate = formatter.parse(endDateStr);

            // Calculate total days for validation
            long diffInMillies = Math.abs(utilEndDate.getTime() - utilStartDate.getTime());
            long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
            
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(utilStartDate);
           // int startMonth = startCal.get(Calendar.MONTH) + 1;
            int startYear = startCal.get(Calendar.YEAR);
            
            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalance(currentUser.getId());
            
            if (leaveBalance == null) {
                leaveBalance = new LeaveBalance();
                leaveBalance.setYearlyLeaveTaken(0);
                leaveBalance.setTotalYearlyLeave(24);
                //leaveBalance.setMonthlyLeaveTaken(0);
               // leaveBalance.setTotalMonthlyLeave(2);
            }
            
           // int monthlyLeaveTaken = leaveBalance.getMonthlyLeaveTaken();
            int yearlyLeaveTaken = leaveBalance.getYearlyLeaveTaken();
            int pendingLeaves = leaveService.getPendingLeavesCount(currentUser.getId());

            // New validation to check if a single leave application exceeds the monthly limit
//            if (days > leaveBalance.getTotalMonthlyLeave()) {
//                request.setAttribute("error", "You cannot apply for more than " + leaveBalance.getTotalMonthlyLeave() + " days at once. Please adjust your date selection.");
//                doGet(request, response);
//                return;
//            }
            
            // Validate based on the month and year of the leave request
            if (startYear == leaveBalance.getYear()) {
                if ((yearlyLeaveTaken + days + pendingLeaves) > leaveBalance.getTotalYearlyLeave()) {
                    request.setAttribute("error", "The applied leave duration exceeds the available yearly leave balance. You have " + (leaveBalance.getTotalYearlyLeave() - yearlyLeaveTaken - pendingLeaves) + " days remaining this year.");
                    doGet(request, response);
                    return;
                }
            }
            // Check if the leave is for the current month and if it exceeds the monthly limit
//            Calendar currentCal = Calendar.getInstance();
//            if (startMonth == (currentCal.get(Calendar.MONTH) + 1) && startYear == currentCal.get(Calendar.YEAR)) {
//                if ((monthlyLeaveTaken + days + pendingLeaves) > leaveBalance.getTotalMonthlyLeave()) {
//                    request.setAttribute("error", "The applied leave duration exceeds the available monthly leave balance. You have " + (leaveBalance.getTotalMonthlyLeave() - monthlyLeaveTaken - pendingLeaves) + " days remaining this month.");
//                    doGet(request, response);
//                    return;
//                }
//            }


            java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(utilEndDate.getTime());

            Leave leave = new Leave();
            leave.setUserId(currentUser.getId());
            leave.setManagerId(currentUser.getManagerId());
            leave.setStartDate(sqlStartDate);
            leave.setEndDate(sqlEndDate);
            leave.setReason(reason);

         // ...
            leaveService.applyLeave(leave);
            request.setAttribute("message", "Leave application submitted successfully!");
            // Redirect to a GET URL to prevent duplicate form submission
            response.sendRedirect(request.getContextPath() + "/manager/leave?status=success");
        
        } catch (ParseException e) {
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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         // Correctly convert java.util.Date to java.sql.Date
            java.sql.Date sqlStartDate = new java.sql.Date(formatter.parse(startDateStr).getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(formatter.parse(endDateStr).getTime());
            
            List<Leave> searchResults = leaveService.getLeavesByDateRange(currentUser.getId(), sqlStartDate, sqlEndDate);
            
            request.setAttribute("searchResults", searchResults);
            
            doGet(request, response);
            
        } catch (ParseException e) {
            request.setAttribute("error", "Invalid date format for search. Please use YYYY-MM-DD.");
            doGet(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error during search: " + e.getMessage());
            doGet(request, response);
        }
    }
}










//@WebServlet("/manager/leave")
//@WebServlet("/manager/leave")
//package com.aurionpro.controller;

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
//                leaveBalance.setTotalYearlyLeave(22);
//                leaveBalance.setMonthlyLeaveTaken(0);
//                leaveBalance.setTotalMonthlyLeave(2);
//            }
//
//            request.setAttribute("currentUser", currentUser);
//            request.setAttribute("leaveBalance", leaveBalance);
//            request.setAttribute("adminName", adminName);
//            request.setAttribute("pastLeaves", pastLeaves);
//
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/leaveApply.jsp");
//            dispatcher.forward(request, response);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            request.setAttribute("error", "Database error: " + e.getMessage());
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/leaveApply.jsp");
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
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            java.util.Date utilStartDate = formatter.parse(startDateStr);
//            java.util.Date utilEndDate = formatter.parse(endDateStr);
//
//            // Calculate total days for validation
//            long diffInMillies = Math.abs(utilEndDate.getTime() - utilStartDate.getTime());
//            long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
//            
//            Calendar startCal = Calendar.getInstance();
//            startCal.setTime(utilStartDate);
//            int startMonth = startCal.get(Calendar.MONTH);
//            int startYear = startCal.get(Calendar.YEAR);
//            
//            LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalance(currentUser.getId());
//            
//            if (leaveBalance == null) {
//                leaveBalance = new LeaveBalance();
//                leaveBalance.setYearlyLeaveTaken(0);
//                leaveBalance.setTotalYearlyLeave(22);
//                leaveBalance.setMonthlyLeaveTaken(0);
//                leaveBalance.setTotalMonthlyLeave(2);
//            }
//            
//         // New validation to check if a single leave application exceeds the monthly limit
//            if (days > leaveBalance.getTotalMonthlyLeave()) {
//                request.setAttribute("error", "You cannot apply for more than " + leaveBalance.getTotalMonthlyLeave() + " days at once. Please adjust your date selection.");
//                doGet(request, response);
//                return;
//            }
//            
//            int monthlyLeaveTaken = leaveBalance.getMonthlyLeaveTaken();
//            int yearlyLeaveTaken = leaveBalance.getYearlyLeaveTaken();
//            int pendingLeaves = leaveService.getPendingLeavesCount(currentUser.getId());
//
//            
//            // Validate based on the month and year of the leave request
//            if (startMonth == leaveBalance.getMonthlyLeaveTaken() && startYear == leaveBalance.getYear()) {
//            	if ((monthlyLeaveTaken + days + pendingLeaves) > leaveBalance.getTotalMonthlyLeave()) {
//                    request.setAttribute("error", "The applied leave duration exceeds the available monthly leave balance. You have " + (leaveBalance.getTotalMonthlyLeave() - monthlyLeaveTaken - pendingLeaves) + " days remaining this month.");
//                    doGet(request, response);
//                    return;
//                }
//            }
//            
//            if (startYear == leaveBalance.getYear()) {
//            	if ((yearlyLeaveTaken + days + pendingLeaves) > leaveBalance.getTotalYearlyLeave()) {
//                    request.setAttribute("error", "The applied leave duration exceeds the available yearly leave balance. You have " + (leaveBalance.getTotalYearlyLeave() - yearlyLeaveTaken - pendingLeaves) + " days remaining this year.");
//                    doGet(request, response);
//                    return;
//                }
//            }
//
//            java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
//            java.sql.Date sqlEndDate = new java.sql.Date(utilEndDate.getTime());
//
//            Leave leave = new Leave();
//            leave.setUserId(currentUser.getId());
//            leave.setManagerId(currentUser.getManagerId());
//            leave.setStartDate(sqlStartDate);
//            leave.setEndDate(sqlEndDate);
//            leave.setReason(reason);
//
//            leaveService.applyLeave(leave);
//
//            request.setAttribute("message", "Leave application submitted successfully!");
//            doGet(request, response);
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
//            List<Leave> searchResults = leaveService.getLeavesByDateRange(currentUser.getId(), sqlStartDate, sqlEndDate);
//            
//            request.setAttribute("searchResults", searchResults);
//            
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
