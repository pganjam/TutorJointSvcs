package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByParty(Party party);
}