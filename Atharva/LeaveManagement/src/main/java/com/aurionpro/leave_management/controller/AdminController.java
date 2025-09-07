package com.aurionpro.leave_management.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.aurionpro.leave_management.model.Leave;
import com.aurionpro.leave_management.model.User;
import com.aurionpro.leave_management.service.AdminService;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminController extends HttpServlet{
	private AdminService adminService;
	
	@Resource(name = "jdbc/leave_management")
    private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		try {
			this.adminService = new AdminService(dataSource);
		} catch (Exception exc) {
			throw new ServletException(exc);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		HttpSession session = request.getSession(false);
//		if (session == null) {
//			// User is not logged in, redirect to login page
//			RequestDispatcher dispatcher = request.getRequestDispatcher(request.getContextPath()+"/controller");
//			dispatcher.forward(request, response);
//		}
		
		HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("admin");
		
		try {
			String theCommand = request.getParameter("command");
			if (theCommand == null || theCommand.isBlank()) {
				theCommand = "DASHBOARD";
			}

			switch (theCommand) {
			case "DASHBOARD":
				int pendingRequestCount = adminService.getPendingRequestManagersCount();
				int unseenNotificationCount = adminService.getNotificationCount(admin.getId());
				List<Map<String, Object>> notifications = adminService.getNotifications(admin.getId());
				request.setAttribute("pendingRequestCount", pendingRequestCount);
				request.setAttribute("unseenNotificationCount", unseenNotificationCount);
				request.setAttribute("notifications", notifications);
				request.getRequestDispatcher("/views/AdminDashboard.jsp")
				.forward(request, response);
				break;
				
			case "REQUESTS":
				List<Leave> pendingRequests = adminService.getPendingRequestManagers();
				Map<Integer, List<User>> approvedLeaves = adminService.getApprovedLeaves(pendingRequests);
				request.setAttribute("pendingRequests", pendingRequests);
				request.setAttribute("approvedLeaves", approvedLeaves);
				request.getRequestDispatcher("/views/Requests.jsp")
				.forward(request, response);
				break;
				
			default:
				int pendingRequestCount2 = adminService.getPendingRequestManagersCount();
				request.setAttribute("pendingRequestCount", pendingRequestCount2);
				request.getRequestDispatcher("/views/AdminDashboard.jsp")
				.forward(request, response);
				break;
			}
		}catch(Exception e){
			
		}
	}
}
