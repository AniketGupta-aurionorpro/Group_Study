package com.aurionpro.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.google.gson.Gson;

import com.aurionpro.Dao.HolidayDAO;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/holidays")
public class HolidayServlet extends HttpServlet {

    @Resource(name = "jdbc/db-source")
    private DataSource dataSource;

    HolidayDAO dao;
    
    @Override
    public void init() throws ServletException {
        dao = new HolidayDAO(dataSource);  // now dataSource is injected
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Map<String, String>> holidays = dao.getAllHolidays();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new Gson().toJson(holidays, response.getWriter());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action"); // add or delete
        String date = request.getParameter("date");
        String reason = request.getParameter("reason");
        int rows=0;
        int notifRows = 0;
        try {
	        switch(action){
	        case "add":
	        	rows = dao.addHoliday(date,reason);
	        	notifRows = dao.sendHolidayAddedNotification(date, reason);
	        	break;
	        case "delete":
	        	rows = dao.deleteHoliday(date);
	        	notifRows = dao.sendHolidayDeletedNotification(date);
	        }
	        response.setContentType("application/json");
            response.getWriter().write("{\"success\":" + (rows > 0) + "}");
        }catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false}");
        }

    }
}

