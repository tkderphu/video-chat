package com.example.video_chat.domain.entities;

import com.example.video_chat.common.SystemUtils;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "conversations")
public class Conversation extends BaseEntity {
    @Column(name = "name")
    private String name;

    private String thumbnail;

    @ManyToMany
    @JoinTable(name = "members",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    @Enumerated(EnumType.STRING)
    private ConversationType conversationType;

    public Conversation(String name,
                        String thumbnail,
                        Set<User> users,
                        ConversationType conversationType) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.users = users;
        this.conversationType = conversationType;
    }

    public Conversation(Set<User> users, ConversationType conversationType) {
        this.users = users;
        this.conversationType = conversationType;
    }

    public Conversation() {

    }

    public String displayName() {
        if(conversationType == ConversationType.PUBLIC) {
            return this.name;
        }
        return getUserPrivateConversation()
                .getFullName();
    }

    public boolean status() {
        return this.users
                .stream()
                .anyMatch(s -> s.isOnline());
    }

    public String imageRepresent() {
        if(conversationType == ConversationType.PRIVATE) {
            return getUserPrivateConversation()
                    .getAvatar();
        }
        return thumbnail;
    }

    private User getUserPrivateConversation() {
        return users.stream()
                .filter(u -> !u.getUsername().equals(SystemUtils.getUsername()))
                .findFirst()
                .get();
    }

    public enum ConversationType {
        PRIVATE,
        PUBLIC
    }


}
