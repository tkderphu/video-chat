package com.example.video_chat.domain.entities;

import jakarta.persistence.*;
import org.springframework.http.HttpMethod;

import java.util.Set;

@Table(name = "permissions")
@Entity
public class Permission extends BaseEntity {
    private String name;


    @ManyToMany
    @JoinTable(name = "privileges",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getName() {
        return name;
    }
}
