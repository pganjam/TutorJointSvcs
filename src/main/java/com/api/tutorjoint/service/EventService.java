package com.api.tutorjoint.service;

import com.api.tutorjoint.dto.EventDto;
import com.api.tutorjoint.exception.CourseNotFoundException;
import com.api.tutorjoint.model.Course;
import com.api.tutorjoint.model.Event;
import com.api.tutorjoint.repository.CourseRepository;
import com.api.tutorjoint.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public long createEvent(EventDto eventDto) throws CourseNotFoundException {
        Course course = null;

        try {
            course = courseRepository.findById(eventDto.getCourse_id()).orElseThrow(() -> new CourseNotFoundException("Course does not exist " + eventDto.getCourse_id()));
        } catch (Exception e) {
            throw new CourseNotFoundException("Course does not exist");
        }

        Event event = new Event();
        event.setCourse(course);
        event.setStart(eventDto.getStart());
        event.setEnd(eventDto.getEnd());
        event.setTitle(eventDto.getTitle());
        event.setCreatedOn(Instant.now());
        event.setUpdatedOn(Instant.now());
        eventRepository.save(event);
        return event.getId();
    }

    @Transactional
    public long updateEvent(EventDto eventDto) throws CourseNotFoundException {
        Course course = null;

        try {
            course = courseRepository.findById(eventDto.getCourse_id()).orElseThrow(() -> new CourseNotFoundException("Course does not exist " + eventDto.getCourse_id()));
        } catch (Exception e) {
            throw new CourseNotFoundException("Course does not exist");
        }

        Event event = new Event();
        event.setCourse(course);
        event.setStart(eventDto.getStart());
        event.setEnd(eventDto.getEnd());
        event.setTitle(eventDto.getTitle());
        event.setId(eventDto.getId());
        event.setUpdatedOn(Instant.now());
        eventRepository.save(event);
        return event.getId();
    }

    @Transactional
    public void deleteEvent(long id) throws CourseNotFoundException {
        Event event = null;
        try {
            event = eventRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("Event does not exist"));
        } catch (Exception e) {
            throw new CourseNotFoundException("Event does not exist");
        }
        eventRepository.delete(event);
    }

    public List<EventDto> getEvents(long course_id) throws CourseNotFoundException {
        Course course = null;

        try {
            course = courseRepository.findById(course_id).orElseThrow(() -> new CourseNotFoundException("Course does not exist " + course_id));
        } catch (Exception e) {
            throw new CourseNotFoundException("Course does not exist");
        }

        List<Event> listEvents = eventRepository.findByCourse(course, Sort.by(Sort.Direction.ASC, "createdOn"));

        List<EventDto> listEventDto = new ArrayList<EventDto>();
        for (Event event : listEvents) {
            EventDto dto = new EventDto();
            dto.setCourse_id(event.getCourse().getId());
            dto.setStart(event.getStart());
            dto.setEnd(event.getEnd());
            dto.setTitle(event.getTitle());
            dto.setId(event.getId());
            listEventDto.add(dto);
        }
        return listEventDto;
    }
}
