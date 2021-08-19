package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.Comment;
import com.api.tutorjoint.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDiscussion(Discussion discussion);
}
