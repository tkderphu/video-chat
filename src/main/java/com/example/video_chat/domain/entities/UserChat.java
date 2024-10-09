package com.example.video_chat.domain.entities;

import com.example.video_chat.common.SystemUtils;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
@DiscriminatorValue("USER_CHAT")
public class UserChat extends BaseChat{

    @ManyToOne
    @JoinColumn(name = "user_one_id")
    private User userOne;
    @ManyToOne
    @JoinColumn(name = "user_two_id")
    private User userTwo;

    public UserChat() {

    }

    public UserChat(User userOne, User userTwo) {
        this.userOne = userOne;
        this.userTwo = userTwo;
    }

    @Override
    public String getName() {
        if(SystemUtils.getUsername().compareTo(userOne.getEmail()) == 0) {
            return userTwo.getFirstName() + " " + userTwo.getLastName();
        }
        return userOne.getFirstName() + " " + userOne.getLastName();
    }

    @Override
    public String getAvatar() {
        if(SystemUtils.getUsername().compareTo(userOne.getEmail()) == 0) {
            return null; //userTwo.getImage;
        }
        return null; //userOne.getImage;
    }

    @Override
    public boolean isOnline() {
        if(SystemUtils.getUsername().compareTo(userOne.getEmail()) == 0) {
            return userTwo.isOnline();
        }
        return userOne.isOnline();
    }

    @Override
    public boolean isUserChat() {
        return true;
    }

    @Override
    public String chatPath() {
        String path = "/topic/private/message/user/";
        if(SystemUtils.getUsername().compareTo(userOne.getEmail()) == 0) {
            return path + userTwo.getId();
        }
        return path + userOne.getId();
    }
}
