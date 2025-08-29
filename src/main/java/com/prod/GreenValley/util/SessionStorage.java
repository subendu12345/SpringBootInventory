package com.prod.GreenValley.util;

public class SessionStorage {
    private Long userId;
    private String message;
    private String role;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
   
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
