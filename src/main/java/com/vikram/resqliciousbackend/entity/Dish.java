package com.vikram.resqliciousbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dish")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private Double price;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer quantity;

    @Column(name = "image_url")
    private String imageUrl;
}
