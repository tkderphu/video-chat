package com.example.video_chat.service.impl;

import com.example.video_chat.common.SystemUtils;
import com.example.video_chat.domain.entities.Chat;
import com.example.video_chat.domain.entities.Group;
import com.example.video_chat.domain.entities.Message;
import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.request.GroupRequest;
import com.example.video_chat.domain.modelviews.request.GroupUpdateUserRequest;
import com.example.video_chat.domain.modelviews.request.MessageDetailsRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import com.example.video_chat.repository.ChatRepository;
import com.example.video_chat.repository.MessageRepository;
import com.example.video_chat.repository.UserRepository;
import com.example.video_chat.service.IMessengerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessengerServiceImpl implements IMessengerService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public MessengerServiceImpl(UserRepository userRepository,
                                ChatRepository chatRepository,
                                MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }


    @Override
    public ApiResponse<?> createMessage(MessageRequest request,
                                        List<MultipartFile> files) {
        User fromUser = getUser();
        Chat chat = this.chatRepository
                .findById(request.getChatId())
                .orElseThrow(() -> new RuntimeException("User who you wanted chat not exists"));

        Message message = new Message();
        message.setContent(request.getContent());
        message.setChat(chat);
        message.setFromUser(fromUser);
        message.setVideo(request.isVideo());
        this.messageRepository.save(message);
        return new ApiResponse<>("created message", 200, 0, new MessageModelView(message));
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


        Chat chat = new Group(request.getName(), members);

        this.chatRepository.save(chat);

        Message message = new Message();
        message.setContent(String.format(
                "------->%s created a group which was %s <-------",
                fromUser.getFirstName() + " " + fromUser.getLastName(),
                chat.getDisplayName()
        ));
        message.setChat(chat);

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
        Page<Message> page = this.messageRepository.findAllMessage(
                fromUser.getId(),
                request.getChatId(),
                request.isType() ? "USER" : "GROUP",
                PageRequest.of(request.getPage() - 1, request.getLimit())
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
        Group group = (Group) this.chatRepository.findById(request.getGroupId())
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

