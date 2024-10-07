package com.example.video_chat.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Table(name = "roles")
@Entity
public class Role extends BaseEntity{
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @ManyToMany(mappedBy = "roles")
    private Set<Permission> permissions;

    public String getName() {
        return name;
    }
}
