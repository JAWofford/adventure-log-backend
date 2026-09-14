package com.jwofford.adventure_log_backend.services;

import com.jwofford.adventure_log_backend.dtos.*;
import com.jwofford.adventure_log_backend.exceptions.CampgroundReviewNotFoundException;
import com.jwofford.adventure_log_backend.models.*;
import com.jwofford.adventure_log_backend.repositories.CampgroundReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampgroundReviewService {

    @Autowired
    private CampgroundReviewRepository campgroundReviewRepository;

    public CampgroundReviewResponseDto createCampgroundReview(CampgroundReviewRequestDto dto, User currentUser) {
        CampgroundReview review = new CampgroundReview();
        review.setUser(currentUser);
        review.setCampgroundName(dto.getCampgroundName());
        review.setLocation(dto.getLocation());
        review.setState(dto.getState());
        review.setCampgroundNotes(dto.getCampgroundNotes());
        review.setPrivacy(dto.getPrivacy());

        CampgroundReview savedReview = campgroundReviewRepository.save(review);

        return mapToResponseDto(savedReview);
    }

    public List<CampgroundReviewResponseDto> getAllCampgroundReviewsForUser(long userId) {
        List<CampgroundReview> reviews = campgroundReviewRepository.findByUserIdOrderByCampgroundNameAsc(userId);

        // turn a List<CampgroundReview> into a List<CampgroundReviewResponseDto>:
        return reviews.stream()
                .map(review -> mapToResponseDto(review))
                .collect(Collectors.toList());
    }

    public CampgroundReviewResponseDto getReviewById(long campgroundId, long currentUserId) {
        CampgroundReview review = campgroundReviewRepository.findById(campgroundId)
                .orElseThrow(() -> new CampgroundReviewNotFoundException("Campground review not found"));

        checkOwnership(review, currentUserId);

        return mapToResponseDto(review);
    }

    public CampgroundReviewResponseDto updateCampgroundReview(long campgroundId, CampgroundReviewRequestDto dto, long currentUserId) {
        CampgroundReview review = campgroundReviewRepository.findById(campgroundId)
                .orElseThrow(() -> new CampgroundReviewNotFoundException("Campground review not found"));

        checkOwnership(review, currentUserId);

        review.setCampgroundName(dto.getCampgroundName());
        review.setLocation(dto.getLocation());
        review.setState(dto.getState());
        review.setCampgroundNotes(dto.getCampgroundNotes());
        review.setPrivacy(dto.getPrivacy());

        CampgroundReview savedReview = campgroundReviewRepository.save(review);
        return mapToResponseDto(savedReview);
    }

    public void deleteCampgroundReview(long campgroundId, long currentUserId) {
        CampgroundReview review = campgroundReviewRepository.findById(campgroundId)
                .orElseThrow(() -> new CampgroundReviewNotFoundException("Campground review not found"));

        checkOwnership(review, currentUserId);

        campgroundReviewRepository.delete(review);
        // cascade + orphanRemoval on reviewStayList
        // deletes every ReviewStay belonging to this campground review.
    }

    public CampgroundReviewResponseDto addReviewStay(long campgroundId, ReviewStayRequestDto dto, long currentUserId) {
        CampgroundReview review = campgroundReviewRepository.findById(campgroundId)
                .orElseThrow(() -> new CampgroundReviewNotFoundException("Campground review not found"));

        checkOwnership(review, currentUserId);

        // ordered based on stayId.
        ReviewStay stay = new ReviewStay();
        stay.setCampgroundReview(review);
        stay.setDateStayed(dto.getDateStayed());
        stay.setSiteNumber(dto.getSiteNumber());
        stay.setStayNotes(dto.getStayNotes());

        review.getReviewStayList().add(stay);

        // saving the parent is enough cascade handles the new ReviewStay too
        CampgroundReview savedReview = campgroundReviewRepository.save(review);
        return mapToResponseDto(savedReview);
    }

    // helper: confirm the review actually belongs to the requesting user
    private void checkOwnership(CampgroundReview review, long currentUserId) {
        if (review.getUser().getId() != currentUserId) {
            throw new CampgroundReviewNotFoundException("Campground review not found");
        }
    }

    // helper: convert one CampgroundReview entity into its response DTO,
    // including mapping its nested stays.
    private CampgroundReviewResponseDto mapToResponseDto(CampgroundReview review) {
        CampgroundReviewResponseDto dto = new CampgroundReviewResponseDto();
        dto.setCampgroundId(review.getCampgroundId());
        dto.setCampgroundName(review.getCampgroundName());
        dto.setLocation(review.getLocation());
        dto.setState(review.getState());
        dto.setCampgroundNotes(review.getCampgroundNotes());
        dto.setPrivacy(review.getPrivacy());


        //attach stays to CampgroundReview response.
        //convert list to stream so can call .map on it.
        //map through stays and create dtos for each
        //collect back into list and add to the review dto.
        List<ReviewStayResponseDto> stayDtos = review.getReviewStayList().stream()
                .map(stay -> {
                    ReviewStayResponseDto stayDto = new ReviewStayResponseDto();
                    stayDto.setStayId(stay.getStayId());
                    stayDto.setDateStayed(stay.getDateStayed());
                    stayDto.setSiteNumber(stay.getSiteNumber());
                    stayDto.setStayNotes(stay.getStayNotes());
                    return stayDto;
                })
                .collect(Collectors.toList());

        dto.setReviewStays(stayDtos);
        return dto;
    }
}
