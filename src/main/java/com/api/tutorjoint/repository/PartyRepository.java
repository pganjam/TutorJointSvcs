package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findById(Long id);

    Optional<Party> findByEmailAddress(String emailAddress);

    List<Party> findAll();

    List<Party> findByPartyType(String partyType);
}