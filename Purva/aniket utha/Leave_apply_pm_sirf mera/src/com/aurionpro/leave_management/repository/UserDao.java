package com.aurionpro.leave_management.repository;

import com.aurionpro.leave_management.model.User;

import com.aurionpro.leave_management.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for handling all database operations related to the 'users' table.
 */
public class UserDao {

    /**
     * Finds a user by their email and password for login verification.
     * NOTE: Storing plain text passwords is a major security risk. In a real-world application,
     * use a strong hashing algorithm like BCrypt to store and verify passwords.
     *
     * @param email    The user's email.
     * @param password The user's plain text password.
     * @return A User object if credentials are valid, otherwise null.
     */
    public User findByEmailAndPassword(String email, String password) {
        // The password in the DB should be a hash. This query would need to change
        // to select by email only, then verify the hash in the service layer.
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new user in the database.
     *
     * @param user The User object containing all necessary details.
     */
    public void createUser(User user) {
        String sql = "INSERT INTO users (name, email, password, role, manager_id, salary, joined_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword()); // Remember to hash this before calling the DAO
            stmt.setString(4, user.getRole().toString());

            // Handle nullable Integer for manager_id
            if (user.getManagerId() != null) {
                stmt.setInt(5, user.getManagerId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setBigDecimal(6, user.getSalary());
            stmt.setDate(7, java.sql.Date.valueOf(user.getJoinedDate()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing user's details. Does not update the password.
     *
     * @param user The User object with updated information.
     */
    public void updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, role = ?, manager_id = ?, salary = ?, joined_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getRole().toString());
            if (user.getManagerId() != null) {
                stmt.setInt(4, user.getManagerId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setBigDecimal(5, user.getSalary());
            stmt.setDate(6, java.sql.Date.valueOf(user.getJoinedDate()));
            stmt.setInt(7, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a user from the database.
     * The database's ON DELETE SET NULL constraint will handle updating the manager_id of their direct reports.
     *
     * @param userId The ID of the user to delete.
     */
    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a single user by their primary key ID.
     *
     * @param userId The ID of the user to find.
     * @return A User object if found, otherwise null.
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return A list of all User objects.
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Retrieves all employees who report to a specific manager.
     *
     * @param managerId The ID of the manager.
     * @return A list of User objects who are direct reports of the manager.
     */
    public List<User> findEmployeesByManagerId(int managerId) {
        List<User> employees = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE manager_id = ? ORDER BY name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    /**
     * Helper method to map a ResultSet row to a User object.
     *
     * @param rs The ResultSet object.
     * @return A populated User object.
     * @throws SQLException if a database access error occurs.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setManagerId((Integer) rs.getObject("manager_id"));
        user.setSalary(rs.getBigDecimal("salary"));

        java.sql.Date joinedDate = rs.getDate("joined_date");
        if (joinedDate != null) {
            user.setJoinedDate(joinedDate.toLocalDate());
        }


        return user;
    }
}