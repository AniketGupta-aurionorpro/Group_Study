package com.notification.model;
 

public class Notification {
    private int id;
    private int receiverId;
    private String message;
    private boolean seen;
    private String createdOn;

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }
    public String getCreatedOn() { return createdOn; }
    public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }
}
