package com.vikram.resqliciousbackend.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderDTO {
    private long id;
    private long restaurantId;
    private long userId;
    private Map<Long, Integer> dishQuantities = new HashMap<>();
    private String pickuptime;
    private double totalPrice;
    private String paymentToken;
}
