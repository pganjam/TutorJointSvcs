package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
