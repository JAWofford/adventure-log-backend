package com.jwofford.adventure_log_backend.services;

import com.jwofford.adventure_log_backend.dtos.RouteLegRequestDto;
import com.jwofford.adventure_log_backend.dtos.RouteLegResponseDto;
import com.jwofford.adventure_log_backend.dtos.TripLogRequestDto;
import com.jwofford.adventure_log_backend.dtos.TripLogResponseDto;
import com.jwofford.adventure_log_backend.models.RouteLeg;
import com.jwofford.adventure_log_backend.models.TripLog;
import com.jwofford.adventure_log_backend.models.User;
import com.jwofford.adventure_log_backend.repositories.TripLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TripLogService {
    @Autowired
    private TripLogRepository tripLogRepository;

    public TripLogResponseDto createTripLog(TripLogRequestDto dto, User currentUser) {
        TripLog tripLog = new TripLog();
        tripLog.setUser(currentUser);
        tripLog.setTripName(dto.getTripName());
        tripLog.setTripDescription(dto.getTripDescription());
        tripLog.setStartDate(dto.getStartDate());
        tripLog.setEndDate(dto.getEndDate());
        tripLog.setPrivacy(dto.getPrivacy());

        TripLog saved = tripLogRepository.save(tripLog);
        return mapToResponseDto(saved);
    }

    public List<TripLogResponseDto> getAllTripLogsForUser(long userId) {
        List<TripLog> tripLogs = tripLogRepository.findByUserIdOrderByStartDateDesc(userId);

        // turning a List<TripLog> into a List<TripLogResponseDto>:
        // .stream() lets us process each element one at a time,
        // .map(...) applies mapToResponseDto to every TripLog in the list,
        // .collect(Collectors.toList()) gathers the results back into a List
        return tripLogs.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public TripLogResponseDto getTripLogById(long tripId, long currentUserId) {
        TripLog tripLog = tripLogRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip log not found"));

        checkOwnership(tripLog, currentUserId);

        return mapToResponseDto(tripLog);
    }

    public TripLogResponseDto updateTripLog(long tripId, TripLogRequestDto dto, long currentUserId) {
        TripLog tripLog = tripLogRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip log not found"));

        checkOwnership(tripLog, currentUserId);

        tripLog.setTripName(dto.getTripName());
        tripLog.setTripDescription(dto.getTripDescription());
        tripLog.setStartDate(dto.getStartDate());
        tripLog.setEndDate(dto.getEndDate());
        tripLog.setPrivacy(dto.getPrivacy());

        TripLog saved = tripLogRepository.save(tripLog);
        return mapToResponseDto(saved);
    }

    public void deleteTripLog(long tripId, long currentUserId) {
        TripLog tripLog = tripLogRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip log not found"));

        checkOwnership(tripLog, currentUserId);

        tripLogRepository.delete(tripLog);
        // cascade + orphanRemoval on routeLegList means this also
        // deletes every RouteLeg belonging to this trip — no separate call needed
    }

    public TripLogResponseDto addRouteLeg(long tripId, RouteLegRequestDto dto, long currentUserId) {
        TripLog tripLog = tripLogRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip log not found"));

        checkOwnership(tripLog, currentUserId);

        RouteLeg leg = new RouteLeg();
        leg.setTripLog(tripLog);
        leg.setLegTitle(dto.getLegTitle());
        leg.setLegNotes(dto.getLegNotes());
        // backend owns ordering: next leg's order = however many legs already exist, plus one
        leg.setLegOrder(tripLog.getRouteLegList().size() + 1);

        tripLog.getRouteLegList().add(leg);

        // saving the parent is enough — cascade = ALL persists the new RouteLeg too
        TripLog saved = tripLogRepository.save(tripLog);
        return mapToResponseDto(saved);
    }

    // shared helper: confirms the trip actually belongs to the requesting user
    private void checkOwnership(TripLog tripLog, long currentUserId) {
        if (tripLog.getUser().getId() != currentUserId) {
            throw new NoSuchElementException("Trip log not found");
        }
    }

    // shared helper: converts one TripLog entity into its response DTO,
    // including mapping its nested route legs
    private TripLogResponseDto mapToResponseDto(TripLog tripLog) {
        TripLogResponseDto dto = new TripLogResponseDto();
        dto.setTripId(tripLog.getTripId());
        dto.setTripName(tripLog.getTripName());
        dto.setTripDescription(tripLog.getTripDescription());
        dto.setStartDate(tripLog.getStartDate());
        dto.setEndDate(tripLog.getEndDate());
        dto.setPrivacy(tripLog.getPrivacy());

        List<RouteLegResponseDto> legDtos = tripLog.getRouteLegList().stream()
                .map(leg -> {
                    RouteLegResponseDto legDto = new RouteLegResponseDto();
                    legDto.setLegId(leg.getLegId());
                    legDto.setLegOrder(leg.getLegOrder());
                    legDto.setLegTitle(leg.getLegTitle());
                    legDto.setLegNotes(leg.getLegNotes());
                    return legDto;
                })
                .collect(Collectors.toList());

        dto.setRouteLegs(legDtos);
        return dto;
    }

}
