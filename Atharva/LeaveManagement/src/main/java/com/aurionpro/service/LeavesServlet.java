package com.aurionpro.service;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import com.aurionpro.Dao.LeaveDAO;
import com.aurionpro.model.Leave;
import com.google.gson.Gson;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/leavesByDate")
public class LeavesServlet extends HttpServlet {

    @Resource(name = "jdbc/db-source")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String date = request.getParameter("date"); // yyyy-MM-dd
        LeaveDAO leaveDao = new LeaveDAO(dataSource);

        List<Leave> leaves = leaveDao.getLeavesByDate(date);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new Gson().toJson(leaves, response.getWriter());
    }
}
