package com.api.tutorjoint.service;

import com.api.tutorjoint.dto.*;
import com.api.tutorjoint.model.*;
import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.model.Contact;
import com.api.tutorjoint.repository.ContactRepository;
import com.api.tutorjoint.exception.PartyNotFoundException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private UserService userService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ContactRepository contactRepository;

    public EventDto[] getEvents(String student_id) {
        List<EventDto> lstEventDto = new ArrayList<EventDto>();

        CourseDto[] courses = enrollmentService.getEnrollments(student_id);
        for (CourseDto course : courses
        ) {
            List<EventDto> lst = eventService.getEvents(course.getId());
            lstEventDto.addAll(lst);
        }
        EventDto arrEventDto[] = lstEventDto.toArray(new EventDto[lstEventDto.size()]);

        return arrEventDto;
    }

    public StudentDto getStudent(Long id) throws Exception {
        StudentDto studentDto = null;

        try {
            Party party = partyService.getPartyById(id);
            Contact contact = contactRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Contact Record Not Found for id " + id));

            studentDto = new StudentDto();

            //Set Party Information
            studentDto.setFullName(party.getFullName());
            studentDto.setLastName(party.getLastName());
            studentDto.setMiddleName(party.getMiddleName());
            studentDto.setFirstName(party.getFirstName());
            studentDto.setEmailAddress(party.getEmailAddress());
            studentDto.setStudentId(party.getId());
            studentDto.setDob(party.getDob());
            studentDto.setGender(party.getGender());

            //Set Contact Information
            studentDto.setHomePhone(contact.getHomePhone());
            studentDto.setMobilePhone(contact.getMobilePhone());
            studentDto.setStreetAddress1(contact.getStreetAddress1());
            studentDto.setStreetAddress2(contact.getStreetAddress2());
            studentDto.setCity(contact.getCity());
            studentDto.setState(contact.getState());
            studentDto.setZipCode(contact.getZipCode());
            studentDto.setCountry(contact.getCountry());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to get Tutor with Id- " + id);
        }
        if (studentDto == null) {
            throw new UsernameNotFoundException("Tutor not found with Id: " + id);
        } else {
            return studentDto;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long registerStudent(StudentDto studentDto) throws Exception {
        Party partyObj = null;

        //Get User
        User user = userService.getUserById(studentDto.getStudentId());

        //Create Party
        Party party = new Party();
        party.setPartyType("S");
        party.setUser(user);
        party.setFullName(studentDto.getFullName());
        party.setLastName(studentDto.getLastName());
        party.setMiddleName(studentDto.getMiddleName());
        party.setFirstName(studentDto.getFirstName());
        party.setEmailAddress(studentDto.getEmailAddress());
        party.setDob(party.getDob());
        party.setGender(studentDto.getGender());
        Long partyId = partyService.addParty(party);

        //Create Contact
        Contact contact = new Contact();
        contact.setHomePhone(studentDto.getHomePhone());
        contact.setMobilePhone(studentDto.getMobilePhone());
        contact.setStreetAddress1(studentDto.getStreetAddress1());
        contact.setStreetAddress2(studentDto.getStreetAddress2());
        contact.setCity(studentDto.getCity());
        contact.setState(studentDto.getState());
        contact.setZipCode(studentDto.getZipCode());
        contact.setCountry(studentDto.getCountry());
        contact.setEmailAddress(studentDto.getEmailAddress());

        //set party-to-contact relationship
        contact.setParty(party);
        contactRepository.save(contact);
        roleService.addStudentRole(user);

        return partyId;
    }
}
