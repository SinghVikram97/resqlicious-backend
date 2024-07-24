package com.vikram.resqliciousbackend.controller;


import com.vikram.resqliciousbackend.dto.AuthRequest;
import com.vikram.resqliciousbackend.dto.AuthResponse;
import com.vikram.resqliciousbackend.dto.RegisterRequest;
import com.vikram.resqliciousbackend.service.AuthService;
import com.vikram.resqliciousbackend.service.UserDetailsService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;
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
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email
    ) {
        try {
            userDetailsService.createPasswordResetToken(email);
            return new ResponseEntity<>("Password reset token generated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error generating token", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        try {
            userDetailsService.resetPassword(token, newPassword);
            return new ResponseEntity<>("Password reset successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error resetting password", HttpStatus.BAD_REQUEST);
        }
    }

}