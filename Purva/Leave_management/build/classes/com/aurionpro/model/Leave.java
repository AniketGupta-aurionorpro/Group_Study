package com.aurionpro.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Leave {
    private int id;
    private int userId;
    private Integer managerId;
    private Date startDate;
    private Date endDate;
    private String reason;
    private String status;
    private Timestamp appliedOn; // Corrected type
    private Integer approvedBy;
    private Timestamp approvedOn; // Corrected type
    private String rejectionReason;

    // Getters and Setters for all fields
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getAppliedOn() { // Corrected return type
		return appliedOn;
	}

	public void setAppliedOn(Timestamp appliedOn) { // Corrected parameter type
		this.appliedOn = appliedOn;
	}
	
	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Timestamp getApprovedOn() { // Corrected return type
		return approvedOn;
	}

	public void setApprovedOn(Timestamp approvedOn) { // Corrected parameter type
		this.approvedOn = approvedOn;
	}
	
	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
}
