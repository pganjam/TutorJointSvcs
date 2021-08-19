package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.IMessage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IMessageRepository extends JpaRepository<IMessage, Long> {

}
