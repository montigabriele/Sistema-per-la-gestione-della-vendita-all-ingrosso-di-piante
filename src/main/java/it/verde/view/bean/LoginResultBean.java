package it.verde.view.bean;

public class LoginResultBean {
    private String username;
    private String role;
    private Integer errorCode;
    private String errorMessage;

    public LoginResultBean() {
        // empty
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = validateRole(role); }
    public Integer getErrorCode() { return errorCode; }
    public void setErrorCode(Integer errorCode) { this.errorCode = errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public boolean isSuccessful() { return errorCode != null && errorCode == 0; }

    private String validateRole(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.matches("ADMINISTRATOR|SALES_MANAGER|LOGISTICS_MANAGER")) throw new IllegalArgumentException("Invalid user role");
        return normalized;
    }
}
