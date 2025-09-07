package com.aurionpro.leave_management.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/*")
public class AdminPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo(); 
        String targetJsp = null;

        if (path == null || "/".equals(path) || "/dashboard".equals(path)) {
            targetJsp = "/WEB-INF/jsp/admin_dashboard.jsp";
        } else if ("/addEmployee".equals(path)) {
            targetJsp = "/WEB-INF/jsp/addEmployee.jsp";
        } else if ("/editEmployee".equals(path)) {
            targetJsp = "/WEB-INF/jsp/editEmployee.jsp";
        } else if ("/welcome".equals(path)) {
            targetJsp = "/WEB-INF/jsp/welcome.jsp";
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // ✅ forward to the right JSP (URL will already be /admin/... because of redirect after login)
        request.getRequestDispatcher(targetJsp).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
