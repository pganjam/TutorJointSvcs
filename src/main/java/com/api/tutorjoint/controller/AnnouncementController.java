package com.api.tutorjoint.controller;

import com.api.tutorjoint.dto.DiscussionDto;

import com.api.tutorjoint.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> createAnnouncement(@RequestBody DiscussionDto announcementDto) {
        long id = messageService.createAnnouncement(announcementDto);
        return ResponseEntity.ok(1);
    }

    @PatchMapping
    public ResponseEntity<?> updateAnnouncement(@RequestBody DiscussionDto announcementDto) {
        long id = messageService.updateAnnouncement(announcementDto);
        return ResponseEntity.ok(-1);
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.DELETE,
            path = {"/{id}"})
    public ResponseEntity deleteAnnouncement(@PathVariable @RequestBody Long id) {
        messageService.deleteAnnouncement(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
