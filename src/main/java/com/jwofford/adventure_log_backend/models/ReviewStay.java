package com.jwofford.adventure_log_backend.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="review_stay")
public class ReviewStay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stayId;

    //set up relationship many stays for one campground review
    @JoinColumn(name="campground_id", nullable = false)
    @ManyToOne (fetch = FetchType.LAZY)
    private CampgroundReview campgroundReview;

    private LocalDate dateStayed;
    private String siteNumber;
    private String stayNotes;

    public ReviewStay() {
    }

    public CampgroundReview getCampgroundReview() {
        return campgroundReview;
    }

    public void setCampgroundReview(CampgroundReview campgroundReview) {
        this.campgroundReview = campgroundReview;
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
