package com.example.video_chat.api;

import com.example.video_chat.domain.modelviews.request.ConversationRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.service.IMessengerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messenger")
@CrossOrigin("*")
public class MessengerAPI {

    private final IMessengerService messengerService;
    private final ObjectMapper objectMapper;
    public MessengerAPI(IMessengerService messengerService,
                        ObjectMapper objectMapper) {
        this.messengerService = messengerService;
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
                        new TypeReference<MessageRequest>() {}),
                files
        );
    }

    @GetMapping("/messages/galleries")
    public ApiListResponse<?> getMessageGalleries(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "15") int limit
    ) {
        return this.messengerService.getEachMessageOfEveryConversation(page, limit);
    }

    @GetMapping("/messages/conversations/{converId}")
    public ApiListResponse<?> getAllMessageOfConversation(
            @PathVariable("converId") Long converId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "50") int limit
    ){
        return this.messengerService.getAllMessageOfConversation(
                converId,
                page,
                limit
        );
    }
    @PostMapping("/conversations")
    public ApiResponse<?> createConversation(@RequestBody ConversationRequest conversationRequest) {
        return this.messengerService.createConversation(conversationRequest);
    }

    @MessageMapping("/video-call")
    public void establishVideoCall(@Payload String signal) {
        this.messengerService.establishVideoCall(signal);
    }

}
