//This is a sample controller for trial
//do not include it in final version


package com.aurionpro.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.aurionpro.model.User;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Resource(name = "jdbc/db-source")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User admin = getAdmin();  // now safe

        HttpSession session = request.getSession();
        session.setAttribute("admin", admin);

        response.sendRedirect(request.getContextPath() + "/admin?command=DASHBOARD");
    }

    private User getAdmin() {
        String sql = "SELECT * FROM users WHERE role = 'ADMIN' LIMIT 1";
        User admin = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                admin = new User();
                admin.setId(rs.getInt("id"));
                admin.setName(rs.getString("name"));
                admin.setEmail(rs.getString("email"));
                admin.setRole(rs.getString("role"));
                admin.setSalary(rs.getDouble("salary"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }
}
