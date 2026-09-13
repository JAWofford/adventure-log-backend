package com.jwofford.adventure_log_backend.models;

import jakarta.persistence.*;

@Entity
@Table(name="route_leg")
public class RouteLeg {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long legId;

    //set up relationship many route legs belong to one trip
    @JoinColumn(name="trip_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TripLog tripLog;

    @Column(nullable = false)
    private int legOrder;

    @Column(nullable = false)
    private String legTitle;

    @Column(columnDefinition = "TEXT")
    private String legNotes;

    public RouteLeg() {
    }


    public long getLegId() {
        return legId;
    }

    public void setLegId(long legId) {
        this.legId = legId;
    }

    public TripLog getTripLog() {
        return tripLog;
    }

    public void setTripLog(TripLog tripLog) {
        this.tripLog = tripLog;
    }

    public int getLegOrder() {
        return legOrder;
    }

    public void setLegOrder(int legOrder) {
        this.legOrder = legOrder;
    }

    public String getLegTitle() {
        return legTitle;
    }

    public void setLegTitle(String legTitle) {
        this.legTitle = legTitle;
    }

    public String getLegNotes() {
        return legNotes;
    }

    public void setLegNotes(String legNotes) {
        this.legNotes = legNotes;
    }
}
