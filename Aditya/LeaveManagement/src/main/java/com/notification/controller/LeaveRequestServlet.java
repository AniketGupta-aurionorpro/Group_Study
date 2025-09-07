package com.notification.controller;
 

import com.notification.dao.LeaveDAO;
import com.notification.dao.NotificationDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LeaveRequestServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int leaveId = Integer.parseInt(request.getParameter("leaveId"));
        String action = request.getParameter("action");
        String rejectionReason = request.getParameter("rejectionReason");
        int managerId = Integer.parseInt(request.getParameter("managerId"));

        LeaveDAO leaveDAO = new LeaveDAO();
        NotificationDAO notificationDAO = new NotificationDAO();

        leaveDAO.updateLeaveStatus(leaveId, action, managerId, rejectionReason);

        String message = "Leave ID " + leaveId + " has been " + action.toLowerCase();
        notificationDAO.createNotification(managerId, message);

        response.sendRedirect("managerNotifications.jsp?managerId=" + managerId);
    }
}
