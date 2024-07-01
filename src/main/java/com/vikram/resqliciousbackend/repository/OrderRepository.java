package com.vikram.resqliciousbackend.repository;

import com.vikram.resqliciousbackend.entity.Cart;
import com.vikram.resqliciousbackend.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserId(Long userId);
    List<Order> findAllByRestaurantId(Long restaurantId);
}
