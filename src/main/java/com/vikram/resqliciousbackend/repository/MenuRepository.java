package com.vikram.resqliciousbackend.repository;

import com.vikram.resqliciousbackend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
