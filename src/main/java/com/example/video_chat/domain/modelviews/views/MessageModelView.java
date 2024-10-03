package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.domain.entities.BaseEntity;
import com.example.video_chat.domain.entities.Message;

public class MessageModelView extends BaseEntity {
    private UserModelView fromUser;
    private ChatModelView chat;
    private String content;

    public MessageModelView(Message message) {
        this.fromUser = new UserModelView(message.getFromUser());
        this.chat = new ChatModelView(message.getChat());
        this.content = message.getContent();
    }
}
