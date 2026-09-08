package com.jwofford.adventure_log_backend.controllers;

import com.jwofford.adventure_log_backend.dtos.RouteLegRequestDto;
import com.jwofford.adventure_log_backend.dtos.TripLogRequestDto;
import com.jwofford.adventure_log_backend.dtos.TripLogResponseDto;
import com.jwofford.adventure_log_backend.models.User;
import com.jwofford.adventure_log_backend.repositories.UserRepository;
import com.jwofford.adventure_log_backend.services.TripLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/triplogs")
public class TripLogController {

    private final TripLogService tripLogService;
    private final UserRepository userRepository;

    public TripLogController(TripLogService tripLogService, UserRepository userRepository) {
        this.tripLogService = tripLogService;
        this.userRepository = userRepository;
    }

    // shared helper: every endpoint needs the actual User entity,
    // but Authentication only gives us a username
    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database"));
    }

    @PostMapping
    public ResponseEntity<TripLogResponseDto> createTripLog(
            @RequestBody TripLogRequestDto dto,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        TripLogResponseDto created = tripLogService.createTripLog(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
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

}
