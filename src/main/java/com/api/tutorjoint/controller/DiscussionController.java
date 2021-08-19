package com.api.tutorjoint.controller;

import com.api.tutorjoint.dto.DiscussionDto;
import com.api.tutorjoint.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discussions")
public class DiscussionController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(
            method = RequestMethod.POST,
            path = {"/{id}/comments"}
    )
    public ResponseEntity<?> createComment(@PathVariable @RequestBody Long id, @RequestBody DiscussionDto comment) {
        return ResponseEntity.ok(messageService.createComment(id, comment));
    }

    @PostMapping
    public ResponseEntity<?> createDiscussion(@RequestBody DiscussionDto discussionDto) {
        long id = messageService.createDiscussion(discussionDto);
        return ResponseEntity.ok(id);
    }

    @PatchMapping
    public ResponseEntity<?> updateDiscussion(@RequestBody DiscussionDto discussionDto) {
        long id = messageService.updateDiscussion(discussionDto);
        return ResponseEntity.ok(id);
    }

    @RequestMapping(produces = "application/json",
            method = RequestMethod.DELETE,
            path = {"/{id}"})
    public ResponseEntity deleteDiscussion(@PathVariable @RequestBody Long id) {
        messageService.deleteDiscussion(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
