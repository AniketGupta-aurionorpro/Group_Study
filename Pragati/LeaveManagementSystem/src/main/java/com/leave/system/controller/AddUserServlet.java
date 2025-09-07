package com.leave.system.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;

import com.leave.system.dao.AdminDAO;
import com.leave.system.model.User;

@WebServlet("/admin/addUser")
public class AddUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role").toUpperCase();
        String joinedDateStr = request.getParameter("joinedDate"); // <-- from JSP form

        Integer managerId = null;
        if ("MANAGER".equals(role)) {
            managerId = 1; // All managers report to Admin
        } else if ("EMPLOYEE".equals(role)) {
            String mgrIdParam = request.getParameter("manager_id");
            if (mgrIdParam != null && !mgrIdParam.isEmpty()) {
                managerId = Integer.parseInt(mgrIdParam);
            }
        }

        // Convert to java.sql.Date
        Date joinedDate = null;
        if (joinedDateStr != null && !joinedDateStr.isEmpty()) {
            joinedDate = Date.valueOf(joinedDateStr); // yyyy-MM-dd
        }

        User user = new User(0, name, email, password, role);
        user.setManagerId(managerId);
        user.setJoinedDate(joinedDate);  // <-- set in model

        boolean added = adminDAO.addUser(user);

        if (added) {
            response.sendRedirect(request.getContextPath() + "/admin/addEmployee?success=true");
        } else {
            request.setAttribute("errorMessage", "Failed to add user!");
            request.getRequestDispatcher("/WEB-INF/jsp/addEmployee.jsp").forward(request, response);
        }
    }
}
