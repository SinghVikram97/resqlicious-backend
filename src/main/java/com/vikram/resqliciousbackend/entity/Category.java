package com.vikram.resqliciousbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Dish> dishes = new ArrayList<>();

    public void addDish(Dish dish) {
        dishes.add(dish);
        dish.setCategory(this);
    }
}
