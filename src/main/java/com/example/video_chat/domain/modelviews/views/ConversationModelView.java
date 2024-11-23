package com.example.video_chat.domain.modelviews.views;

import com.example.video_chat.common.SecurityUtils;
import com.example.video_chat.domain.entities.Conversation;
import com.example.video_chat.domain.entities.Message;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationModelView {
    private Long id;
    private String displayName;
    private String imageRepresent;
    private boolean status;
    private Conversation.ConversationType scope;
    private MessageModelView recentMessage;
    private Set<UserModelView> members;
    private List<PinMessageModelView> pinMessages;
    private boolean owner;
    public ConversationModelView(Conversation conversation) {
        if(conversation != null) {
            this.id = conversation.getId();
            this.displayName = conversation.displayName();
            this.imageRepresent = conversation.imageRepresent();
            this.status = conversation.status();
            this.scope = conversation.getConversationType();
            this.owner = conversation.getCreatedBy().compareTo(SecurityUtils.getUsername()) == 0;
            this.members = conversation.getUsers().stream().map(s -> new UserModelView(s)).collect(Collectors.toSet());
            this.pinMessages = conversation.getPinMessages().stream().map(PinMessageModelView::new).collect(Collectors.toList());
        }
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageRepresent() {
        return imageRepresent;
    }

    public boolean isStatus() {
        return status;
    }

    public MessageModelView getRecentMessage() {
        return recentMessage;
    }

    public Conversation.ConversationType getScope() {
        return scope;
    }

    public Set<UserModelView> getMembers() {
        return members;
    }

    public List<PinMessageModelView> getPinMessages() {
        return pinMessages;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setRecentMessage(Message recentMessage) {
        if(recentMessage != null) {
            this.recentMessage = new MessageModelView(recentMessage);
        }
    }
}
