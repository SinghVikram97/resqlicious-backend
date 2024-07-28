package com.vikram.resqliciousbackend.controller;

import com.vikram.resqliciousbackend.dto.ResetPasswordRequestDTO;
import com.vikram.resqliciousbackend.entity.PasswordResetToken;
import com.vikram.resqliciousbackend.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        passwordResetService.sendResetToken(email);
        return ResponseEntity.ok("Password reset link sent");
    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPasswordForm(@RequestParam String token) {
        boolean isValid = passwordResetService.checkTokenValid(token);

        if(!isValid){
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }else{
            return ResponseEntity.ok("Token is valid");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
        boolean isValid = passwordResetService.checkTokenValid(resetPasswordRequestDTO.getToken());
        if(!isValid){
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        passwordResetService.resetPassword(resetPasswordRequestDTO.getToken(), resetPasswordRequestDTO.getNewPassword());

        return ResponseEntity.ok("Password successfully reset");
    }
}
