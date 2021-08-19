package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Tutor;
import com.api.tutorjoint.model.TutorSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorSlotRepository extends JpaRepository<TutorSlot, Long> {
    List<TutorSlot> findByTutor(Tutor tutor);

}
