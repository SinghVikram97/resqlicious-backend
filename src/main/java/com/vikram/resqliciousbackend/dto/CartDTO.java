package com.vikram.resqliciousbackend.dto;

import com.vikram.resqliciousbackend.entity.Restaurant;
import com.vikram.resqliciousbackend.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CartDTO {
    private long id;
    private long restaurantId;
    private long userId;
    private Map<Long, Integer> dishQuantities = new HashMap<>();
}

