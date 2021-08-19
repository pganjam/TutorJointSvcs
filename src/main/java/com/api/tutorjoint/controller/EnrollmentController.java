package com.api.tutorjoint.controller;

import com.api.tutorjoint.dto.EnrollmentDto;
import com.api.tutorjoint.service.EnrollmentService;
import com.api.tutorjoint.dto.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity createCourse(@RequestBody EnrollmentDto[] enrollments) {
        enrollmentService.createEnrollments(enrollments);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}"})
    public ResponseEntity<?> getEnrollments(@PathVariable("id") String Id) {
        try {
            CourseDto[] courses = enrollmentService.getEnrollments(Id);
            return ResponseEntity.ok(courses);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
