package com.vikram.resqliciousbackend.repository;

import com.vikram.resqliciousbackend.entity.PasswordResetToken;
import com.vikram.resqliciousbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(User user);
}
