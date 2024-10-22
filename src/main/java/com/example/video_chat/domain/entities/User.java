package com.example.video_chat.domain.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails{

    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean online;

    private String avatar;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(mappedBy = "users")
    private Set<Conversation> conversations;

    public User(Long id) {
        super(id);
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public User() {

    }

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public boolean isOnline() {
        return online;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(s -> new SimpleGrantedAuthority("ROLE_"+ s.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User) {
            User user = (User) obj;
            if(this.email.compareTo(user.email) == 0) {
                return true;
            }
        }
        return false;
    }
}
