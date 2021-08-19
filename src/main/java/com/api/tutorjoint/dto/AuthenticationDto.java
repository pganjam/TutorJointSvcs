package com.api.tutorjoint.dto;

public class AuthenticationDto {
    private String authenticationToken;
    private String username;

    public AuthenticationDto(String authenticationToken, String username) {
        this.authenticationToken = authenticationToken;
        this.username = username;
    }
}
