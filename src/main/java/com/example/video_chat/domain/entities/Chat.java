package com.example.video_chat.domain.entities;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "chats")
@DiscriminatorColumn(name = "type")
public class Chat extends BaseEntity{

    public String getDisplayName() {
        throw new UnsupportedOperationException();
    }

    public String getImageRepresent() {
        throw new UnsupportedOperationException();
    }
}
