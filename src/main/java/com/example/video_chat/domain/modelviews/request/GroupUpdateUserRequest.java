package com.example.video_chat.domain.modelviews.request;

import java.util.Set;

public class GroupUpdateUserRequest {
    private Set<Long> userIds;
    private Long groupId;

    private boolean remove; //true -> delete user else add user

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
}
