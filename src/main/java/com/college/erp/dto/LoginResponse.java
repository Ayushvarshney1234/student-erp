package com.college.erp.dto;

public class LoginResponse {
    private Long userId;
    private String username;
    private String email;
    private String role;
    private Long profileId; // Student or Faculty entity ID if applicable
    private String name;

    public LoginResponse() {
    }

    public LoginResponse(Long userId, String username, String email, String role, Long profileId, String name) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.profileId = profileId;
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
