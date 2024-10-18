package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.common.FileEntityConvert;
import com.example.video_chat.domain.entities.Message;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageModelView {
    private long id;
    private Date createdDate;
    private UserModelView fromUser;
    private ConversationModelView toConversation;
    private String content;
    private List<String> detachImages;
    public MessageModelView(Message message) {
        this.id = message.getId();
        this.createdDate = message.getCreatedDate();
        this.fromUser = new UserModelView(message.getFromUser());
        this.toConversation = new ConversationModelView(message.getConversation());
        this.content = message.getContent();
        this.detachImages = FileEntityConvert.fileEntityToString(message.getDetachImages());
    }

    public long getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public UserModelView getFromUser() {
        return fromUser;
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
