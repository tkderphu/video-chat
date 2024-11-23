package com.example.video_chat.service.impl;

import com.example.video_chat.common.SecurityUtils;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.entities.PinMessage;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import com.example.video_chat.domain.modelviews.views.PinMessageModelView;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.repository.MessageRepository;
import com.example.video_chat.repository.PinMessageRepository;
import com.example.video_chat.service.PinMessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PinMessageServiceImpl implements PinMessageService {
    private final MessageRepository messageRepository;
    private final PinMessageRepository pinMessageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public PinMessageServiceImpl(MessageRepository messageRepository, PinMessageRepository pinMessageRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.messageRepository = messageRepository;
        this.pinMessageRepository = pinMessageRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public ApiResponse<?> createPinMessage(Long messageId) {
        Message message = this.messageRepository.findById(messageId)
                .orElseThrow(() -> new GeneralException("not found"));
        PinMessage pinMessage = new PinMessage();
        pinMessage.setConversation(message.getConversation());
        pinMessage.setMessage(message);
        pinMessage.setOwner(SecurityUtils.getLoginUser());
        this.pinMessageRepository.save(pinMessage);

        PinMessageModelView pinMessageModelView = new PinMessageModelView(pinMessage);

        pinMessage.getConversation().getUsers().forEach(user -> {
            this.simpMessagingTemplate.convertAndSend(
                    "/topic/private/messages/pin/conversation/user/" + user.getId(),
                    pinMessageModelView
            );
        });
        return null;
    }

    @Override
    public void delete(Long pinId) {
        PinMessage pinMessage = this.pinMessageRepository.findById(pinId)
                .orElseThrow(() -> new GeneralException("not found"));
        this.pinMessageRepository.delete(pinMessage);
        pinMessage.getConversation().getUsers().forEach(user -> {
            this.simpMessagingTemplate.convertAndSend(
                    "/topic/private/messages/pin/delete/conversation/user/" + user.getId(),
                    new PinMessageModelView(pinMessage)
            );
        });
    }
}
