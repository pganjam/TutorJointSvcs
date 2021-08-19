package com.api.tutorjoint.controller;

import com.api.tutorjoint.dto.ReviewDto;
import com.api.tutorjoint.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<ReviewDto>> createReview(@RequestBody ReviewDto reviewDto) {
        reviewService.createReview(reviewDto);
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ReviewDto>> getReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable @RequestBody Long id) {
        return new ResponseEntity<>(reviewService.getReview(id), HttpStatus.OK);
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForTutor(@PathVariable @RequestBody String tutorId) {
        return new ResponseEntity<>(reviewService.getReviewsForTutor(tutorId), HttpStatus.OK);
    }

}
