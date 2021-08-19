package com.api.tutorjoint.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Discussion {
    @Id
    private Long discussion_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", nullable = false)
    @MapsId
    private IMessage message;

    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public Long getDiscussion_id() {
        return discussion_id;
    }

    public void setDiscussion_id(Long discussion_id) {
        this.discussion_id = discussion_id;
    }

    public IMessage getMessage() {
        return message;
    }

    public void setMessage(IMessage message) {
        this.message = message;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}