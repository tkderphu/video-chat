package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.common.FileEntityConvert;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageModelView {
    private Long id;
    private LocalDateTime createdDate;
    private UserModelView fromUser;
    private ConversationModelView toConversation;
    private String content;
    private List<String> detachImages;
    private MessageType messageType;
    public MessageModelView() {

    }
    public MessageModelView(Message message) {
        this.id = message.getId();
        this.createdDate = message.getCreatedDate();
        this.fromUser = new UserModelView(message.getFromUser());
        this.toConversation = new ConversationModelView(message.getConversation());
        this.content = message.getContent();
        this.detachImages = FileEntityConvert.fileEntityToString(message.getDetachImages());
        this.createdDate = message.getCreatedDate();
        this.messageType = message.getMessageType();
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public UserModelView getFromUser() {
        return fromUser;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public ConversationModelView getToConversation() {
        return toConversation;
    }

    public String getContent() {
        return content;
    }

    public List<String> getDetachImages() {
        return detachImages;
    }

}
