package com.api.tutorjoint.service;

import com.api.tutorjoint.dto.*;
import com.api.tutorjoint.exception.CourseNotFoundException;
import com.api.tutorjoint.exception.PartyNotFoundException;
import com.api.tutorjoint.model.*;
import com.api.tutorjoint.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TutorService {

    @Autowired
    private PartyService partyService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private EventService eventService;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CourseService courseService;

    public TutorDto getTutor(Long id) throws Exception {
        TutorDto tutorDto = null;
        try {
            Party party = partyService.getPartyById(id);

            Contact contact = contactRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Contact Record Not Found for id " + id));
            Tutor tutor = tutorRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Tutor Record Not Found for id " + id));

            //Set Image
            Long imageId = tutor.getImageId();
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new PartyNotFoundException("Party Avatar Not Found for id " + id));
            AvatarDto avatarDto = new AvatarDto();
            avatarDto.setName(image.getName());
            avatarDto.setType(image.getType());
            avatarDto.setBase64String(image.getBase64String());

            tutorDto = new TutorDto();
            tutorDto.setAvatar(avatarDto);

            //Set Party Information
            tutorDto.setTaxId(party.getTaxId());
            tutorDto.setFullName(party.getFullName());
            tutorDto.setLastName(party.getLastName());
            tutorDto.setMiddleName(party.getMiddleName());
            tutorDto.setFirstName(party.getFirstName());
            tutorDto.setEmailAddress(party.getEmailAddress());
            tutorDto.setInstructorId(party.getId());
            tutorDto.setDob(party.getDob());

            //Set tutor Information
            tutorDto.setEducation(tutor.getEducation());
            tutorDto.setEducationTxt(tutor.getEducationTxt());
            tutorDto.setOccupation(tutor.getOccupation());
            tutorDto.setOccupationTxt(tutor.getOccupationTxt());
            tutorDto.setExperience(tutor.getExperience());
            tutorDto.setExperienceTxt(tutor.getExperience());
            tutorDto.setDescription(tutor.getDescription());
            tutorDto.setPreference(tutor.getPreference());

            //Set Contact Information
            tutorDto.setHomePhone(contact.getHomePhone());
            tutorDto.setMobilePhone(contact.getMobilePhone());
            tutorDto.setStreetAddress1(contact.getStreetAddress1());
            tutorDto.setStreetAddress2(contact.getStreetAddress2());
            tutorDto.setCity(contact.getCity());
            tutorDto.setState(contact.getState());
            tutorDto.setZipCode(contact.getZipCode());
            tutorDto.setCountry(contact.getCountry());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to get Tutor with Id- " + id);
        }
        if (tutorDto == null) {
            throw new UsernameNotFoundException("Tutor not found with Id: " + id);
        } else {
            return tutorDto;
        }
    }

    public TutorDto getTutorByEmailId(String email) throws Exception {
        TutorDto tutorDto = null;
        try {
            Party party = partyService.getPartyByEmailId(email);
            Contact contact = contactRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Contact Record Not Found for email " + email));
            Tutor tutor = tutorRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found for email " + email));

            tutorDto = new TutorDto();

            //Set Party Information
            tutorDto.setTaxId(party.getTaxId());
            tutorDto.setFullName(party.getFullName());
            tutorDto.setLastName(party.getLastName());
            tutorDto.setMiddleName(party.getMiddleName());
            tutorDto.setFirstName(party.getFirstName());
            tutorDto.setEmailAddress(party.getEmailAddress());
            tutorDto.setInstructorId(party.getId());
            tutorDto.setDob(party.getDob());

            //Set Tutor Information
            tutorDto.setEducation(tutor.getEducation());
            tutorDto.setEducationTxt(tutor.getEducationTxt());
            tutorDto.setOccupation(tutor.getOccupation());
            tutorDto.setOccupationTxt(tutor.getOccupationTxt());
            tutorDto.setExperience(tutor.getExperience());
            tutorDto.setExperienceTxt(tutor.getExperienceTxt());
            tutorDto.setDescription(tutor.getDescription());
            tutorDto.setPreference(tutor.getPreference());

            //Set Contact Information
            tutorDto.setHomePhone(contact.getHomePhone());
            tutorDto.setMobilePhone(contact.getMobilePhone());
            tutorDto.setStreetAddress1(contact.getStreetAddress1());
            tutorDto.setStreetAddress2(contact.getStreetAddress2());
            tutorDto.setCity(contact.getCity());
            tutorDto.setState(contact.getState());
            tutorDto.setZipCode(contact.getZipCode());
            tutorDto.setCountry(contact.getCountry());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to get Tutor with emailId- " + email);
        }
        if (tutorDto == null) {
            throw new UsernameNotFoundException("Tutor not found with emailId: " + email);
        } else {
            return tutorDto;
        }
    }

    public List<TutorDto> getTutors(String partyType) throws Exception {
        List<TutorDto> tutorDtoList = new ArrayList<TutorDto>();
        try {
            List<Party> partyList = partyService.getAllParties(partyType);
            for (int i = 0; i < partyList.size(); i++) {
                Party party = partyList.get(i);

                Contact contact = contactRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Contact Record Not Found for Party " + party.getId()));
                Tutor tutor = tutorRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found for Party " + party.getId()));

                //Set Image
                Long imageId = tutor.getImageId();
                Image image = imageRepository.findById(imageId).orElseThrow(() -> new PartyNotFoundException("Tutor Avatar for id " + tutor.getImageId()));
                AvatarDto avatarDto = new AvatarDto();
                avatarDto.setName(image.getName());
                avatarDto.setType(image.getType());
                avatarDto.setBase64String(image.getBase64String());

                TutorDto tutorDto = new TutorDto();
                tutorDto.setAvatar(avatarDto);

                //Set Party Information
                tutorDto.setInstructorId(party.getId());
                tutorDto.setTaxId(party.getTaxId());
                tutorDto.setFullName(party.getFullName());
                tutorDto.setLastName(party.getLastName());
                tutorDto.setMiddleName(party.getMiddleName());
                tutorDto.setFirstName(party.getFirstName());
                tutorDto.setEmailAddress(party.getEmailAddress());
                tutorDto.setDob(party.getDob());

                //Set Tutor Information
                tutorDto.setEducation(tutor.getEducation());
                tutorDto.setEducationTxt(tutor.getEducationTxt());
                tutorDto.setOccupation(tutor.getOccupation());
                tutorDto.setOccupationTxt(tutor.getOccupationTxt());
                tutorDto.setExperience(tutor.getExperience());
                tutorDto.setExperienceTxt(tutor.getExperienceTxt());
                tutorDto.setDescription(tutor.getDescription());
                tutorDto.setPreference(tutor.getPreference());

                //Set Contact Information
                tutorDto.setHomePhone(contact.getHomePhone());
                tutorDto.setMobilePhone(contact.getMobilePhone());
                tutorDto.setStreetAddress1(contact.getStreetAddress1());
                tutorDto.setStreetAddress2(contact.getStreetAddress2());
                tutorDto.setCity(contact.getCity());
                tutorDto.setState(contact.getState());
                tutorDto.setZipCode(contact.getZipCode());
                tutorDto.setCountry(contact.getCountry());

                tutorDtoList.add(tutorDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error:  Unable to get Tutors");
        }
        if (tutorDtoList == null) {
            throw new UsernameNotFoundException("No Tutors Found");
        } else {
            return tutorDtoList;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long addTutor(TutorDto tutorDto) throws Exception {
        Party partyObj = null;

        //Get User
        User user = userService.getUserById(tutorDto.getInstructorId());

        //Create Party
        Party party = new Party();
        party.setUser(user);
        party.setPartyType("I");
        party.setTaxId(tutorDto.getTaxId());
        party.setFullName(tutorDto.getFullName());
        party.setLastName(tutorDto.getLastName());
        party.setMiddleName(tutorDto.getMiddleName());
        party.setFirstName(tutorDto.getFirstName());
        party.setEmailAddress(tutorDto.getEmailAddress());
        party.setDob(party.getDob());
        partyService.addParty(party);

        //Create Contact
        Contact contact = new Contact();
        contact.setHomePhone(tutorDto.getHomePhone());
        contact.setMobilePhone(tutorDto.getMobilePhone());
        contact.setStreetAddress1(tutorDto.getStreetAddress1());
        contact.setStreetAddress2(tutorDto.getStreetAddress2());
        contact.setCity(tutorDto.getCity());
        contact.setState(tutorDto.getState());
        contact.setZipCode(tutorDto.getZipCode());
        contact.setCountry(tutorDto.getCountry());
        contact.setEmailAddress(tutorDto.getEmailAddress());
        contact.setParty(party); //set party-to-contact relationship
        contactRepository.save(contact);

        //Create Avatar
        Tutor tutor = new Tutor();
        AvatarDto avatarDto = tutorDto.getAvatar();
        Image image = new Image();
        image.setBase64String(avatarDto.getBase64String());
        image.setName(avatarDto.getName());
        image.setCreatedOn(Instant.now());
        image.setUpdatedOn(Instant.now());
        image = imageRepository.save(image);

        //Create Tutor
        tutor.setImageId(image.getId());
        tutor.setEducation(tutorDto.getEducation());
        tutor.setEducationTxt(tutorDto.getEducationTxt());
        tutor.setOccupation(tutorDto.getOccupation());
        tutor.setOccupationTxt(tutorDto.getOccupationTxt());
        tutor.setExperience(tutorDto.getExperience());
        tutor.setExperienceTxt(tutorDto.getExperienceTxt());
        tutor.setDescription(tutorDto.getDescription());
        tutor.setPreference(tutorDto.getPreference());
        tutor.setParty(party);
        tutorRepository.save(tutor);
        roleService.addTutorRole(user);

        return party.getId();
    }

    public String updateTutor(TutorDto tutorDto) throws Exception {
        return null;
    }

    public boolean deleteTutor(Long id) throws Exception {
        return false;
    }

    public EventDto[] getEvents(Long tutor_id) {
        List<EventDto> lstEventDto = new ArrayList<EventDto>();

        List<CourseDto> courses = courseService.getCoursesByTutor(tutor_id);
        for (CourseDto course : courses
        ) {
            List<EventDto> lst = eventService.getEvents(course.getId());
            lstEventDto.addAll(lst);
        }
        EventDto arrEventDto[] = lstEventDto.toArray(new EventDto[lstEventDto.size()]);

        return arrEventDto;
    }
}
