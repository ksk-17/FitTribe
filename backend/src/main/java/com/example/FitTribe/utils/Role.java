package com.example.FitTribe.utils;

public enum Role {
    ROLE_ADMIN("admin"),
    ROLE_USER("user");

    final String role;

    Role(String role) {
        this.role = role;
    }

    public String getName(){
        return role;
    }
}
