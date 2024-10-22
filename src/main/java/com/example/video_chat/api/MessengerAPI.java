package com.example.video_chat.api;

import com.example.video_chat.domain.modelviews.request.ConversationRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.ConversationModelView;
import com.example.video_chat.service.ConversationService;
import com.example.video_chat.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messenger")
@CrossOrigin("*")
public class MessengerAPI {

    private final MessageService messengerService;
    private final ConversationService conversationService;
    private final ObjectMapper objectMapper;

    public MessengerAPI(MessageService messengerService,
                        ConversationService conversationService,
                        ObjectMapper objectMapper) {
        this.messengerService = messengerService;
        this.conversationService = conversationService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/messages")
    public ApiResponse<?> createMessage(
            @RequestParam("messageRequest") String messageRequest,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) throws JsonProcessingException {
        return messengerService.createMessage(
                this.objectMapper.readValue(
                        messageRequest,
                        new TypeReference<MessageRequest>() {
                        }),
                files
        );
    }

    @GetMapping("/conversations")
    public ApiListResponse<?> getAllConversationOfCurrentUser(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "50") int limit
    ) {
        return this.conversationService
                .getAllConversationOfCurrentUser(
                        page,
                        limit
                );
    }

    @GetMapping("/messages/conversations/{conversationId}")
    public ApiListResponse<?> getAllMessageOfConversation(
            @PathVariable("conversationId") Long conversationId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        return this.messengerService
                .getAllMessageOfConversation(
                        conversationId,
                        page,
                        limit
                );
    }

    @PostMapping("/conversations")
    public ApiResponse<?> createConversation(
            @RequestBody ConversationRequest conversationRequest
    ) {
        return this.conversationService
                .createConversation(
                        conversationRequest
                );
    }

    @MessageMapping("/signal")
    public void establishVideoCall(@Payload String signal) {
        this.messengerService.establishVideoCall(
                null,
                signal);
    }

    @PostMapping("/signal")
    public void videoCall(@RequestBody String signal) {
        this.messengerService.establishVideoCall(
                null,
                signal);
    }


    @GetMapping("/conversations/users/{userId}")
    public ApiResponse<ConversationModelView> getPrivateConversation(
            @PathVariable("userId") Long userId
    ) {
        return this.conversationService
                .findPrivateConversation(
                        userId
                );
    }

    @GetMapping("/conversations/{conversationId}/checkUser")
    public ApiResponse<?> checkWhetherConversationContainsCurrentUser(
            @PathVariable("conversationId") Long conversationId
    ) {
        return conversationService.checkConversationContainsCurrentUser(conversationId);
    }

}
