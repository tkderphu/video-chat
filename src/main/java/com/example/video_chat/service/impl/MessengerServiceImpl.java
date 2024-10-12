package com.example.video_chat.service.impl;

import com.example.video_chat.common.SystemUtils;
import com.example.video_chat.domain.entities.Conversation;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.request.ConversationRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.repository.ConversationRepository;
import com.example.video_chat.repository.MessageRepository;
import com.example.video_chat.repository.UserRepository;
import com.example.video_chat.service.IMessengerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.video_chat.domain.entities.Conversation.ConversationType.PRIVATE;
import static com.example.video_chat.domain.entities.Conversation.ConversationType.PUBLIC;
import static com.example.video_chat.domain.entities.Message.MessageType.TEXT;
import static com.example.video_chat.domain.entities.Message.MessageType.VIDEO;

@Service
public class MessengerServiceImpl implements IMessengerService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessengerServiceImpl(UserRepository userRepository,
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
        User fromUser = getUser();
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
        ApiResponse<MessageModelView> response = new ApiResponse<>(
                "CREATE MESSAGE",
                200,
                0,
                new MessageModelView(message)
        );
        refreshMessageToConversation(conversation, response);
        return response;

    }

    private void refreshMessageToConversation(Conversation conversation,
                                              ApiResponse<MessageModelView> response) {
        this.simpMessagingTemplate.convertAndSend(
                "/topic/private/conversation/chat/message/" + conversation.getId(),
                response
        );
        this.simpMessagingTemplate.convertAndSend(
                "/topic/private/conversation/gallery/message/" + conversation.getId(),
                getEachMessageOfEveryConversation(1, 50)
        );
    }

    @Override
    public ApiResponse<?> createConversation(ConversationRequest request) {
        User fromUser = getUser();
        if (request == null || request.getUserIds().size() < 2) {
            throw new GeneralException("Size of group must greater than 2");
        }
        Set<User> members = request.getUserIds()
                .stream()
                .map(s -> userRepository.findById(s)
                        .orElseThrow(() -> new UsernameNotFoundException("Not found user")))
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
                        "------->%s created a group which was %s <-------",
                        fromUser.getFullName(),
                        request.getName()
                ),
                TEXT,
                conversation
        );
        this.messageRepository.save(message);

        ApiResponse<MessageModelView> response = new ApiResponse<>(
                "created group",
                200,
                0,
                new MessageModelView(message));
        refreshMessageToConversation(conversation, response);
        return response;
    }

    @Override
    public ApiListResponse<MessageModelView> getAllMessageOfConversation(
            Long conversationId,
            int numPage,
            int limit
    ) {
        User fromUser = getUser();
        Page<Message> page = this.messageRepository.findAllByToConversationId(
                conversationId,
                PageRequest.of(numPage - 1,
                        limit,
                        Sort.by("id").descending())
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
    public ApiListResponse<MessageModelView> getEachMessageOfEveryConversation(int pageNumber, int limit) {
        User fromUser = getUser();
        Page<Message> page = this.messageRepository.findAllMessage(
                fromUser.getId(),
                PageRequest.of(pageNumber - 1, limit)
        );
        return new ApiListResponse<>(
                "get all message of each user who you chatted recently",
                200,
                0,
                page.getTotalPages(), pageNumber, limit,
                page.getContent()
                        .stream()
                        .map(MessageModelView::new)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void establishVideoCall(String signal) {

    }

    private User getUser() {
        return userRepository
                .findByEmailIgnoreCase(SystemUtils.getUsername())
                .get();
    }
}

