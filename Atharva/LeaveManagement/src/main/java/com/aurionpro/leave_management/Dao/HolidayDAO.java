package com.aurionpro.leave_management.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.google.gson.Gson;

public class HolidayDAO {
	private DataSource dataSource;

	public HolidayDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<Map<String, String>> getAllHolidays() {
		List<Map<String, String>> holidays = new ArrayList<>();
		String sql = "SELECT holiday_date, reason FROM holidays ORDER BY holiday_date";
		try (Connection con = dataSource.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Map<String, String> holiday = new HashMap<>();
				holiday.put("date", rs.getString("holiday_date"));
				holiday.put("reason", rs.getString("reason"));
				holidays.add(holiday);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return holidays;
	}

	public int addHoliday(String date, String reason) throws SQLException {
		int rows = 0;
		Connection con = dataSource.getConnection();
		con.setAutoCommit(false); // Start transaction

		// 1. Insert the holiday
		try (PreparedStatement ps = con.prepareStatement("INSERT INTO holidays (holiday_date, reason) VALUES (?, ?)")) {
			ps.setString(1, date);
			ps.setString(2, reason);
			rows = ps.executeUpdate();
		}

		// 2. Reject all leaves that start or end on this date (PENDING or APPROVED)
		try (PreparedStatement ps = con
				.prepareStatement("UPDATE leaves SET status = 'REJECTED' WHERE start_date = ? OR end_date = ?")) {
			ps.setString(1, date);
			ps.setString(2, date);
			ps.executeUpdate();
		}

		// 3. Send notifications to affected users
		try (PreparedStatement ps = con.prepareStatement("INSERT INTO notifications (receiver_id, message) "
				+ "SELECT user_id, CONCAT('Your leave on ', ?, ' has been cancelled as it is now a holiday') "
				+ "FROM leaves WHERE (start_date = ? OR end_date = ?) AND status = 'REJECTED'")) {
			ps.setString(1, date);
			ps.setString(2, date);
			ps.setString(3, date);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		con.commit(); 
		return rows;
	}

	public int deleteHoliday(String date) throws SQLException {
		int rows = 0;
		try (Connection con = dataSource.getConnection()) {
			PreparedStatement ps = con.prepareStatement("DELETE FROM holidays WHERE holiday_date = ?");
			ps.setString(1, date);
			rows = ps.executeUpdate();
		}
		return rows;
	}

	public int sendHolidayAddedNotification(String date, String reason) throws SQLException {
		String getUsersSql = "SELECT id FROM users";
		String insertNotifSql = "INSERT INTO notifications (receiver_id, message) VALUES (?, ?)";
		int rows = 0;

		try (Connection con = dataSource.getConnection();
				PreparedStatement psUsers = con.prepareStatement(getUsersSql);
				ResultSet rs = psUsers.executeQuery()) {

			while (rs.next()) {
				int userId = rs.getInt("id");
				String message = String.format("New holiday added on %s for %s", date, reason);

				try (PreparedStatement psNotif = con.prepareStatement(insertNotifSql)) {
					psNotif.setInt(1, userId);
					psNotif.setString(2, message);
					rows += psNotif.executeUpdate();
				}
			}
		}
		return rows;
	}

	public int sendHolidayDeletedNotification(String date) throws SQLException {
		String getUsersSql = "SELECT id FROM users";
		String insertNotifSql = "INSERT INTO notifications (receiver_id, message) VALUES (?, ?)";
		int rows = 0;

		try (Connection con = dataSource.getConnection();
				PreparedStatement psUsers = con.prepareStatement(getUsersSql);
				ResultSet rs = psUsers.executeQuery()) {

			while (rs.next()) {
				int userId = rs.getInt("id");
				String message = String.format("Holiday on %s has been cancelled.", date);

				try (PreparedStatement psNotif = con.prepareStatement(insertNotifSql)) {
					psNotif.setInt(1, userId);
					psNotif.setString(2, message);
					rows += psNotif.executeUpdate();
				}
			}
		}
		return rows;
	}

}