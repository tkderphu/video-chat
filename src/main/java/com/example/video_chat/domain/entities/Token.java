package com.example.video_chat.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "auth_tokens")
public class Token extends BaseEntity {
    private String uuid;
    private long expiredTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean revoked;


    public String getUuid() {
        return uuid;
    }


    public User getUser() {
        return user;
    }


    public long getExpiredTime() {
        return expiredTime;
    }

    public Token() {

    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Token(String uuid, long expiredTime, User user, boolean revoked) {
        this.uuid = uuid;
        this.expiredTime = expiredTime;
        this.user = user;
        this.revoked = revoked;
    }
}
