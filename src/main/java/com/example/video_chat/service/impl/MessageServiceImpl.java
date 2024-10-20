package com.example.video_chat.service.impl;

import com.example.video_chat.common.SystemUtils;
import com.example.video_chat.domain.entities.Conversation;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.repository.ConversationRepository;
import com.example.video_chat.repository.MessageRepository;
import com.example.video_chat.repository.UserRepository;
import com.example.video_chat.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.video_chat.domain.entities.Conversation.ConversationType.PRIVATE;
import static com.example.video_chat.domain.entities.Message.MessageType.TEXT;
import static com.example.video_chat.domain.entities.Message.MessageType.VIDEO;

@Service
public class MessageServiceImpl implements MessageService {
    private final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageServiceImpl(UserRepository userRepository,
                              ConversationRepository conversationRepository,
                              MessageRepository messageRepository,
                              SimpMessagingTemplate simpMessagingTemplate) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @Override
    @Transactional
    public ApiResponse<?> createMessage(MessageRequest request,
                                        List<MultipartFile> files) {
        User fromUser = userRepository
                .findByEmailIgnoreCase(SystemUtils.getUsername())
                .get();
        Conversation conversation = this.conversationRepository
                .findById(request.getDestId())
                .orElse(null);


        if (conversation == null) {
            conversation = new Conversation(
                    new HashSet<>(Set.of(
                            fromUser,
                            this.userRepository
                                    .findById(request.getDestId())
                                    .orElseThrow(() -> new GeneralException("not found user id"))
                    )),
                    PRIVATE
            );
            this.conversationRepository.save(conversation);
        } else if (!conversation.getUsers().contains(fromUser)) {
            throw new GeneralException("you haven't added to group yet");
        }
        Message message = new Message(
                fromUser,
                request.getContent(),
                request.isVideo() ? VIDEO : TEXT,
                conversation
        );
        this.messageRepository.save(message);
        final ApiResponse<MessageModelView> response = new ApiResponse<>(
                "CREATE MESSAGE",
                200,
                0,
                new MessageModelView(message)
        );
        conversation.getUsers()
                .forEach(user -> {
                    this.simpMessagingTemplate.convertAndSend(
                            "/topic/private/messages/conversation/user/" + user.getId(),
                            response.getData()
                    );
                });
        return response;

    }


    @Override
    public ApiListResponse<MessageModelView> getAllMessageOfConversation(
            Long conversationId,
            int numPage,
            int limit
    ) {
        User fromUser = userRepository
                .findByEmailIgnoreCase(SystemUtils.getUsername())
                .get();
        Page<Message> page = this.messageRepository.findAllByConversationId(
                conversationId,
                PageRequest.of(numPage - 1, limit)
        );

        if (!CollectionUtils.isEmpty(fromUser.getConversations()) &&
                !fromUser.getConversations()
                        .stream()
                        .anyMatch(s -> s.getId().compareTo(conversationId) == 0)) {
            throw new GeneralException("You haven't participated group yet");
        }
        return new ApiListResponse<>(
                "get details message of specific chat",
                200,
                0,
                page.getTotalPages(),
                numPage,
                limit,
                page.getContent()
                        .stream()
                        .map(MessageModelView::new)
                        .collect(Collectors.toList())
        );


    }
    @Override
    public void establishVideoCall(Long conversationId, String signal) {
        this.simpMessagingTemplate.convertAndSend(
                "/topic/room",
                signal
        );
        this.LOGGER.info(signal);
    }
}

