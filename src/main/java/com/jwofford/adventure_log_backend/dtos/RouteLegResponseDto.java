package com.jwofford.adventure_log_backend.dtos;

public class RouteLegResponseDto {
    private long legId;
    private int legOrder;
    private String legTitle;
    private String legNotes;

    public RouteLegResponseDto() {
    }

    public long getLegId() {
        return legId;
    }

    public void setLegId(long legId) {
        this.legId = legId;
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
