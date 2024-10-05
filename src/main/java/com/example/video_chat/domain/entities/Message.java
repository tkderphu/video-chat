package com.example.video_chat.domain.entities;

import com.example.video_chat.common.ValidationUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;
    private String content;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private boolean video;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public void setContent(String content) {
        ValidationUtils.checkField(content, 1);
        this.content = content;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public User getFromUser() {
        return fromUser;
    }

    public String getContent() {
        return content;
    }
}
