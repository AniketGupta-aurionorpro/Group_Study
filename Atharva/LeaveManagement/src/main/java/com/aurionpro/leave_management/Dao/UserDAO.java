package com.aurionpro.leave_management.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.aurionpro.leave_management.model.Leave;
import com.aurionpro.leave_management.model.User;

public class UserDAO {
	
	private DataSource datasource;
	
	public UserDAO(DataSource datasource){
		this.datasource = datasource;
	}

	public User getManagerById(int id) {
        User user = null;
        try (Connection con = datasource.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id=? AND role='MANAGER'")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setSalary(rs.getDouble("salary"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
	
	public List<User> getApprovedLeaveUsers(Leave leave) {
	    List<User> users = new ArrayList<>();
	    String query = "SELECT u.* " +
	                   "FROM users u " +
	                   "JOIN leaves l ON u.id = l.user_id " +
	                   "WHERE l.status = 'APPROVED' " +
	                   "AND NOT (l.end_date < ? OR l.start_date > ?)";

	    try (Connection con = datasource.getConnection();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setDate(1, leave.getStartDate());
	        ps.setDate(2, leave.getEndDate());

	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            User user = new User();
	            user.setId(rs.getInt("id"));
	            user.setName(rs.getString("name"));
	            user.setEmail(rs.getString("email"));
	            user.setRole(rs.getString("role"));
	            user.setSalary(rs.getDouble("salary"));
	            users.add(user);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return users;
	}

}
