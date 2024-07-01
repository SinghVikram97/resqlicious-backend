package com.vikram.resqliciousbackend.service;

import com.vikram.resqliciousbackend.dto.CategoryDTO;
import com.vikram.resqliciousbackend.dto.DishDTO;
import com.vikram.resqliciousbackend.entity.Category;
import com.vikram.resqliciousbackend.entity.Dish;
import com.vikram.resqliciousbackend.entity.Menu;
import com.vikram.resqliciousbackend.exception.ResourceNotFoundException;
import com.vikram.resqliciousbackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MenuService menuService;

    public CategoryDTO getCategory(Long categoryId){
        Category category = getCategoryOrThrowException(categoryId);
        List<Long> dishIdList = category.getDishes().stream().map(Dish::getId).toList();
        return CategoryDTO
                .builder()
                .id(category.getId())
                .name(category.getName())
                .dishIdList(dishIdList)
                .menuId(category.getMenu().getId())
                .build();
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Long menuId = categoryDTO.getMenuId();
        Menu menu = menuService.getMenuOrThrowException(menuId);

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .menu(menu)
                .dishes(Collections.emptyList())
                .build();

        Category savedCategory = categoryRepository.save(category);

        menu.addCategory(savedCategory);

        return CategoryDTO.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .menuId(savedCategory.getMenu().getId())
                .dishIdList(savedCategory.getDishes().stream().map(Dish::getId).collect(Collectors.toList()))
                .build();
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream().map(category -> CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .menuId(category.getMenu().getId())
                .dishIdList(category.getDishes().stream().map(Dish::getId).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    public Category getCategoryOrThrowException(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", categoryId));
    }
}
