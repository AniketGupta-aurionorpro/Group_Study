package com.notification.controller;
 

import com.notification.dao.NotificationDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class NotificationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int managerId = Integer.parseInt(request.getParameter("managerId"));
        NotificationDAO dao = new NotificationDAO();
        List<String> notifications = dao.getNotificationsForManager(managerId);
        request.setAttribute("notifications", notifications);
        RequestDispatcher rd = request.getRequestDispatcher("notificationList.jsp");
        rd.forward(request, response);
    }
}
