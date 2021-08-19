package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Course;
import com.api.tutorjoint.model.Goal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByCourse(Course course, Sort sort);
}
