package com.example.video_chat.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private BaseChat chat;

    public Message(User fromUser, String content,
                   MessageType messageType, BaseChat chat) {
        this.fromUser = fromUser;
        this.content = content;
        this.messageType = messageType;
        this.chat = chat;
    }

    public Message() {

    }

    public User getFromUser() {
        return fromUser;
    }

    public BaseChat getChat() {
        return chat;
    }

    public String getContent() {
        return content;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
