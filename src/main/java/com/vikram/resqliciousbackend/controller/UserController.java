package com.vikram.resqliciousbackend.controller;

import com.vikram.resqliciousbackend.dto.UserDTO;
import com.vikram.resqliciousbackend.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserDetailsService userDetailsService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<UserDTO> getUser(@PathVariable long userId){
        UserDTO user = userDetailsService.loadUserByUserId(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}