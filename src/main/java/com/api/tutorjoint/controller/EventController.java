package com.api.tutorjoint.controller;

import com.api.tutorjoint.dto.EventDto;
import com.api.tutorjoint.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDto dto) {
        long id = eventService.createEvent(dto);
        return ResponseEntity.ok(id);
    }

    @PatchMapping
    public ResponseEntity<?> updateEvent(@RequestBody EventDto dto) {
        long id = eventService.updateEvent(dto);
        return ResponseEntity.ok(id);
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.DELETE,
            path = {"/{id}"})
    public ResponseEntity deleteEvent(@PathVariable @RequestBody Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.GET,
            path = {"/{id}"})
    public ResponseEntity<List<EventDto>> getEvents(@PathVariable @RequestBody Long id) {
        return new ResponseEntity<>(eventService.getEvents(id), HttpStatus.OK);
    }
}
