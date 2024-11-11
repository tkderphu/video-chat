package com.example.video_chat.domain.modelviews.response;

import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.views.UserModelView;

public class AuthResponse {
    private String uuid;
    private long expiredTime;
    private UserModelView info;

    public AuthResponse() {

    }

    public AuthResponse(String uuid, long expiredTime, User info) {
        this.uuid = uuid;
        this.expiredTime = expiredTime;
        this.info = new UserModelView(info);
    }


    public String getUuid() {
        return uuid;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public UserModelView getInfo() {
        return info;
    }
}
