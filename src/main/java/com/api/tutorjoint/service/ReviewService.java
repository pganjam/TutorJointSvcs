package com.api.tutorjoint.service;

import com.api.tutorjoint.dto.ReviewDto;
import com.api.tutorjoint.exception.ReviewNotFoundException;
import com.api.tutorjoint.model.Review;
import com.api.tutorjoint.repository.ReviewRepository;
import com.api.tutorjoint.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ReviewService {

    @Autowired
    private AuthService authService;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::mapFromReviewToDto).collect(toList());
    }

    public List<ReviewDto> getReviewsForTutor(String tutorId) {
        List<Review> reviews = reviewRepository.findByItemId(tutorId);
        return reviews.stream().map(this::mapFromReviewToDto).collect(toList());
    }

    public ReviewDto getReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException("For id " + id));
        return mapFromReviewToDto(review);
    }

    @Transactional
    public void createReview(ReviewDto reviewDto) {
        Review review = mapFromDtoToReview(reviewDto);
        reviewRepository.save(review);
    }

    private ReviewDto mapFromReviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(review.getId());
        reviewDto.setNotes(review.getNotes());
        reviewDto.setRating(review.getRating());
        reviewDto.setItemId(review.getItemId());
        reviewDto.setReviewerId(review.getReviewerId());
        reviewDto.setItemCategory(review.getItemCategory());

        return reviewDto;
    }

    private Review mapFromDtoToReview(ReviewDto reviewDto) {

        Review review = new Review();

        review.setCreatedOn(Instant.now());
        review.setUpdatedOn(Instant.now());
        review.setNotes(reviewDto.getNotes());
        review.setRating(reviewDto.getRating());
        review.setItemId(reviewDto.getItemId());
        review.setItemCategory(reviewDto.getItemCategory());

        UserDetailsImpl loggedInUser = authService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        review.setReviewerId(loggedInUser.getUsername());

        return review;
    }
}
