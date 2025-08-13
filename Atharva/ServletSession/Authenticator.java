package com.aurionpro.model;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/authenticate")
public class Authenticator extends HttpServlet{
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		arg1.setContentType("text/html");
		String username = null;
		String password = null;
		username = arg0.getParameter("username");
		password = arg0.getParameter("password");
		
		if(username == null || password == null) {
			arg1.sendRedirect("login.html");
			return;
		}
		
		if(username.equals("atharva") && password.equals("12345")) {
			HttpSession session = arg0.getSession();
			session.setAttribute("username", username);

			arg1.sendRedirect("login");
		}
		else {
			arg1.sendRedirect("login.html");
		}
	}
}
