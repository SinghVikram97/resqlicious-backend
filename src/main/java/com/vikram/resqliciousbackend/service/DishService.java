package com.vikram.resqliciousbackend.service;

import com.vikram.resqliciousbackend.dto.DishDTO;
import com.vikram.resqliciousbackend.entity.Category;
import com.vikram.resqliciousbackend.entity.Dish;
import com.vikram.resqliciousbackend.exception.ResourceNotFoundException;
import com.vikram.resqliciousbackend.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final CategoryService categoryService;

    public DishDTO addDish(DishDTO dishDTO){
        Long categoryId = dishDTO.getCategoryId();

        Category category = categoryService.getCategoryOrThrowException(categoryId);

        Dish dish = Dish.builder()
                .name(dishDTO.getName())
                .price(dishDTO.getPrice())
                .category(category)
                .description(dishDTO.getDescription())
                .quantity(dishDTO.getQuantity())
                .imageUrl(dishDTO.getImageUrl())
                .build();

        Dish savedDish = dishRepository.save(dish);

        category.addDish(savedDish);

        return DishDTO.builder()
                .id(dish.getId())
                .name(savedDish.getName())
                .categoryId(savedDish.getCategory().getId())
                .price(savedDish.getPrice())
                .description(savedDish.getDescription())
                .quantity(savedDish.getQuantity())
                .imageUrl(savedDish.getImageUrl())
                .build();

    }

    public DishDTO getDish(long dishId){
        Dish dish = getDishOrThrowException(dishId);
        return DishDTO
                .builder()
                .id(dish.getId())
                .name(dish.getName())
                .price(dish.getPrice())
                .categoryId(dish.getCategory().getId())
                .description(dish.getDescription())
                .quantity(dish.getQuantity())
                .imageUrl(dish.getImageUrl())
                .build();
    }

    public List<DishDTO> getAllDishes() {
        List<Dish> allDishes = dishRepository.findAll();

        return allDishes.stream().map(dish -> DishDTO
                .builder()
                .id(dish.getId())
                .name(dish.getName())
                .price(dish.getPrice())
                .categoryId(dish.getCategory().getId())
                .description(dish.getDescription())
                .quantity(dish.getQuantity())
                .imageUrl(dish.getImageUrl())
                .build()).toList();
    }

    public DishDTO updateDish(Long id, DishDTO dishDTO) {
        Dish dish = getDishOrThrowException(id);
        Category category = categoryService.getCategoryOrThrowException(dishDTO.getCategoryId());

        dish.setName(dishDTO.getName());
        dish.setPrice(dishDTO.getPrice());
        dish.setCategory(category);
        dish.setDescription(dishDTO.getDescription());
        dish.setQuantity(dishDTO.getQuantity());

        Dish savedDish = dishRepository.save(dish);

        category.addDish(savedDish);

        return DishDTO.builder()
                .id(dish.getId())
                .name(savedDish.getName())
                .categoryId(savedDish.getCategory().getId())
                .price(savedDish.getPrice())
                .description(savedDish.getDescription())
                .quantity(savedDish.getQuantity())
                .imageUrl(savedDish.getImageUrl())
                .build();
    }
    public Dish getDishOrThrowException(long dishId) {
        return dishRepository.findById(dishId).orElseThrow(() -> new ResourceNotFoundException("Dish", "Dish ID", dishId));
    }


    public DishDTO addDishImage(Long id, String path, MultipartFile file) throws IOException {
        Dish dish = getDishOrThrowException(id);

        // File logic
        String originalFilename = file.getOriginalFilename();


        String randomID = UUID.randomUUID().toString();
        String randomFileName= randomID.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));

        // Full path
        String filePath = path + File.separator + randomFileName;

        // Create folder if not created
        File f = new File(path);

        if(!f.exists()) {
            f.mkdir();
        }

        // File copy
        Files.copy(file.getInputStream(), Paths.get(filePath));

        dish.setImageUrl(randomFileName);


        Dish savedDish = dishRepository.save(dish);

        return DishDTO.builder()
                .id(savedDish.getId())
                .name(savedDish.getName())
                .categoryId(savedDish.getCategory().getId())
                .price(savedDish.getPrice())
                .description(savedDish.getDescription())
                .quantity(savedDish.getQuantity())
                .imageUrl(savedDish.getImageUrl())
                .build();

    }
}
