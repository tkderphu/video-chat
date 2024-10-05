package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.BaseEntity;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.modelviews.views.ChatModelView;
import com.example.video_chat.domain.modelviews.views.UserModelView;

public class MessageModelView extends BaseEntity {
    private UserModelView fromUser;
    private ChatModelView chat;
    private String content;

    public MessageModelView(Message message) {
        super(message.getId(), message.getCreatedBy(), message.getModifiedBy(),
                message.getCreatedDate(), message.getModifiedDate());
        this.fromUser = new UserModelView(message.getFromUser());
        this.chat = new ChatModelView(message.getChat());
        this.content = message.getContent();
    }
}
