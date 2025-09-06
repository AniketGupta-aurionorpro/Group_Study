package com.notification.model;
// 
//import java.sql.Timestamp;
//
//public class Leave {
//    private int id;
//    private int userId;
//    private String startDate;
//    private String endDate;
//    private String reason;
//    private String status;
//    private String rejectionReason;
//    private int approvedBy;
//    private Timestamp appliedOn;
//
//    // Getters and setters
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//
//    public int getUserId() { return userId; }
//    public void setUserId(int userId) { this.userId = userId; }
//
//    public String getStartDate() { return startDate; }
//    public void setStartDate(String startDate) { this.startDate = startDate; }
//
//    public String getEndDate() { return endDate; }
//    public void setEndDate(String endDate) { this.endDate = endDate; }
//
//    public String getReason() { return reason; }
//    public void setReason(String reason) { this.reason = reason; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public String getRejectionReason() { return rejectionReason; }
//    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
//
//    public int getApprovedBy() { return approvedBy; }
//    public void setApprovedBy(int approvedBy) { this.approvedBy = approvedBy; }
//
//    public Timestamp getAppliedOn() { return appliedOn; }
//    public void setAppliedOn(Timestamp appliedOn) { this.appliedOn = appliedOn; }
//}
















 

public class Leave {
    private int id;
    private int userId;
    private String employeeName;
    private String startDate;
    private String endDate;
    private String reason;
    private String status;
    private String rejectionReason;

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
