package com.example.video_chat.domain.modelviews.request;

public class MessageDetailsRequest {
    private Long chatId;
    private boolean type;
    private int page;
    private int limit;

    public MessageDetailsRequest() {}

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

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
