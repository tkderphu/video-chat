package com.example.video_chat.domain.modelviews.response;

public class AuthResponse {
    private Long id;
    private String uuid;
    private long expiredTime;

    public AuthResponse() {

    }

    public AuthResponse(Long id, String uuid, long expiredTime) {
        this.id = id;
        this.uuid = uuid;
        this.expiredTime = expiredTime;
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
}
