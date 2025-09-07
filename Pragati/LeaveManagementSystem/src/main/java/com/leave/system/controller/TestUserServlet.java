package com.leave.system.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.leave.system.dao.AdminDAO;
import com.leave.system.model.User;

@WebServlet("/testUsers")
public class TestUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        List<User> users = adminDAO.getAllUsers();

        if (users.isEmpty()) {
            out.println("<h3>No users found in DB!</h3>");
        } else {
            out.println("<h3>Users from Database:</h3>");
            out.println("<ul>");
            for (User u : users) {
                out.println("<li>ID: " + u.getId() +
                        ", Name: " + u.getName() +
                        ", Email: " + u.getEmail() +
                        ", Role: " + u.getRole() + "</li>");
            }
            out.println("</ul>");
        }
    }
}
