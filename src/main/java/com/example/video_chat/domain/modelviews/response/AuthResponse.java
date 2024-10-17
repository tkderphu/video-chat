package com.example.video_chat.domain.modelviews.response;

public class AuthResponse {
    private Long id;
    private String uuid;
    private long expiredTime;
    private String fullName;

    public AuthResponse() {

    }

    public AuthResponse(Long id, String uuid, long expiredTime, String fullName) {
        this.id = id;
        this.uuid = uuid;
        this.expiredTime = expiredTime;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public String getFullName() {
        return fullName;
    }
}
