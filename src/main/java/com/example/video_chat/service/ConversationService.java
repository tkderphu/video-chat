package com.example.video_chat.service;

import com.example.video_chat.domain.modelviews.request.ConversationRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.ConversationModelView;

public interface ConversationService {
    ApiResponse<ConversationModelView> createConversation(
            ConversationRequest request
    );
    ApiListResponse<ConversationModelView> getAllConversationOfCurrentUser(
            int page,
            int limit
    );

    ApiResponse<ConversationModelView> findPrivateConversation(Long userId);

    ApiResponse<?> checkConversationContainsCurrentUser(Long conversationId);

    ApiResponse<?> removeUserInConversation(Long conversationId, Long userId);

    ApiResponse<?> deleteById(Long id);

    ApiResponse<?> inviteUserToConversation(Long conversationId, Long userId);
}
