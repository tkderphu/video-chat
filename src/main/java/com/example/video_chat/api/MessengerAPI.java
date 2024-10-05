package com.example.video_chat.api;

import com.example.video_chat.domain.modelviews.request.GroupRequest;
import com.example.video_chat.domain.modelviews.request.GroupUpdateUserRequest;
import com.example.video_chat.domain.modelviews.request.MessageDetailsRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.service.IMessengerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            @RequestParam("files") List<MultipartFile> files
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
        return this.messengerService.getMessageGalleries(page, limit);
    }

    @GetMapping("/messages/details")
    public ApiListResponse<?> getMessageDetails(@RequestBody MessageDetailsRequest request){
        return this.messengerService.getMessageDetails(request);
    }
    @PostMapping("/groups")
    public ApiResponse<?> createConversation(@RequestBody GroupRequest groupRequest) {
        return this.messengerService.createConversation(groupRequest);
    }

    @PostMapping("/group/users")
    public ApiResponse<?> updateUserInConversation(@RequestBody GroupUpdateUserRequest request) {
        return this.messengerService.userManipulateWithGroup(request);
    }

}
