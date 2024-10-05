package com.example.video_chat.domain.entities;

import com.example.video_chat.common.ValidationUtils;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@DiscriminatorValue("USER")
public class User extends Chat implements UserDetails {

    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean online;

    @ManyToMany(mappedBy = "members")
    private Set<Group> groups;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        ValidationUtils.checkField(email, 12);
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String password) {
        ValidationUtils.checkField(password, 8);
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        ValidationUtils.checkField(firstName, 3);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        ValidationUtils.checkField(lastName, 3);
        this.lastName = lastName;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public String getDisplayName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String getImageRepresent() {
        return super.getImageRepresent();
    }

    @Override
    public boolean getStatus() {
        return this.online;
    }

    @Override
    public boolean getType() {
        return true;
    }
}
