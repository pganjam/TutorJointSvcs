package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Contact;
import com.api.tutorjoint.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByParty(Party party);
}
