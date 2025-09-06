package com.aurionpro.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.aurionpro.Dao.LeaveDAO;
import com.aurionpro.model.User;
import com.google.gson.Gson;

import jakarta.annotation.Resource;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/leaveAction")
public class LeaveActionServlet extends HttpServlet {

    @Resource(name = "jdbc/db-source")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String leaveIdStr = request.getParameter("leaveId");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("admin");

        Map<String, Object> result = new HashMap<>();
        try {
            int leaveId = Integer.parseInt(leaveIdStr);
            int adminId = admin.getId();
            LeaveDAO LeaveDao = new LeaveDAO(dataSource);

            boolean success = false;
            if ("APPROVE".equalsIgnoreCase(action)) {
                success = LeaveDao.updateLeaveStatus(leaveId, "APPROVED", adminId);
                boolean leaveNotification = LeaveDao.addLeaveNotification(leaveId, "APPROVED");
            } else if ("REJECT".equalsIgnoreCase(action)) {
                success = LeaveDao.updateLeaveStatus(leaveId, "REJECTED", adminId);
                boolean leaveNotification = LeaveDao.addLeaveNotification(leaveId, "REJECTED");
            }

            result.put("success", success);
            result.put("message", success ? "Leave " + action.toLowerCase() + "d successfully." : "Failed to update leave.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Server error");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new Gson().toJson(result, response.getWriter());
    }
}

