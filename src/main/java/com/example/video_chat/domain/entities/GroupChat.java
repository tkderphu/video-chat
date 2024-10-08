package com.example.video_chat.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
@DiscriminatorValue("GROUP_CHAT")
public class GroupChat extends BaseChat{
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public GroupChat() {
        super();
    }

    public GroupChat(Group group) {
        this.group = group;
    }

    @Override
    public String getName() {
        return this.group.getName();
    }

    @Override
    public String getAvatar() {
        return null;
    }

    @Override
   public  boolean isOnline() {
        return this.group.getMembers()
                .stream()
                .anyMatch(s -> s.isOnline());
    }

    @Override
    public boolean isUserChat() {
        return false;
    }
}
