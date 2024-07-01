package com.vikram.resqliciousbackend.controller;


import com.vikram.resqliciousbackend.dto.AuthRequest;
import com.vikram.resqliciousbackend.dto.AuthResponse;
import com.vikram.resqliciousbackend.dto.RegisterRequest;
import com.vikram.resqliciousbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request
    ) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }
}