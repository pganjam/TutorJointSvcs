package com.api.tutorjoint.dto;

import java.util.Date;

public class AppointmentDto {

    private Long tutorId;
    private String[] timeLabel;
    private Date date;

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public String[] getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String[] timeLabel) {
        this.timeLabel = timeLabel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
