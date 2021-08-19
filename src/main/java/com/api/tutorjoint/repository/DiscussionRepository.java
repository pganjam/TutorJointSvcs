package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Course;
import com.api.tutorjoint.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findByCourse(Course course);
}
