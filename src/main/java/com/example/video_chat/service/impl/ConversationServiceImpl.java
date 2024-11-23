package com.example.video_chat.service.impl;

import com.example.video_chat.common.SecurityUtils;
import com.example.video_chat.domain.entities.Conversation;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.request.ConversationRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.ConversationModelView;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.repository.ConversationRepository;
import com.example.video_chat.repository.MessageRepository;
import com.example.video_chat.repository.UserRepository;
import com.example.video_chat.service.ConversationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.video_chat.common.SecurityUtils.*;
import static com.example.video_chat.domain.entities.Conversation.ConversationType.PUBLIC;
import static com.example.video_chat.domain.enums.MessageType.TEXT;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ConversationServiceImpl(UserRepository userRepository,
                                   ConversationRepository conversationRepository,
                                   MessageRepository messageRepository,
                                   SimpMessagingTemplate simpMessagingTemplate) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public ApiResponse<ConversationModelView> createConversation(
            ConversationRequest request
    ) {
        User fromUser = userRepository
                .findByEmailIgnoreCase(getUsername())
                .get();
        if (request == null || request.getUserIds().size() < 2) {
            throw new GeneralException("Size of group must greater than 2");
        }
        Set<User> members = request.getUserIds()
                .stream()
                .map(s -> userRepository.findById(s)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Not found user"
                        )))
                .collect(Collectors.toSet());
        members.add(fromUser);

        Conversation conversation = new Conversation(
                request.getName(),
                null,
                members,
                PUBLIC
        );
        this.conversationRepository.save(conversation);
        Message message = new Message(fromUser,
                String.format(
                        "------->%s da tao nhom %s <-------",
                        fromUser.getFullName(),
                        request.getName()
                ),
                TEXT,
                conversation
        );
        this.messageRepository.save(message);

        ApiResponse<ConversationModelView> response = new ApiResponse<>(
                "created conversation",
                200,
                0,
                new ConversationModelView(conversation)
        );
        response.getData().setRecentMessage(message);
        conversation.getUsers()
                .forEach(user -> {
                    this.simpMessagingTemplate.convertAndSend(
                            "/topic/private/conversation/user/" + user.getId(),
                            response.getData()
                    );
                });
        return response;
    }

    @Override
    public ApiListResponse<ConversationModelView> getAllConversationOfCurrentUser(
            int page,
            int limit
    ) {
        Page<Conversation> pageConversation = this.conversationRepository
                .findAllByUserUsername(
                        getUsername(),
                        PageRequest.of(page - 1, limit)
                );
        return new ApiListResponse<>(
                "get all conversation",
                200,
                0,
                page,
                limit,
                pageConversation.getTotalPages(),
                pageConversation
                        .getContent()
                        .stream()
                        .map(conv -> {
                            var c = new ConversationModelView(conv);
                            c.setRecentMessage(messageRepository.findLatestMessageByConversationId(c.getId()).get());
                            return c;
                        })
                        .sorted((c1, c2) -> c2.getRecentMessage().getCreatedDate().compareTo(c1.getRecentMessage().getCreatedDate()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public ApiResponse<ConversationModelView> findPrivateConversation(Long userId) {
        User user = userRepository
                .findByEmailIgnoreCase(getUsername())
                .get();

        Conversation conversation = this.conversationRepository
                .findPrivateConversation(user.getId(), userId)
                .orElseThrow(() -> new GeneralException("Not found Conversation"));
        return new ApiResponse<>(
                "get private conversation",
                200,
                0,
                new ConversationModelView(conversation)
        );
    }

    @Override
    public ApiResponse<?> checkConversationContainsCurrentUser(Long conversationId) {
        User user = userRepository
                .findByEmailIgnoreCase(getUsername())
                .get();
        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(
                        () -> new GeneralException("Conversation not found")
                );
        ApiResponse<Object> apiResponse = new ApiResponse<>(
                "check conversation has contains user",
                200,
                0,
                null
        );
        if(conversation.getUsers().contains(user)) {
            apiResponse.setData(true);
        } else {
            apiResponse.setData(false);
        }
        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse<?> removeUserInConversation(Long conversationId, Long userId) {
        Conversation conversation = this.conversationRepository.findById(conversationId)
                .orElseThrow(() -> new GeneralException("not found"));
        if(conversation.getCreatedBy().compareTo(getUsername()) != 0) {
            throw new GeneralException("access denied");
        }
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException("not found user"));
        conversation.getUsers().remove(user);
        this.conversationRepository.save(conversation);
        sendMessage(conversation,  String.format("<<----------%s da duoi %s ra khoi nhom--------->>", getLoginUser().getFullName(), user.getFullName()));

        return new ApiResponse<>(
                "remove ok",
                200,
                0,
                null
        );
    }

    @Override
    @Transactional
    public ApiResponse<?> deleteById(Long id) {
        this.messageRepository.deleteAllByConversationId(id);
        this.conversationRepository.deleteById(id);
        return new ApiResponse<>("disband ok", 200, 0, null);
    }

    @Override
    public ApiResponse<?> inviteUserToConversation(Long conversationId, Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException("not found user"));
        Conversation conversation = this.conversationRepository.findById(conversationId)
                .orElseThrow(() -> new GeneralException("not found conversation"));
        conversation.getUsers().add(user);
        this.conversationRepository.save(conversation);
        sendMessage(conversation, String.format("<---%s da moi %s tham gia vao nhom--->", getLoginUser().getFullName(), user.getFullName()));
        return new ApiResponse<>("ok", 200, 0, null);
    }

    private void sendMessage(Conversation conversation, String text) {
        Message message = new Message(
                getLoginUser(),
                text,
                TEXT,
                conversation
        );
        this.messageRepository.save(message);

        conversation.getUsers()
                .forEach(u -> {
                    this.simpMessagingTemplate.convertAndSend(
                            "/topic/private/messages/conversation/user/" + u.getId(),
                            new MessageModelView(message)
                    );
                });
    }
}
