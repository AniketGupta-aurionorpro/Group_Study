package com.aurionpro.leave_management.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private Integer managerId;
    private BigDecimal salary;
    private LocalDate joinedDate;

    // Constructor
    public User() {
    }

    public User(int id, String name, String email, String password, Role role, Integer managerId, BigDecimal salary, LocalDate joinedDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.managerId = managerId;
        this.salary = salary;
        this.joinedDate = joinedDate;
    }

    public User( String name, String email, String password, Role role, Integer managerId, BigDecimal salary, LocalDate joinedDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.managerId = managerId;
        this.salary = salary;
        this.joinedDate = joinedDate; // Default to current date
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    // Enum for role
    public enum Role {
        ADMIN,
        MANAGER,
        EMPLOYEE
    }

    // Optional: toString method
    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", managerId=" + managerId +
                ", salary=" + salary +
                ", joinedDate=" + joinedDate +
                '}';
    }
}
