package com.example.video_chat.service;

import com.example.video_chat.domain.modelviews.request.GroupRequest;
import com.example.video_chat.domain.modelviews.request.GroupUpdateUserRequest;
import com.example.video_chat.domain.modelviews.request.MessageDetailsRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMessengerService {
    /**
     * Create message when chat with user or group
     * @param request:
     * @return
     */
    ApiResponse<?> createMessage(MessageRequest request,
                                 List<MultipartFile> files);
    ApiResponse<?> createConversation(GroupRequest request);
    ApiListResponse<MessageModelView> getMessageDetails(
            MessageDetailsRequest request
    );
    ApiListResponse<MessageModelView> getMessageGalleries(
            int page,
            int limit
    );
    ApiResponse<?> userManipulateWithGroup(
            GroupUpdateUserRequest groupUpdateUserRequest
    );
}
