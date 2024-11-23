package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.common.FileEntityConvert;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.enums.MessageType;

import java.time.LocalDateTime;
import java.util.List;

public class MessageModelViewSimple {
    private Long id;
    private LocalDateTime createdDate;
    private UserModelView fromUser;
    private String content;
    private List<String> detachImages;
    private MessageType messageType;


    public MessageModelViewSimple(Message message) {
        this.id = message.getId();
        this.createdDate = message.getCreatedDate();
        this.fromUser = new UserModelView(message.getFromUser());
        this.content = message.getContent();
        this.detachImages = FileEntityConvert.fileEntityToString(message.getDetachImages());
        this.createdDate = message.getCreatedDate();
        this.messageType = message.getMessageType();
    }
    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public UserModelView getFromUser() {
        return fromUser;
    }

    public String getContent() {
        return content;
    }

    public List<String> getDetachImages() {
        return detachImages;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
