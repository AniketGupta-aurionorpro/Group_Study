package com.aurionpro.model;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/login")
public class Dashboard extends HttpServlet{
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		PrintWriter pw = arg1.getWriter();
		arg1.setContentType("text/html");
		HttpSession session = arg0.getSession(false);
		if(session == null) {
			arg1.sendRedirect("login.html");
			return;
		}
		String username = (String)session.getAttribute("username");
		
		pw.write("<html><body>");
        pw.write("<h2>Welcome " + username + " to my page : data from session</h2>");

        pw.write("<form action='logout' method='post'>");
        pw.write("<input type='submit' value='Logout' />");
        pw.write("</form>");

        pw.write("</body></html>");
		
	}
}
