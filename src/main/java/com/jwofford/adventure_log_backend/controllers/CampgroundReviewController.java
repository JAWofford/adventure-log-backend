package com.jwofford.adventure_log_backend.controllers;

import com.jwofford.adventure_log_backend.dtos.*;
import com.jwofford.adventure_log_backend.models.User;
import com.jwofford.adventure_log_backend.repositories.UserRepository;
import com.jwofford.adventure_log_backend.services.CampgroundReviewService;
import com.jwofford.adventure_log_backend.services.TripLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/campgroundreviews")
public class CampgroundReviewController {

    @Autowired
    private CampgroundReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    //use response entity to return 201 "created" status code.
    public ResponseEntity<CampgroundReviewResponseDto> createCampgroundReview(
            //get and deserialize incoming JSON into request DTO
            @RequestBody CampgroundReviewRequestDto dto,
            Authentication authentication) {
        //use helper method.
        User currentUser = getCurrentUser(authentication);
        //put together info to send back to front end.
        CampgroundReviewResponseDto createdReview = reviewService.createCampgroundReview(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @GetMapping("/user")
    public ResponseEntity<List<CampgroundReviewResponseDto>> getAllCampgroundReviewsForUser(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        List<CampgroundReviewResponseDto> reviews = reviewService.getAllCampgroundReviewsForUser(currentUser.getId());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{campgroundId}")
    public ResponseEntity<CampgroundReviewResponseDto> getReviewById(
            @PathVariable long campgroundId,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        CampgroundReviewResponseDto review= reviewService.getReviewById(campgroundId, currentUser.getId());
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{campgroundId}")
    public ResponseEntity<CampgroundReviewResponseDto> updateCampgroundReview(
            @PathVariable long campgroundId,
            @RequestBody CampgroundReviewRequestDto dto,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        CampgroundReviewResponseDto updated = reviewService.updateCampgroundReview(campgroundId, dto, currentUser.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{campgroundId}")
    public ResponseEntity<Void> deleteCampgroundReview(
            @PathVariable long campgroundId,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        reviewService.deleteCampgroundReview(campgroundId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{campgroundId}/stays")
    public ResponseEntity<CampgroundReviewResponseDto> addReviewStay(
            @PathVariable long campgroundId,
            @RequestBody ReviewStayRequestDto dto,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        CampgroundReviewResponseDto updated = reviewService.addReviewStay(campgroundId, dto, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    // helper method: get the full the adventure log User entity,
    // since Authentication only provide a username.
    private User getCurrentUser(Authentication authentication) {
        //get username string from authentication object
        String username = authentication.getName();

        return userRepository.findByUserName(username)
                //exception to handle Optional. This should never happen since user is authenticated
                //so let error bubble up.
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database"));
    }

}
