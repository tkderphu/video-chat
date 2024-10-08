package com.example.video_chat.domain.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "group_members")
public class Group extends BaseEntity {
    @Column(name = "name")
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

    public Group() {

    }


    public String getName() {
        return name;
    }

    public Set<User> getMembers() {
        return members;
    }
}
