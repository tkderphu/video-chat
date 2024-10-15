package com.example.video_chat.service;

import com.example.video_chat.domain.modelviews.request.ConversationRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {
    ApiResponse<?> createMessage(MessageRequest request,
                                 List<MultipartFile> files);
    ApiListResponse<MessageModelView> getAllMessageOfConversation(
            Long conversationId,
            int page,
            int limit
    );
    void establishVideoCall(String signal);

}
