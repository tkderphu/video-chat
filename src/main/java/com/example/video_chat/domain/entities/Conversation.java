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
    @JoinTable(name = "participates",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    @Enumerated(EnumType.STRING)
    private ConversationType conversationType;



    public Conversation(Long id) {
        super(id);
    }

    public Conversation(Set<User> users, ConversationType conversationType) {
        this.users = users;
        this.conversationType = conversationType;
    }



    public Conversation(String name,
                        Set<User> users,
                        ConversationType conversationType) {
        this(users, conversationType);
        this.name = name;
    }

    public Conversation(String name,
                        String thumbnail,
                        Set<User> users,
                        ConversationType conversationType) {
        this(name, users, conversationType);
        this.thumbnail = thumbnail;
    }


    public Conversation() {

    }

    public ConversationType getConversationType() {
        return conversationType;
    }

    public Set<User> getUsers() {
        return users;
    }

    public String displayName() {
        if (conversationType == ConversationType.PUBLIC) {
            return this.name;
        }
        return getUserPrivateConversation()
                .getFullName();
    }

    public boolean status() {
        if(conversationType == ConversationType.PUBLIC) {
            return this.users
                    .stream()
                    .anyMatch(s -> s.isOnline());
        }
        return getUserPrivateConversation().isOnline();
    }

    public String imageRepresent() {
        if (conversationType == ConversationType.PRIVATE) {
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


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Conversation) {
            Conversation conversation = (Conversation) obj;
            if(this.getId().compareTo(conversation.getId()) == 0)  {
                return true;
            }
        }
        return false;
    }
}
