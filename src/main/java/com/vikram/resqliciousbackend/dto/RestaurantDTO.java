package com.vikram.resqliciousbackend.dto;

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
public class RestaurantDTO {
    private Long id;
    private Long userId;
    private String name;
    private String cuisine;
    private Double rating;
    private String address;
    private Double avgPrice;
    private Long menuId;
    private String description;
    private String phone;
    private String email;
}