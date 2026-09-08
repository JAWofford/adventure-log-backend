package com.jwofford.adventure_log_backend.services;

import com.jwofford.adventure_log_backend.dtos.RouteLegRequestDto;
import com.jwofford.adventure_log_backend.dtos.RouteLegResponseDto;
import com.jwofford.adventure_log_backend.dtos.TripLogRequestDto;
import com.jwofford.adventure_log_backend.dtos.TripLogResponseDto;
import com.jwofford.adventure_log_backend.exceptions.TripLogNotFoundException;
import com.jwofford.adventure_log_backend.models.RouteLeg;
import com.jwofford.adventure_log_backend.models.TripLog;
import com.jwofford.adventure_log_backend.models.User;
import com.jwofford.adventure_log_backend.repositories.TripLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripLogService {
    @Autowired
    private TripLogRepository tripLogRepository;

    public TripLogResponseDto createTripLog(TripLogRequestDto dto, User currentUser) {
        TripLog tripLog = new TripLog();
        //set user object so trip gets tied to the logged-in user
        tripLog.setUser(currentUser);
        tripLog.setTripName(dto.getTripName());
        tripLog.setTripDescription(dto.getTripDescription());
        tripLog.setStartDate(dto.getStartDate());
        tripLog.setEndDate(dto.getEndDate());
        tripLog.setPrivacy(dto.getPrivacy());

        TripLog savedLog = tripLogRepository.save(tripLog);
        //user helper method to return response DTO
        return mapToResponseDto(savedLog);
    }

    public List<TripLogResponseDto> getAllTripLogsForUser(long userId) {
        List<TripLog> tripLogs = tripLogRepository.findByUserIdOrderByStartDateDesc(userId);

        // turn a List<TripLog> into a List<TripLogResponseDto>:
        return tripLogs.stream()
                .map(tripLog -> mapToResponseDto(tripLog))
                .collect(Collectors.toList());
    }

    public TripLogResponseDto getTripLogById(long tripId, long currentUserId) {
        TripLog tripLog = tripLogRepository.findById(tripId)
                .orElseThrow(() -> new TripLogNotFoundException("Trip log not found"));

        checkOwnership(tripLog, currentUserId);

        return mapToResponseDto(tripLog);
    }

    public TripLogResponseDto updateTripLog(long tripId, TripLogRequestDto dto, long currentUserId) {
        TripLog tripLog = tripLogRepository.findById(tripId)
                .orElseThrow(() -> new TripLogNotFoundException("Trip log not found"));

        checkOwnership(tripLog, currentUserId);

        tripLog.setTripName(dto.getTripName());
        tripLog.setTripDescription(dto.getTripDescription());
        tripLog.setStartDate(dto.getStartDate());
        tripLog.setEndDate(dto.getEndDate());
        tripLog.setPrivacy(dto.getPrivacy());

        TripLog savedLog = tripLogRepository.save(tripLog);
        return mapToResponseDto(savedLog);
    }

    public void deleteTripLog(long tripId, long currentUserId) {
        TripLog tripLog = tripLogRepository.findById(tripId)
                .orElseThrow(() -> new TripLogNotFoundException("Trip log not found"));

        checkOwnership(tripLog, currentUserId);

        tripLogRepository.delete(tripLog);
        // cascade + orphanRemoval on routeLegList
        // deletes every RouteLeg belonging to this trip.
    }

    public TripLogResponseDto addRouteLeg(long tripId, RouteLegRequestDto dto, long currentUserId) {
        TripLog tripLog = tripLogRepository.findById(tripId)
                .orElseThrow(() -> new TripLogNotFoundException("Trip log not found"));

        checkOwnership(tripLog, currentUserId);

        RouteLeg leg = new RouteLeg();
        leg.setTripLog(tripLog);
        leg.setLegTitle(dto.getLegTitle());
        leg.setLegNotes(dto.getLegNotes());
        // backend owns ordering: next leg's order = however many legs already exist, plus one
        leg.setLegOrder(tripLog.getRouteLegList().size() + 1);

        tripLog.getRouteLegList().add(leg);

        // saving the parent is enough — cascade = ALL persists the new RouteLeg too
        TripLog savedLog = tripLogRepository.save(tripLog);
        return mapToResponseDto(savedLog);
    }

    // helper: confirm the trip actually belongs to the requesting user
    private void checkOwnership(TripLog tripLog, long currentUserId) {
        if (tripLog.getUser().getId() != currentUserId) {
            throw new TripLogNotFoundException("Trip log not found");
        }
    }

    // helper: convert one TripLog entity into its response DTO,
    // including mapping its nested route legs
    private TripLogResponseDto mapToResponseDto(TripLog tripLog) {
        TripLogResponseDto dto = new TripLogResponseDto();
        dto.setTripId(tripLog.getTripId());
        dto.setTripName(tripLog.getTripName());
        dto.setTripDescription(tripLog.getTripDescription());
        dto.setStartDate(tripLog.getStartDate());
        dto.setEndDate(tripLog.getEndDate());
        dto.setPrivacy(tripLog.getPrivacy());

        //attach route legs to trip log response.
        //convert list to stream so can call .map on it.
        //map through route legs and create dtos for each
        //collect back into list and add to the trip dto.
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
