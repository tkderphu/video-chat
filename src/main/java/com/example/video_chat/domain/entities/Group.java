package com.example.video_chat.domain.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group extends Chat{
    private String name;
    @ManyToMany
    @JoinTable(name = "conversation",
            joinColumns = @JoinColumn(name = "group_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members;


    public Group(String name, Set<User> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Set<User> getMembers() {
        return members;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String getImageRepresent() {
        return super.getImageRepresent();
    }
}
