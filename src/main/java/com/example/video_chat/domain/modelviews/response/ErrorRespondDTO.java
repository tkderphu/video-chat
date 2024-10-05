package com.example.video_chat.domain.modelviews.response;

import java.util.ArrayList;
import java.util.List;

public class ErrorRespondDTO {
    private String error;
    List<String> details = new ArrayList<String>();
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public List<String> getDetails() {
        return details;
    }
    public void setDetails(List<String> details) {
        this.details = details;
    }
}
