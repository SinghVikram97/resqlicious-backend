package com.vikram.resqliciousbackend.dto;

import com.vikram.resqliciousbackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private long id;

    private String firstName;

    private String lastName;

    private String email;
}