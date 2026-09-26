package it.verde.model.entity;

import it.verde.model.type.UserRole;

public class UserAccount {
    private String username;
    private UserRole role;

    public UserAccount(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
