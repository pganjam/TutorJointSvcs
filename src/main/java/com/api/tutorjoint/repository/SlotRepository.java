package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByMeasure(String measure);


}
