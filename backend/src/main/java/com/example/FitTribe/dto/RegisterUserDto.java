package com.example.FitTribe.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
}
