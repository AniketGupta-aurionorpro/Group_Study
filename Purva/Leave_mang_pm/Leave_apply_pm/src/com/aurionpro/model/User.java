package com.aurionpro.model;

import java.math.BigDecimal;
import java.sql.Date;

public class User {
	
	    public enum UserRole {
	        ADMIN, MANAGER, EMPLOYEE
	    }

	    private int id;
	    private String name;
	    private String email;
	    private String password;
	    private UserRole role;
	    private Integer managerId;
	    private BigDecimal salary;
	    private Date joinedDate;

	    
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

		public UserRole getRole() {
			return role;
		}

		public void setRole(UserRole role) {
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

		public Date getJoinedDate() {
			return joinedDate;
		}

		public void setJoinedDate(Date joinedDate) {
			this.joinedDate = joinedDate;
		}
	}
