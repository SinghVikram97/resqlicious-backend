package com.example.dishapp.controller;

import com.example.dishapp.dto.DishDTO;
import com.example.dishapp.entity.Dish;
import com.example.dishapp.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishRepository dishRepository;

    @PostMapping
    public ResponseEntity<DishDTO> createDish(@RequestParam("name") String name, @RequestParam("photo") MultipartFile photo) throws IOException {
        Dish dish = new Dish();
        dish.setName(name);
        dish.setPhoto(photo.getBytes());
        Dish savedDish = dishRepository.save(dish);

        DishDTO dishDTO = new DishDTO(savedDish.getId(), savedDish.getName(), Base64Utils.encodeToString(savedDish.getPhoto()));
        return ResponseEntity.ok(dishDTO);
    }

    @GetMapping
    public List<DishDTO> getAllDishes() {
        return dishRepository.findAll().stream().map(dish -> new DishDTO(dish.getId(), dish.getName(), Base64Utils.encodeToString(dish.getPhoto()))).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDishById(@PathVariable Long id) {
        return dishRepository.findById(id)
                .map(dish -> new DishDTO(dish.getId(), dish.getName(), Base64Utils.encodeToString(dish.getPhoto())))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
