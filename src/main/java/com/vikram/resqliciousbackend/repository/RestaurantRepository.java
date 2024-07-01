package com.vikram.resqliciousbackend.repository;

import com.vikram.resqliciousbackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByUserId(Long userId);}
