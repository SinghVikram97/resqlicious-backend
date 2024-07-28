package com.vikram.resqliciousbackend.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResetPasswordRequestDTO {
    private String token;
    private String newPassword;
}
