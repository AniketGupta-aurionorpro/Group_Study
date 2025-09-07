package com.leave.system.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import com.leave.system.dao.UserDAO;
import com.leave.system.model.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (role != null) {
            role = role.toUpperCase();
        }

        User user = userDAO.login(email, password, role);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userName", user.getName());
            session.setAttribute("role", user.getRole());

            switch (user.getRole()) {
                case "ADMIN":
                    // ✅ Redirect to AdminPageServlet
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    break;
                case "MANAGER":
                    // ✅ Redirect to ManagerPageServlet (you need to create /manager/* servlet)
                    response.sendRedirect(request.getContextPath() + "/manager/dashboard");
                    break;
                case "EMPLOYEE":
                    // ✅ Redirect to EmployeePageServlet (you need to create /employee/* servlet)
                    response.sendRedirect(request.getContextPath() + "/employee/dashboard");
                    break;
                default:
                    request.setAttribute("errorMessage", "Invalid role!");
                    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                    break;
            }

        } else {
            request.setAttribute("errorMessage", "Invalid email, password, or role!");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Always redirect to /login (ShowLoginServlet), never directly expose .jsp
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
