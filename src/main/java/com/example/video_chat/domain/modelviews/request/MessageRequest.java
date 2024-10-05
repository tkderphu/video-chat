package com.example.video_chat.domain.modelviews.request;

public class MessageRequest {
    private Long chatId;
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
