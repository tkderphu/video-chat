package com.example.video_chat.service.impl;

import com.example.video_chat.common.SystemUtils;
import com.example.video_chat.domain.entities.*;
import com.example.video_chat.domain.modelviews.request.GroupRequest;
import com.example.video_chat.domain.modelviews.request.GroupUpdateUserRequest;
import com.example.video_chat.domain.modelviews.request.MessageDetailsRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import com.example.video_chat.repository.*;
import com.example.video_chat.service.IMessengerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.video_chat.domain.entities.MessageType.TEXT;

@Service
public class MessengerServiceImpl implements IMessengerService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final BaseChatRepository baseChatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    public MessengerServiceImpl(UserRepository userRepository,
                                GroupRepository groupRepository,
                                MessageRepository messageRepository,
                                BaseChatRepository baseChatRepository,
                                SimpMessagingTemplate simpMessagingTemplate) {
        this.userRepository = userRepository;
        this.groupRepository= groupRepository;
        this.messageRepository = messageRepository;
        this.baseChatRepository = baseChatRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @Override
    @Transactional
    public ApiResponse<?> createMessage(MessageRequest request,
                                        List<MultipartFile> files) {
        User fromUser = getUser();
        BaseChat baseChat = this.baseChatRepository.findById(request.getDestId())
                .orElse(null);
        if(baseChat == null) {
            User anotherUser = this.userRepository.findById(request.getDestId())
                    .orElseThrow(() -> new UsernameNotFoundException("user who you want to chat not exist"));
            baseChat = new UserChat(fromUser, anotherUser);
            this.baseChatRepository.save(baseChat);
        }
        Message message = new Message(
                fromUser,
                request.getContent(),
                TEXT,
                baseChat
        );
        this.messageRepository.save(message);

        MessageModelView messageModelView = new MessageModelView(message);
        this.simpMessagingTemplate.convertAndSend(baseChat.chatPath(), messageModelView);
        return new ApiResponse<>("created message", 200, 0, messageModelView);
    }

    @Override
    public ApiResponse<?> createConversation(GroupRequest request) {
        User fromUser = getUser();
        if (request == null || request.getUserIds().size() < 2) {
            throw new UnsupportedOperationException("Size of group must greater than 1");
        }
        Set<User> members = request.getUserIds()
                .stream()
                .map(s -> userRepository.findById(s)
                        .orElseThrow(() -> new UsernameNotFoundException("Not found user")))
                .collect(Collectors.toSet());
        members.add(fromUser);


        BaseChat chat = new GroupChat(new Group(request.getName(), members));

        this.baseChatRepository.save(chat);

        Message message = new Message(
                fromUser,
                String.format(
                        "------->%s created a group which was %s <-------",
                        fromUser.getFirstName() + " " + fromUser.getLastName(),
                        request.getName()
                ),
                TEXT,
                chat
        );
        this.messageRepository.save(message);
        return new ApiResponse<>(
                "created group",
                200, 0,
                new MessageModelView(message));
    }

    @Override
    public ApiListResponse<MessageModelView> getMessageDetails(
            MessageDetailsRequest request
    ) {
        User fromUser = getUser();
        Page<Message> page = this.messageRepository.findAllByChatId(
                request.getChatId(),
                PageRequest.of(request.getPage() - 1,
                        request.getLimit(),
                        Sort.by("id").descending())
        );

        return new ApiListResponse<>(
                "get details message of specific chat",
                200,
                0,
                page.getTotalPages(),
                request.getPage(),
                request.getLimit(),
                page.getContent()
                        .stream()
                        .map(MessageModelView::new)
                        .collect(Collectors.toList())
        );


    }

    @Override
    public ApiListResponse<MessageModelView> getMessageGalleries(int pageNumber, int limit) {
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
    public ApiResponse<?> userManipulateWithGroup(
            GroupUpdateUserRequest request
    ) {
        Group group =  this.groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new UnsupportedOperationException());
        Set<User> users = request.getUserIds()
                .stream()
                .map(s -> this.userRepository.findById(s)
                        .orElseThrow(() -> new UnsupportedOperationException()))
                .collect(Collectors.toSet());

        if (request.isRemove()) {
            group.getMembers().removeAll(users);
        } else {
            group.getMembers().addAll(users);
        }
        return new ApiResponse<>("user manipulate group", 200, 0, null);
    }

    private User getUser() {
        return userRepository
                .findByEmailIgnoreCase(SystemUtils.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Not found user"));
    }
}

