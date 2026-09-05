package com.jwofford.adventure_log_backend.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="trip_log")
public class TripLog {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long tripId;

    //set up relationship many trips belong to one user
   @JoinColumn(name="user_id", nullable=false)
   @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String tripName;
    private String tripDescription;
    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private String privacy;

    //set up relationship one trip log can have many route legs
    @OneToMany(cascade = CascadeType.ALL, mappedBy="tripLog", orphanRemoval = true)
    private List<RouteLeg> routeLegList = new ArrayList<>();

    public TripLog() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<RouteLeg> getRouteLegList() {
        return routeLegList;
    }

    public void setRouteLegList(List<RouteLeg> routeLegList) {
        this.routeLegList = routeLegList;
    }
}
