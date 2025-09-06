package com.notification.controller;
 
import java.io.IOException;

//
//import java.io.IOException;
//import java.util.List;
//
//import com.notification.dao.NotificationDAO;
//import com.notification.model.Notification;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class NotificationServlet extends HttpServlet {
//    private NotificationDAO notificationDAO = new NotificationDAO();
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int empId = Integer.parseInt(request.getParameter("empId"));
//        List<Notification> notifications = notificationDAO.getNotifications(empId);
//        request.setAttribute("notifications", notifications);
//        RequestDispatcher rd = request.getRequestDispatcher("notificationList.jsp");
//        rd.forward(request, response);
//    }
//}




























 
 
import com.notification.dao.NotificationDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotificationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int managerId = 2; // demo
        NotificationDAO dao = new NotificationDAO();
        request.setAttribute("notifications", dao.getNotificationsForManager(managerId));
        RequestDispatcher rd = request.getRequestDispatcher("notificationList.jsp");
        rd.forward(request, response);
    }
}
