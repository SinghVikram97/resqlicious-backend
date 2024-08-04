package com.vikram.resqliciousbackend.repository;

import com.vikram.resqliciousbackend.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
