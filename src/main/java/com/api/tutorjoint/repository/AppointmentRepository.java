package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Appointment;
import com.api.tutorjoint.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByTutor(Tutor tutor);

    List<Appointment> findByTutorAndDate(Tutor tutor, Instant date);
}
