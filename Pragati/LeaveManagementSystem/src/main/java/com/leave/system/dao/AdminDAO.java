package com.leave.system.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.leave.system.model.User;

public class AdminDAO {

    // Add a new user
	// Add a new user
	public boolean addUser(User user) {
	    String sql = "INSERT INTO users (name, email, password, role, manager_id, joined_date) VALUES (?, ?, ?, ?, ?, ?)";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, user.getName());
	        stmt.setString(2, user.getEmail());
	        stmt.setString(3, user.getPassword());
	        stmt.setString(4, user.getRole());

	        if (user.getManagerId() != null) {
	            stmt.setInt(5, user.getManagerId());
	        } else {
	            stmt.setNull(5, Types.INTEGER);
	        }

	        if (user.getJoinedDate() != null) {
	            stmt.setDate(6, user.getJoinedDate());  // from JSP
	        } else {
	            stmt.setDate(6, new java.sql.Date(System.currentTimeMillis())); // fallback to today
	        }

	        int rows = stmt.executeUpdate();
	        System.out.println("✅ User added: " + user.getName() + " | Rows affected: " + rows);
	        return rows > 0;

	    } catch (SQLException e) {
	        System.err.println("❌ Error inserting user: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}


    // Fetch all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email, password, role, manager_id FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );

                int managerId = rs.getInt("manager_id");
                if (!rs.wasNull()) {
                    user.setManagerId(managerId);
                }

                System.out.println("👤 User fetched: ID=" + user.getId() + ", Name=" + user.getName() + ", Role=" + user.getRole());
                users.add(user);
            }

            System.out.println("📌 Total users fetched: " + users.size());

        } catch (SQLException e) {
            System.err.println("❌ Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    // Get a user by ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                    int managerId = rs.getInt("manager_id");
                    if (!rs.wasNull()) user.setManagerId(managerId);

                    System.out.println("🔎 User found by ID " + id + ": " + user.getName());
                    return user;
                } else {
                    System.out.println("⚠️ No user found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Update a user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ?, manager_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            if (user.getManagerId() != null) {
                stmt.setInt(5, user.getManagerId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, user.getId());

            int rows = stmt.executeUpdate();
            System.out.println("✏️ User updated: ID=" + user.getId() + ", Rows affected=" + rows);
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean hasEmployees(int managerId) {
        String sql = "SELECT COUNT(*) AS empCount FROM users WHERE manager_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("empCount") > 0; // true if manager has employees
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
