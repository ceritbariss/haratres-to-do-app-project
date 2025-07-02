package com.todoapp.todoapp.dto.response;

public class AuthResponse {

    private String jwt;

    public AuthResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
