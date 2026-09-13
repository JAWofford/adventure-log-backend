package com.jwofford.adventure_log_backend.dtos;

import java.time.LocalDate;

public class ReviewStayRequestDto {

    private LocalDate dateStayed;
    private String siteNumber;
    private String stayNotes;

    public ReviewStayRequestDto() {
    }

    public LocalDate getDateStayed() {
        return dateStayed;
    }

    public void setDateStayed(LocalDate dateStayed) {
        this.dateStayed = dateStayed;
    }

    public String getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(String siteNumber) {
        this.siteNumber = siteNumber;
    }

    public String getStayNotes() {
        return stayNotes;
    }

    public void setStayNotes(String stayNotes) {
        this.stayNotes = stayNotes;
    }
}

