package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.Conversation;
import com.example.video_chat.domain.entities.Message;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationModelView {
    private Long id;
    private String displayName;
    private String imageRepresent;
    private boolean status;
    private Conversation.ConversationType scope;
    private MessageModelView recentMessage;

    public ConversationModelView(Conversation conversation) {
        if(conversation != null) {
            this.id = conversation.getId();
            this.displayName = conversation.displayName();
            this.imageRepresent = conversation.imageRepresent();
            this.status = conversation.status();
            this.scope = conversation.getConversationType();
        }
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

    public MessageModelView getRecentMessage() {
        return recentMessage;
    }

    public Conversation.ConversationType getScope() {
        return scope;
    }

    public void setRecentMessage(Message recentMessage) {
        if(recentMessage != null) {
            this.recentMessage = new MessageModelView(recentMessage);
        }
    }
}
