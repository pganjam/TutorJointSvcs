package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Course;
import com.api.tutorjoint.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByCourse(Course course);
}
