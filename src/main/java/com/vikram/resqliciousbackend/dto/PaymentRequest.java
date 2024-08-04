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
public class PaymentRequest {
    private String token;
    private Double amount;
    private Long userId;
    private Long restaurantId;
    private Long cartId;
}
