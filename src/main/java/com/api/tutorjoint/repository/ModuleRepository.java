package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Course;
import com.api.tutorjoint.model.Module;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCourse(Course course, Sort sort);
}
