package com.vikram.resqliciousbackend.controller;

import com.vikram.resqliciousbackend.dto.OrderDTO;
import com.vikram.resqliciousbackend.dto.RestaurantDTO;
import com.vikram.resqliciousbackend.service.OrderService;
import com.vikram.resqliciousbackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final OrderService orderService;

    @Value("${project.image}")
    private String path;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<RestaurantDTO> addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO addedRestaurant = restaurantService.addRestaurant(restaurantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRestaurant);
    }

    @PutMapping("/{id}/restaurantImage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<RestaurantDTO> addRestaurantImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) throws IOException {
        RestaurantDTO updatedRestaurant = restaurantService.addRestaurantImage(id, path, image);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRestaurant);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable Long id) {
        RestaurantDTO restaurantDTO = restaurantService.getRestaurant(id);
        return ResponseEntity.ok(restaurantDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<RestaurantDTO> allRestaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(allRestaurants);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO updateRestaurant = restaurantService.updateRestaurant(id, restaurantDTO);
        return ResponseEntity.ok(updateRestaurant);
    }

    @GetMapping("/{id}/orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<OrderDTO>> getAllOrdersForRestaurant(@PathVariable Long id) {
        List<OrderDTO> allOrdersForRestaurant = orderService.getAllOrdersForRestaurant(id);
        return ResponseEntity.ok(allOrdersForRestaurant);
    }
}
