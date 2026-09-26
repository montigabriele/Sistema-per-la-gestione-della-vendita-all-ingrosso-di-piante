package it.verde.view.bean;

public class UserBean {
    private String username;
    private String password;
    private String role;

    public UserBean() {
        // empty
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = validateUsername(username); }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = validatePassword(password); }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = validateRole(role); }

    private String validateUsername(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim();
        if (!normalized.matches("^\\w{3,20}$")) throw new IllegalArgumentException("Username must contain 3 to 20 word characters");
        return normalized;
    }

    private String validatePassword(String value) {
        if (value == null || value.isBlank()) return value;
        if (!value.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$")) throw new IllegalArgumentException("Password must be at least 8 characters and include upper case, lower case and a digit");
        return value;
    }

    private String validateRole(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.matches("ADMINISTRATOR|SALES_MANAGER|LOGISTICS_MANAGER")) throw new IllegalArgumentException("Invalid user role");
        return normalized;
    }
}
