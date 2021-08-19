package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Course;

import com.api.tutorjoint.model.FileResource;
import com.api.tutorjoint.model.Party;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileResource, Long> {
    List<FileResource> findByCourse(Course course, Sort sort);

    List<FileResource> findByParty(Party party, Sort sort);
}
