package com.jwofford.adventure_log_backend.dtos;

import java.time.LocalDate;
import java.util.List;

public class TripLogResponseDto {
    private long tripId;
    private String tripName;
    private String tripDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private String privacy;
    private List<RouteLegResponseDto> routeLegs;

    public TripLogResponseDto() {
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public List<RouteLegResponseDto> getRouteLegs() {
        return routeLegs;
    }

    public void setRouteLegs(List<RouteLegResponseDto> routeLegs) {
        this.routeLegs = routeLegs;
    }
}
