package com.example.video_chat.domain.modelviews.request;

public class MessageDetailsRequest {
    private Long chatId;
    private int page;
    private int limit;

    public MessageDetailsRequest() {}


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
