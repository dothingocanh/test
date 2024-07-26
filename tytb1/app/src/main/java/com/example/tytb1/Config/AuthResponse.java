package com.example.tytb1.Config;

public class AuthResponse {
    private String jwt;
    private boolean success;

    public AuthResponse(String jwt, boolean success) {
        this.jwt = jwt;
        this.success = success;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
