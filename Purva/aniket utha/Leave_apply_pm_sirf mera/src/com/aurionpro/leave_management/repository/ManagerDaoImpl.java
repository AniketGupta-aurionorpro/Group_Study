package com.aurionpro.leave_management.repository;

import com.aurionpro.leave_management.model.User;
import com.aurionpro.leave_management.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;



public class ManagerDaoImpl implements ManagerDao {

    private DataSource dataSource;

    public ManagerDaoImpl() {
//        this.dataSource = dataSource;
    }

    @Override
    public List<User> getEmployeesByManagerId(int managerId) throws SQLException {
        List<User> employees = new ArrayList<>();

        String sql = "SELECT id, name, email, role, salary, joined_date " +
                     "FROM users WHERE manager_id = ? AND role = 'EMPLOYEE'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, managerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    
                    // Convert String to your UserRole enum
                    String roleString = rs.getString("role");
                    user.setRole(User.Role.valueOf(roleString.toUpperCase()));
                    
                    user.setSalary(rs.getBigDecimal("salary"));
                    user.setJoinedDate(rs.getDate("joined_date").toLocalDate());
                    user.setManagerId(managerId); // Populate the managerId field

                    employees.add(user);

                    System.out.println("Employee found: " + user.getName());
                }
            }
        }
        return employees;
    }
}
