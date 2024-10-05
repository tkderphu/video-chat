package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.BaseEntity;
import com.example.video_chat.domain.entities.User;

public class UserModelView extends BaseEntity {
    private String fullName;

    public UserModelView(User user) {
        this.fullName = user.getFirstName() + " " + user.getLastName();
    }
}
