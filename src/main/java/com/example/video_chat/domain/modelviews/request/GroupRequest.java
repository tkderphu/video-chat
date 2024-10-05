package com.example.video_chat.domain.modelviews.request;

import java.util.Set;

public class GroupRequest {
    private Set<Long> userIds;
    private String name;

    public GroupRequest() {}

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
