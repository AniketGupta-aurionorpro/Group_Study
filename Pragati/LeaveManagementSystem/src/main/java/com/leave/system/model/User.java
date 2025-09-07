package com.leave.system.model;

import java.sql.Date;

public class User {

	private int id;
	private String name;
	private String email;
	private String password;
	private String role;
	private Integer managerId;
	private Date joinedDate;

	public User(int id, String name, String email, String password, String role) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public Date getJoinedDate() {
		return joinedDate;
	}

	// Setters
	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}
}
