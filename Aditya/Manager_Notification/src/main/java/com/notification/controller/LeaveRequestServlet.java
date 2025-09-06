package com.notification.controller;
// 
//
//import java.io.IOException;
//
//import com.notification.dao.LeaveDAO;
//import com.notification.dao.NotificationDAO;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class LeaveRequestServlet extends HttpServlet {
//    private LeaveDAO leaveDAO = new LeaveDAO();
//    private NotificationDAO notificationDAO = new NotificationDAO();
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int leaveId = Integer.parseInt(request.getParameter("leaveId"));
//        int empId = Integer.parseInt(request.getParameter("empId"));
//        int managerId = 2; // sample manager id
//        String action = request.getParameter("action");
//        String rejectionReason = request.getParameter("rejectionReason");
//
//        if ("approve".equals(action)) {
//            leaveDAO.updateLeaveStatus(leaveId, "APPROVED", null, managerId);
//            notificationDAO.addNotification(empId, "Your leave request #" + leaveId + " has been APPROVED.");
//        } else if ("reject".equals(action)) {
//            leaveDAO.updateLeaveStatus(leaveId, "REJECTED", rejectionReason, managerId);
//            notificationDAO.addNotification(empId, "Your leave request #" + leaveId + " has been REJECTED. Reason: " + rejectionReason);
//        }
//
//        response.sendRedirect("managerNotifications.jsp");
//    }
//}


























 

import java.io.IOException;

import com.notification.dao.LeaveDAO;
import com.notification.dao.NotificationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LeaveRequestServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int leaveId = Integer.parseInt(request.getParameter("leaveId"));
        String action = request.getParameter("action");
        String rejectionReason = request.getParameter("rejectionReason");
        int managerId = 2; // fixed manager for demo

        LeaveDAO leaveDAO = new LeaveDAO();
        NotificationDAO notificationDAO = new NotificationDAO();

        if ("approve".equals(action)) {
            leaveDAO.updateLeaveStatus(leaveId, "APPROVED", managerId, null);
            notificationDAO.createNotification(managerId, "Leave ID " + leaveId + " has been approved.");
        } else if ("reject".equals(action)) {
            leaveDAO.updateLeaveStatus(leaveId, "REJECTED", managerId, rejectionReason);
            notificationDAO.createNotification(managerId, "Leave ID " + leaveId + " has been rejected. Reason: " + rejectionReason);
        }

        response.sendRedirect("managerNotifications.jsp");
    }
}
