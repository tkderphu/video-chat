package com.example.video_chat.domain.modelviews.request;

import java.util.Set;

public class ConversationRequest {
    private Set<Long> userIds;
    private String name;

    public ConversationRequest(Set<Long> userIds, String name) {
        this.userIds = userIds;
        this.name = name;
    }


    public ConversationRequest() {}

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
