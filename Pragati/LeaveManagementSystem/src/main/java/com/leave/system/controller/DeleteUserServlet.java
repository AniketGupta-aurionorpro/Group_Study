package com.leave.system.controller;
import java.io.IOException;

import com.leave.system.dao.AdminDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/deleteUser")
public class DeleteUserServlet extends HttpServlet {
    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String role = request.getParameter("role");

        if("MANAGER".equals(role) && adminDAO.hasEmployees(id)) {
            request.getSession().setAttribute("errorMessage", 
                "Cannot delete manager with employees under them!");
        } else {
            boolean deleted = adminDAO.deleteUser(id);
            request.getSession().setAttribute("successMessage", 
                deleted ? "User deleted successfully!" : "Failed to delete user!");
        }

        response.sendRedirect(request.getContextPath() + "/admin/editUser");
    }
}
