package com.jwofford.adventure_log_backend.dtos;

public class CampgroundReviewRequestDto {

    private String campgroundName;
    private String location;
    private String state;
    private String campgroundNotes;
    private String privacy;

    public CampgroundReviewRequestDto() {
    }

    public String getCampgroundName() {
        return campgroundName;
    }

    public void setCampgroundName(String campgroundName) {
        this.campgroundName = campgroundName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCampgroundNotes() {
        return campgroundNotes;
    }

    public void setCampgroundNotes(String campgroundNotes) {
        this.campgroundNotes = campgroundNotes;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
