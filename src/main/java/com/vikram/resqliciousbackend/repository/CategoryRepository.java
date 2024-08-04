package com.vikram.resqliciousbackend.repository;

import com.vikram.resqliciousbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
