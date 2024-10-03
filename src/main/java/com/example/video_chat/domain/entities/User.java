package com.example.video_chat.domain.entities;

import com.example.video_chat.common.ValidationUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends Chat{

    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        ValidationUtils.checkField(email, 12);
        this.email = email;
    }

    public String getPassword() {
        return password;
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

    @Override
    public String getDisplayName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String getImageRepresent() {
        return super.getImageRepresent();
    }
}
