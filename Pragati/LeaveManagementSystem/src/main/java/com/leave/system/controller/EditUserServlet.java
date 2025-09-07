package com.leave.system.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import com.leave.system.dao.AdminDAO;
import com.leave.system.model.User;

@WebServlet("/admin/editUser")
public class EditUserServlet extends HttpServlet {
    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = adminDAO.getAllUsers(); // Fetch all users
        request.setAttribute("users", users);

        request.getRequestDispatcher("/WEB-INF/jsp/editEmployee.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String managerIdStr = request.getParameter("managerId");

            Integer managerId = null;
            if ("MANAGER".equals(role)) {
                managerId = 1; // default admin
            } else if (managerIdStr != null && !managerIdStr.isEmpty()) {
                managerId = Integer.parseInt(managerIdStr);
            }

            User user = new User(id, name, email, password, role);
            user.setManagerId(managerId);

            boolean updated = adminDAO.updateUser(user);

            if (updated) {
                response.sendRedirect(request.getContextPath() + "/admin/editUser?success=true");
            } else {
                request.setAttribute("errorMessage", "Failed to update user.");
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input for ID or Manager ID.");
            doGet(request, response);
        }
    }
}
