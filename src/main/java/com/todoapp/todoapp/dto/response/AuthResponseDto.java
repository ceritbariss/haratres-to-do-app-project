package com.todoapp.todoapp.dto.response;

public class AuthResponseDto {

    private String jwt;

    public AuthResponseDto(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
