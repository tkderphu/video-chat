package com.example.video_chat.domain.modelviews.request;

public class MessageRequest {
    /**
     * At here destId is conversationId or userId
     * is User when conversation between two user isn't created
     * else ConversationRepository
     */
    private Long destId;
    private String content;
    private boolean video;
    private boolean user;
    public MessageRequest(Long destId, String content, boolean video) {
        this.destId = destId;
        this.content = content;
        this.video = video;
    }

    public MessageRequest() {

    }
    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Long getDestId() {
        return destId;
    }

    public void setDestId(Long destId) {
        this.destId = destId;
    }

    public String getContent() {
        return content;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public boolean isUser() {
        return user;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
