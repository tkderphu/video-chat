package com.example.video_chat.domain.modelviews.request;

public class MessageRequest {
    private Long destId; //something when you haven't created chat yet then i will see destId is userId
    private String content;
    private boolean video;
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

    public void setContent(String content) {
        this.content = content;
    }
}
