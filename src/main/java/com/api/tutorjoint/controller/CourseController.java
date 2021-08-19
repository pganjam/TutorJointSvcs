package com.api.tutorjoint.controller;

import java.util.List;

import com.api.tutorjoint.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.dto.CourseDto;
import com.api.tutorjoint.model.FileInfo;
import com.api.tutorjoint.dto.DiscussionDto;
import com.api.tutorjoint.service.PartyService;
import com.api.tutorjoint.service.CourseService;
import com.api.tutorjoint.service.MessageService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private FileStorageService fileSvc;

    @PostMapping
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity createCourse(@RequestBody CourseDto courseDto) {
        courseService.createCourse(courseDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/tutor/{Id}")
    public ResponseEntity<List<CourseDto>> getCoursesByTutorId(@PathVariable @RequestBody Long Id) {
        return new ResponseEntity<>(courseService.getCoursesByTutor(Id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable @RequestBody Long id) {
        return new ResponseEntity<>(courseService.getCourse(id), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<CourseDto>> getCoursesForLoggedInUser() {
        Long Id = null;
        try {
            Party party = partyService.getPartyForCurrentUser();
            Id = party.getId();
        } catch (Exception e) {

        }
        return new ResponseEntity<>(courseService.getCoursesByTutor(Id), HttpStatus.OK);
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/discussions"})
    public ResponseEntity<List<DiscussionDto>> getDiscussions(@PathVariable("id") Long course_id) {
        try {
            return new ResponseEntity<>(messageService.getCourseDiscussions(course_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/announcements"})
    public ResponseEntity<List<DiscussionDto>> getAnnouncements(@PathVariable("id") Long course_id) {
        try {
            return new ResponseEntity<>(messageService.getCourseAnnouncements(course_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/files"})
    public ResponseEntity<List<FileInfo>> getFiles(@PathVariable("id") Long course_id) {
        try {
            return new ResponseEntity<>(fileSvc.getMetadataByCourse(course_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
