package com.jwofford.adventure_log_backend.dtos;

public class UserResponseDto {

    private long id;
    private String displayName;

    public UserResponseDto() {
    }

    public UserResponseDto(long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
