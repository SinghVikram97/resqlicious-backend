package com.vikram.resqliciousbackend.controller;

import com.vikram.resqliciousbackend.dto.DishDTO;
import com.vikram.resqliciousbackend.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dishes")
public class DishController {
    private final DishService dishService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<DishDTO> addDish(@RequestBody DishDTO dishDTO) {
        DishDTO addedDish = dishService.addDish(dishDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedDish);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<DishDTO> getDish(@PathVariable Long id) {
        DishDTO dishDTO = dishService.getDish(id);
        return ResponseEntity.ok(dishDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<DishDTO> updateDish(@PathVariable Long id, @RequestBody DishDTO dishDTO) {
        DishDTO updatedDishDTO = dishService.updateDish(id, dishDTO);
        return ResponseEntity.ok(updatedDishDTO);
    }



    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<DishDTO>> getAllCategories() {
        List<DishDTO> allDishes = dishService.getAllDishes();
        return ResponseEntity.ok(allDishes);
    }
}
