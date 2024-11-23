package com.example.video_chat.service;

import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.PinMessageModelView;

public interface PinMessageService {
    ApiResponse<?> createPinMessage(Long messageId);
    void delete(Long pinId);
}
