package com.api.tutorjoint.service;

import com.api.tutorjoint.dto.EnrollmentDto;
import com.api.tutorjoint.exception.CourseNotFoundException;
import com.api.tutorjoint.model.Course;
import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.repository.CourseRepository;
import com.api.tutorjoint.repository.EnrollmentRepository;
import com.api.tutorjoint.repository.PartyRepository;
import com.api.tutorjoint.dto.CourseDto;
import com.api.tutorjoint.dto.PostDto;
import com.api.tutorjoint.exception.PartyNotFoundException;
import com.api.tutorjoint.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private AuthService authService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;


    public PostDto getEnrollment(Long id) {
        return null;
    }


    @Transactional
    public void createEnrollments(EnrollmentDto[] enrollments) {

        for (EnrollmentDto element : enrollments) {
            Enrollment enroll = new Enrollment();
            Course course = courseRepository.findById(element.getCourse_id()).orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
            Party party = partyRepository.findById(Long.parseLong(element.getStudent_id())).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));
            enroll.setCourse(course);
            enroll.setParty(party);
            enroll.setPrice(35.00f);
            enroll.setCreatedOn(Instant.now());
            enroll.setUpdatedOn(Instant.now());

            enrollmentRepository.save(enroll);
        }
        ;
    }

    @Transactional
    public CourseDto[] getEnrollments(String party_id) {

        Party party = partyRepository.findById(Long.parseLong(party_id)).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));
        List<Enrollment> listEnrollment = enrollmentRepository.findByParty(party, Sort.by(Sort.Direction.ASC, "createdOn"));

        List<CourseDto> listCourseDto = new ArrayList<CourseDto>();
        for (Enrollment element : listEnrollment) {
            Course course = element.getCourse();
            CourseDto courseDto = courseService.getCourse(course.getId());
            listCourseDto.add(courseDto);
        }
        ;
        CourseDto arrCourseDto[] = listCourseDto.toArray(new CourseDto[listCourseDto.size()]);

        return arrCourseDto;
    }
}
