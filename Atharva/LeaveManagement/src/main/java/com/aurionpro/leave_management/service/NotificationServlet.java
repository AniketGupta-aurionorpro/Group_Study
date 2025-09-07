package com.aurionpro.leave_management.service;

import java.io.IOException;

import javax.sql.DataSource;

import com.aurionpro.leave_management.Dao.NotificationDAO;
import com.aurionpro.leave_management.model.User;

import jakarta.annotation.Resource;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {
    
	@Resource(name = "jdbc/leave_management")
    private DataSource dataSource;

    private NotificationDAO dao;

    @Override
    public void init() {
        dao = new NotificationDAO(dataSource);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String action = request.getParameter("action");
        int userId = ((User) request.getSession().getAttribute("admin")).getId(); // assuming user in session
        boolean success = false;

        try {
            switch (action) {
                case "markRead":
                    success = dao.markAsRead(Integer.parseInt(request.getParameter("id")));
                    break;
                case "delete":
                    success = dao.deleteNotification(Integer.parseInt(request.getParameter("id")));
                    break;
                case "markAllRead":
                    success = dao.markAllAsRead(userId);
                    break;
                case "deleteAllRead":
                    success = dao.deleteAllRead(userId);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"success\":" + success + "}");
    }
}

