package com.aurionpro.leave_management.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.aurionpro.leave_management.model.User;
import com.aurionpro.leave_management.model.User.Role;

/**
 * Servlet implementation class TestLoginServlet
 */
@WebServlet("/manager/testLogin")
public class TestLoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        // Create a mock MANAGER user
        User manager = new User();
        manager.setId(2); // Matches manager ID from your DB script
        manager.setName("Test Manager");
        manager.setEmail("manager@test.com");
        manager.setRole(Role.MANAGER);
        manager.setManagerId(1); // Matches admin ID from your DB script

        session.setAttribute("user", manager);

        response.sendRedirect(request.getContextPath() + "/manager/leave");
    }
}