package com.example.video_chat.domain.entities;


import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "chat_type")
@Table(name = "chat")
public abstract class BaseChat  extends BaseEntity {

    public BaseChat() {
        super();
    }
    public abstract String getName();
    public abstract String getAvatar();
    public abstract boolean isOnline();
    public abstract boolean isUserChat();
}
