package com.example.video_chat.domain.modelviews.response;

public class ApiResponse <T>{
    private String message;
    private int status;
    private int error;
    private T data;

    public ApiResponse(String message, int status, int error, T data) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.data = data;
    }


    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public int getError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
