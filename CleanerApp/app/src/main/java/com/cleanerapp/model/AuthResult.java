package com.cleanerapp.model;

import com.cleanerapp.data.db.entities.UserEntity;

public class AuthResult {
    public final boolean success;
    public final UserEntity user;
    public final String errorMessage;

    private AuthResult(boolean success, UserEntity user, String errorMessage) {
        this.success = success;
        this.user = user;
        this.errorMessage = errorMessage;
    }

    public static AuthResult success(UserEntity user) {
        return new AuthResult(true, user, null);
    }

    public static AuthResult error(String message) {
        return new AuthResult(false, null, message);
    }
}
