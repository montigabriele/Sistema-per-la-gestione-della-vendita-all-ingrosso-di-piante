package it.verde.model.readmodel;

import it.verde.model.type.UserRole;

public record AuthenticationResult(String username, UserRole role, int errorCode, String errorMessage) {
    public boolean isSuccessful() { return errorCode == 0 && role != null; }
}
