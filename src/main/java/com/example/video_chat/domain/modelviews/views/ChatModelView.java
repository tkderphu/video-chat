package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.BaseEntity;
import com.example.video_chat.domain.entities.Chat;

public class ChatModelView extends BaseEntity {
    private String nameDisplay;
    private String thumbnail;
    public ChatModelView(Chat chat) {
        this.nameDisplay = chat.getDisplayName();
        this.thumbnail = chat.getImageRepresent();
    }
}
