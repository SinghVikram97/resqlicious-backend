package com.vikram.resqliciousbackend.controller;

import com.vikram.resqliciousbackend.dto.CategoryDTO;
import com.vikram.resqliciousbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO addedCategory = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCategory);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategory(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(allCategories);
    }
}

