package com.example.video_chat.domain.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "messages")
public class Message extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    @OneToMany(cascade = CascadeType.ALL)
    private List<FileEntity> detachImages;
    @ManyToOne
    @JoinColumn(name = "to_conversation_id")
    private Conversation toConversation;
    private boolean saw;

    public Message(User fromUser,
                   String content,
                   MessageType messageType,
                   Conversation toConversation
                   ) {
        this.fromUser = fromUser;
        this.content = content;
        this.messageType = messageType;
        this.toConversation = toConversation;
    }

    public Message() {

    }

    public User getFromUser() {
        return fromUser;
    }

    public Conversation getToConversation() {
        return toConversation;
    }

    public String getContent() {
        return content;
    }

    public List<FileEntity> getDetachImages() {
        return detachImages;
    }


    public MessageType getMessageType() {
        return messageType;
    }


    public enum MessageType {
        VIDEO,
        TEXT
    }
}
