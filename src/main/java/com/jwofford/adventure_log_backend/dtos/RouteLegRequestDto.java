package com.jwofford.adventure_log_backend.dtos;

public class RouteLegRequestDto {

        private String legTitle;
        private String legNotes;

    public RouteLegRequestDto() {
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
