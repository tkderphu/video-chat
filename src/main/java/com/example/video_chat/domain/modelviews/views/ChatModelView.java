package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.BaseEntity;
import com.example.video_chat.domain.entities.Chat;

public class ChatModelView extends BaseEntity {
    private String nameDisplay;
    private String thumbnail;
    private boolean status;
    private boolean type; //true if it is user else false

    public ChatModelView(Chat chat) {
        super(chat.getId(), chat.getCreatedBy(), chat.getModifiedBy(),
                chat.getCreatedDate(), chat.getModifiedDate());
        this.nameDisplay = chat.getDisplayName();
        this.thumbnail = chat.getImageRepresent();
        this.status = chat.getStatus();
        this.type = chat.getType();
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isType() {
        return type;
    }
}
