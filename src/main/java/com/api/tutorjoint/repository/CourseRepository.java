package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Course;
import org.springframework.data.domain.Sort;
import com.api.tutorjoint.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTutor(Tutor tutor, Sort sort);
}
