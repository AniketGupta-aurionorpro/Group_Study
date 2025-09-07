package com.aurionpro.leave_management.model;

public class LeaveBalance {
    private int id;
    private int userId;
    private int year;
    private int totalYearlyLeave;
    private int yearlyLeaveTaken;

    public int getRemainingLeave() {
        return totalYearlyLeave - yearlyLeaveTaken;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getTotalYearlyLeave() { return totalYearlyLeave; }
    public void setTotalYearlyLeave(int totalYearlyLeave) { this.totalYearlyLeave = totalYearlyLeave; }
    public int getYearlyLeaveTaken() { return yearlyLeaveTaken; }
    public void setYearlyLeaveTaken(int yearlyLeaveTaken) { this.yearlyLeaveTaken = yearlyLeaveTaken; }
}