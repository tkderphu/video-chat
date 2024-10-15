package com.example.video_chat.service.impl;

import com.example.video_chat.common.SystemUtils;
import com.example.video_chat.domain.entities.Conversation;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.request.ConversationRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.ConversationModelView;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.repository.ConversationRepository;
import com.example.video_chat.repository.MessageRepository;
import com.example.video_chat.repository.UserRepository;
import com.example.video_chat.service.ConversationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.video_chat.domain.entities.Conversation.ConversationType.PUBLIC;
import static com.example.video_chat.domain.entities.Message.MessageType.TEXT;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConversationServiceImpl(UserRepository userRepository,
                                   ConversationRepository conversationRepository,
                                   MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public ApiResponse<ConversationModelView> createConversation(
            ConversationRequest request
    ) {
        User fromUser = userRepository
                .findByEmailIgnoreCase(SystemUtils.getUsername())
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
                        "------->%s created a group which was %s <-------",
                        fromUser.getFullName(),
                        request.getName()
                ),
                TEXT,
                conversation
        );
        this.messageRepository.save(message);
        return null;
    }

    @Override
    public ApiListResponse<ConversationModelView> getAllConversationOfCurrentUser(
            int page,
            int limit
    ) {
        Page<Conversation> pageConversation = this.conversationRepository
                .findAllByUserUsername(
                        SystemUtils.getUsername(),
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
                    .map(con -> {
                        ConversationModelView conv =
                                new ConversationModelView(con);
                        conv.setRecentMessage(
                                this
                                    .messageRepository
                                    .findLatestMessageByConversationId(con.getId())
                                    .get()
                        );
                        return conv;
                    })
                    .collect(Collectors.toList())
        );
    }
}
