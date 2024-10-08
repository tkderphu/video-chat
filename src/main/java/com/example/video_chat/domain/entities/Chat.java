package com.example.video_chat.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "chats")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public class Chat extends BaseEntity{


    public String getDisplayName() {
        throw new UnsupportedOperationException();
    }

    public String getImageRepresent() {
        throw new UnsupportedOperationException();
    }

    public boolean getStatus() {
        throw new UnsupportedOperationException();
    }

    public boolean getType() { //true if chat to user else false
        throw new UnsupportedOperationException();
    }

}
