package com.vikram.resqliciousbackend.repository;

import com.vikram.resqliciousbackend.entity.Cart;
import com.vikram.resqliciousbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByUserId(Long userId);
}
