package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByItemId(String itemId);
}
