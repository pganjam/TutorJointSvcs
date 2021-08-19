package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.model.Enrollment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByParty(Party party, Sort sort);
}
