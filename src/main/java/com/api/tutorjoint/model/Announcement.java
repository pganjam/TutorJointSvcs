package com.api.tutorjoint.model;

import javax.persistence.*;

@Entity
public class Announcement {
    @Id
    private Long announcement_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    @MapsId
    private IMessage message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public IMessage getMessage() {
        return message;
    }

    public void setMessage(IMessage message) {
        this.message = message;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Long getAnnouncement_id() {
        return announcement_id;
    }

    public void setAnnouncement_id(Long announcement_id) {
        this.announcement_id = announcement_id;
    }
}