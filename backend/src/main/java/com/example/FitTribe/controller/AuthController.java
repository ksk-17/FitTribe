package com.example.FitTribe.controller;

import com.example.FitTribe.dto.AuthResponseDto;
import com.example.FitTribe.dto.LoginUserDto;
import com.example.FitTribe.dto.RegisterUserDto;
import com.example.FitTribe.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterUserDto userDto){
        return ResponseEntity.ok(authService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginUserDto userDto){
        return ResponseEntity.ok(authService.login(userDto));
    }
}
