package com.api.tutorjoint.controller;

import com.api.tutorjoint.dto.DiscussionDto;
import com.api.tutorjoint.dto.EventDto;
import com.api.tutorjoint.dto.StudentDto;
import com.api.tutorjoint.model.FileInfo;
import com.api.tutorjoint.repository.RoleRepository;
import com.api.tutorjoint.service.*;
import com.api.tutorjoint.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private PartyService partyService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private FileStorageService fileSvc;

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}"})
    public ResponseEntity<?> getStudent(@PathVariable("id") Long Id) {
        try {
            StudentDto student = studentService.getStudent(Id);

            return ResponseEntity.ok(student);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> registerStudent(@RequestBody StudentDto studentDto) {
        try {
            Long Id = studentService.registerStudent(studentDto);

            JSONObject jsonData = new JSONObject();
            jsonData.put("status", "SUCCESS");
            jsonData.put("id", Id);
            return ResponseEntity.ok(jsonData.toJSONString());
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/events"})
    @PreAuthorize("hasRole('GUEST') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getEvents(@PathVariable("id") String id) {
        try {
            EventDto[] messages = studentService.getEvents(id);

            return ResponseEntity.ok(messages);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/discussions"})
    public ResponseEntity<List<DiscussionDto>> getDiscussions(@PathVariable("id") Long student_id) {
        try {
            return new ResponseEntity<>(messageService.getStudentDiscussions(student_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/announcements"})
    public ResponseEntity<List<DiscussionDto>> getAnnouncements(@PathVariable("id") Long student_id) {
        try {
            return new ResponseEntity<>(messageService.getStudentAnnouncements(student_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/files"})
    public ResponseEntity<List<FileInfo>> getFiles(@PathVariable("id") Long student_id) {
        try {
            return new ResponseEntity<>(fileSvc.getMetadata(student_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
