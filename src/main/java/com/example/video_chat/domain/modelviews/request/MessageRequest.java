package com.example.video_chat.domain.modelviews.request;

public class MessageRequest {
    private Long chatId;
    private String content;
    public MessageRequest() {

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
