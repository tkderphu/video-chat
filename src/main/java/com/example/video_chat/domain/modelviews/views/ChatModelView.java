package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.BaseChat;
import com.example.video_chat.domain.entities.BaseEntity;
import com.example.video_chat.domain.entities.Chat;

public class ChatModelView extends BaseEntity {
    private String nameDisplay;
    private String thumbnail;
    private boolean status;
    private boolean type; //true if it is user else false

    public ChatModelView(BaseChat chat) {
        super(chat.getId(), chat.getCreatedBy(), chat.getModifiedBy(),
                chat.getCreatedDate(), chat.getModifiedDate());
        this.nameDisplay = chat.getName();
        this.thumbnail = chat.getAvatar();
        this.status = chat.isOnline();
        this.type = chat.isUserChat();
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
