package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.User;

public class UserModelView  {
    private String fullName;
    private long id;
    private String avatar;
    public UserModelView(User user) {
        this.fullName = user.getFullName();
        this.id = user.getId();
        this.avatar = user.getAvatar();
    }

    public UserModelView() {

    }

    public String getFullName() {
        return fullName;
    }

    public long getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }
}
