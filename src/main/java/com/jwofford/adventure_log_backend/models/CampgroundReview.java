package com.jwofford.adventure_log_backend.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="campground_review")
public class CampgroundReview {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long campgroundId;

    //set up relationship many reviews belong to one user
    @JoinColumn(name="user_id", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String campgroundName;

    private String campgroundNotes;

    @Column(nullable = false)
    private String privacy;

    //set up relationship one review can have many stays
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campgroundReview", orphanRemoval = true)
    private List<ReviewStay> reviewStayList = new ArrayList<>();

    public CampgroundReview() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCampgroundName() {
        return campgroundName;
    }

    public void setCampgroundName(String campgroundName) {
        this.campgroundName = campgroundName;
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

    public List<ReviewStay> getReviewStayList() {
        return reviewStayList;
    }

    public void setReviewStayList(List<ReviewStay> reviewStayList) {
        this.reviewStayList = reviewStayList;
    }
}
