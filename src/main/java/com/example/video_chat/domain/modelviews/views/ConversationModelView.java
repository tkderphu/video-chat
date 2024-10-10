package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.Conversation;

public class ConversationModelView {
    private long id;
    private String displayName;
    private String imageRepresent;
    private boolean status;

    public ConversationModelView(Conversation conversation) {
        this.id = conversation.getId();
        this.displayName = conversation.displayName();
        this.imageRepresent = conversation.imageRepresent();
        this.status = conversation.status();
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageRepresent() {
        return imageRepresent;
    }

    public boolean isStatus() {
        return status;
    }
}
