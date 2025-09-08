package com.notification.controller;
 

import com.notification.model.User;
import com.notification.util.DBConnection;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email=? AND password=? AND role='MANAGER'");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User manager = new User();
                manager.setId(rs.getInt("id"));
                manager.setName(rs.getString("name"));
                manager.setEmail(rs.getString("email"));
                manager.setRole(rs.getString("role"));
                manager.setManagerId(rs.getInt("manager_id"));
                HttpSession session = request.getSession();
                session.setAttribute("manager", manager);
                response.sendRedirect("managerDashboard.jsp");
            } else {
                response.sendRedirect("index.jsp?error=Invalid Credentials");
            }
        } catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=Database Error");
        }
    }
}
