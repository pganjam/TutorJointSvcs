package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Course;
import com.api.tutorjoint.model.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCourse(Course course, Sort sort);
}
