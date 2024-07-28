package com.vikram.resqliciousbackend.service;

import com.vikram.resqliciousbackend.entity.PasswordResetToken;
import com.vikram.resqliciousbackend.entity.User;
import com.vikram.resqliciousbackend.repository.PasswordResetTokenRepository;
import com.vikram.resqliciousbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendResetToken(String email) {
        Optional<User> userByEmailOptional = userRepository.findByEmail(email);
        if(userByEmailOptional.isEmpty()){
            throw new UsernameNotFoundException("User not found with email: "+email);
        }

        User user = userByEmailOptional.get();

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(resetToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click the link below:\n"
                + "http://localhost:8080/api/v1/reset-password?token=" + token);
        mailSender.send(mailMessage);
    }

    public boolean checkTokenValid(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);

        return !Objects.isNull(resetToken) && !resetToken.getExpiryDate().isBefore(LocalDateTime.now());
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}
