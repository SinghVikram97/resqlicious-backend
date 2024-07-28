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
public class DishDTO {
    private Long id;
    private String name;
    private Double price;
    private Long categoryId;
    private String description;
    private Integer quantity;
}
