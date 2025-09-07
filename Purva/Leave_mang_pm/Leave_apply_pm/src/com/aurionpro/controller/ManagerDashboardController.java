package com.aurionpro.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import com.aurionpro.model.User;
import com.aurionpro.service.ManagerService;
import com.aurionpro.service.ManagerServiceLayer;

/**
 * Servlet implementation class ManagerDashboardController
 */
@WebServlet("/ManagerDashboardController")
public class ManagerDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ManagerService managerService;

    @Resource(name = "jdbc/LeaveManagementDB")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        managerService = new ManagerServiceLayer(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // For testing purposes: use a hardcoded managerId
        int managerId = 2;

        try {
            List<User> employees = managerService.getEmployeesByManagerId(managerId);

            if (employees.isEmpty()) {
                request.setAttribute("message", "No employees found under this manager.");
            } else {
                request.setAttribute("employees", employees);
            }

            request.getRequestDispatcher("/WEB-INF/views/dashboard_manager.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}