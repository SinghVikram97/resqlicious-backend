package com.vikram.resqliciousbackend.controller;

import com.vikram.resqliciousbackend.dto.CartDTO;
import com.vikram.resqliciousbackend.dto.OrderDTO;
import com.vikram.resqliciousbackend.dto.RestaurantDTO;
import com.vikram.resqliciousbackend.dto.UserDTO;
import com.vikram.resqliciousbackend.entity.User;
import com.vikram.resqliciousbackend.service.CartService;
import com.vikram.resqliciousbackend.service.OrderService;
import com.vikram.resqliciousbackend.service.RestaurantService;
import com.vikram.resqliciousbackend.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserDetailsService userDetailsService;
    private final RestaurantService restaurantService;
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserDTO> getUser(@PathVariable long userId){
        UserDTO user = userDetailsService.loadUserByUserId(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/restaurants")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantOfUser(@PathVariable long userId){
        User user = userDetailsService.getUserOrThrowException(userId);

        List<RestaurantDTO> restaurantByUserId = restaurantService.getRestaurantByUserId(user.getId());

        return ResponseEntity.ok(restaurantByUserId);
    }

    @GetMapping("/{userId}/carts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable long userId){
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}/orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<OrderDTO> getOrderByUserId(@PathVariable long userId){
        OrderDTO orderDTO = orderService.getOrderByUserId(userId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

}