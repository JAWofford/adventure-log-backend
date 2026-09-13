package com.jwofford.adventure_log_backend.controllers;

import com.jwofford.adventure_log_backend.dtos.RouteLegRequestDto;
import com.jwofford.adventure_log_backend.dtos.TripLogRequestDto;
import com.jwofford.adventure_log_backend.dtos.TripLogResponseDto;
import com.jwofford.adventure_log_backend.models.User;
import com.jwofford.adventure_log_backend.repositories.UserRepository;
import com.jwofford.adventure_log_backend.services.TripLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/triplogs")
public class TripLogController {

    @Autowired
    private TripLogService tripLogService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    //use response entity to return 201 "created" status code.
    public ResponseEntity<TripLogResponseDto> createTripLog(
            //get and deserialize incoming JSON into request DTO
            @RequestBody TripLogRequestDto dto,
            Authentication authentication) {
        //use helper method.
        User currentUser = getCurrentUser(authentication);
        //put together info to send back to front end.
        TripLogResponseDto createdLog = tripLogService.createTripLog(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
    }

    @GetMapping("/user")
    public ResponseEntity<List<TripLogResponseDto>> getAllTripLogsForUser(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        List<TripLogResponseDto> tripLogs = tripLogService.getAllTripLogsForUser(currentUser.getId());
        return ResponseEntity.ok(tripLogs);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripLogResponseDto> getTripLogById(
            @PathVariable long tripId,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        TripLogResponseDto tripLog = tripLogService.getTripLogById(tripId, currentUser.getId());
        return ResponseEntity.ok(tripLog);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<TripLogResponseDto> updateTripLog(
            @PathVariable long tripId,
            @RequestBody TripLogRequestDto dto,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        TripLogResponseDto updated = tripLogService.updateTripLog(tripId, dto, currentUser.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTripLog(
            @PathVariable long tripId,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        tripLogService.deleteTripLog(tripId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tripId}/legs")
    public ResponseEntity<TripLogResponseDto> addRouteLeg(
            @PathVariable long tripId,
            @RequestBody RouteLegRequestDto dto,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        TripLogResponseDto updated = tripLogService.addRouteLeg(tripId, dto, currentUser.getId());
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
