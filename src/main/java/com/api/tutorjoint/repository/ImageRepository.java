package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
