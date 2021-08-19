package com.api.tutorjoint.controller;

import com.api.tutorjoint.dto.*;
import com.api.tutorjoint.model.FileInfo;
import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.service.*;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/tutors")
public class TutorController {

    @Autowired
    private PartyService partyService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private FileStorageService fileService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getTutors() {
        try {
            return ResponseEntity.ok(tutorService.getTutors("I"));
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}"})
    public ResponseEntity<?> getTutor(@PathVariable("id") Long Id) {
        try {
            TutorDto party = tutorService.getTutor(Id);
            return ResponseEntity.ok(party);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addTutor(@RequestBody TutorDto tutorDto) {
        try {
            Long Id = tutorService.addTutor(tutorDto);

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

    @RequestMapping(method = RequestMethod.DELETE,
            path = {"/{id}"})
    @PreAuthorize("hasRole('GUEST') or hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTutor(@PathVariable("id") Long id) {
        try {
            partyService.deleteParty(id);
            JSONObject jsonData = new JSONObject();

            jsonData.put("status", "SUCCESS");
            return ResponseEntity.ok(jsonData.toJSONString());
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('GUEST') or hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTutor(@RequestBody Party party) {
        try {
            String id = partyService.updateParty(party);

            JSONObject jsonData = new JSONObject();
            jsonData.put("id", party.getId());
            jsonData.put("status", "SUCCESS");
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
    @PreAuthorize("hasRole('GUEST') or hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getEvents(@PathVariable("id") Long id) {
        try {
            EventDto[] messages = tutorService.getEvents(id);

            return ResponseEntity.ok(messages);
        } catch (Exception exception) {
            return new ResponseEntity<>(
                    exception,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/office-hours"})
    public ResponseEntity<List<TutorSlotDto>> getOfficeHours(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(appointmentService.getOfficeHours(id, "HOUR30"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @RequestMapping(produces = "application/json",
            path = {"/{id}/office-hours"})
    public ResponseEntity updateOfficeHours(@RequestBody List<TutorSlotDto> tutorSlotDtoList, @PathVariable("id") Long id) {
        try {
            appointmentService.updateOfficeHours(id, tutorSlotDtoList);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/discussions"})
    public ResponseEntity<List<DiscussionDto>> getDiscussions(@PathVariable("id") Long tutor_id) {
        try {
            return new ResponseEntity<>(messageService.getTutorDiscussions(tutor_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/announcements"})
    public ResponseEntity<List<DiscussionDto>> getAnnouncements(@PathVariable("id") Long tutor_id) {
        try {
            return new ResponseEntity<>(messageService.getTutorAnnouncements(tutor_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/files"})
    public ResponseEntity<List<FileInfo>> getFiles(@PathVariable("id") Long tutor_id) {
        try {
            return new ResponseEntity<>(fileService.getMetadata(tutor_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}/categories"})
    public ResponseEntity<List<PartySubCategoryDto>> getCategories(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(categoryService.getCategories(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @RequestMapping(produces = "application/json",
            path = {"/{id}/categories"})
    public ResponseEntity updateCategories(@RequestBody List<PartySubCategoryDto> partySubCategoryDtoList, @PathVariable("id") Long id) {
        try {
            categoryService.updateCategories(id, partySubCategoryDtoList);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.POST,
            path = {"/{id}/appointments"})
    public ResponseEntity addAppointments(@RequestBody List<TutorSlotDto> appointments, @PathVariable("id") Long id) {
        AppointmentDto dto = null;
        appointmentService.addAppointments(id, appointments);
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(produces = "application/json",
            method = RequestMethod.POST,
            path = {"/{id}/availability"})
    public ResponseEntity<?> getAvailability(@RequestBody Instant date, @PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(appointmentService.getAvailability(id, date), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
