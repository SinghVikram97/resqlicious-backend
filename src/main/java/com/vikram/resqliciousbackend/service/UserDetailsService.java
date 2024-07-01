package com.vikram.resqliciousbackend.service;

import com.vikram.resqliciousbackend.dto.UserDTO;
import com.vikram.resqliciousbackend.entity.User;
import com.vikram.resqliciousbackend.exception.ResourceNotFoundException;
import com.vikram.resqliciousbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+username));
    }

    public UserDTO loadUserByUserId(Long id) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();

            return UserDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail()).build();
        }else{
            throw new UsernameNotFoundException("User not found with id: "+id);
        }
    }

    public User getUserOrThrowException(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User ID", userId));
    }

}