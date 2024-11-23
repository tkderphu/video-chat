package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.PinMessage;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PinMessageModelView {
    private Long id;
    private String owner;
    private Long conversationId;
    private LocalDateTime createdDate;
    private MessageModelViewSimple message;

    public PinMessageModelView(PinMessage pinMessage) {
        this.id = pinMessage.getId();
        this.owner = pinMessage.getOwner().getFullName();
        this.conversationId = pinMessage.getConversation().getId();
        this.createdDate = pinMessage.getCreatedDate();
        this.message = new MessageModelViewSimple(pinMessage.getMessage());
    }

    public Long getId() {
        return id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public MessageModelViewSimple getMessage() {
        return message;
    }

    public String getOwner() {
        return owner;
    }
}
