package com.example.video_chat.service;

import com.example.video_chat.domain.modelviews.request.GroupRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.request.GroupUpdateUserRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;

public interface IMessengerService {
    ApiResponse<?> createMessage(MessageRequest request);
    ApiResponse<?> createConversation(GroupRequest request);
    ApiListResponse<MessageModelView> getMessageDetails();
    ApiListResponse<MessageModelView> getMessageGalleries();
    ApiResponse<?> userManipulateWithGroup(GroupUpdateUserRequest groupUpdateUserRequest);
}
