package com.vikram.resqliciousbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "cart")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection
    @CollectionTable(name = "cart_dishes", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "dish_id")
    @Column(name = "quantity")
    private Map<Long, Integer> dishQuantities = new HashMap<>();
}
