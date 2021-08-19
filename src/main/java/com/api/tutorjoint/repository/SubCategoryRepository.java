package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Category;
import com.api.tutorjoint.model.SubCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategory(Category category, Sort sort);
}
