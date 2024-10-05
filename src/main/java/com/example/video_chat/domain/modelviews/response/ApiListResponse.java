package com.example.video_chat.domain.modelviews.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiListResponse <T>{
    private String message;
    private Integer status;
    private Integer error;
    private Integer totalPage;
    private Integer page;
    private Integer limit;
    private List<T> data;

    public ApiListResponse(String message, Integer status, Integer error,
                           Integer totalPage, Integer page, Integer limit,
                           List<T> data) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.totalPage = totalPage;
        this.page = page;
        this.limit = limit;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getError() {
        return error;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public List<T> getData() {
        return data;
    }
}
