package com.example.video_chat.api;

import com.example.video_chat.domain.entities.Conversation;
import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.request.ConversationRequest;
import com.example.video_chat.domain.modelviews.request.LoginRequest;
import com.example.video_chat.domain.modelviews.request.MessageRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.response.AuthResponse;
import com.example.video_chat.domain.modelviews.views.MessageModelView;
import com.example.video_chat.repository.ConversationRepository;
import com.example.video_chat.repository.MessageRepository;
import com.example.video_chat.repository.TokenRepository;
import com.example.video_chat.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class MessengerAPITest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private List<User> users;
    private List<Conversation> conversations;


    @BeforeEach
    void init() {
        users = new ArrayList<>(List.of(
                new User("quang0@gmail.com", passwordEncoder.encode("a"), null, null),
                new User("quang1@gmail.com", passwordEncoder.encode("a"), null, null),
                new User("quang2@gmail.com", passwordEncoder.encode("a"), null, null),
                new User("quang3@gmail.com", passwordEncoder.encode("a"), null, null),
                new User("quang4@gmail.com", passwordEncoder.encode("a"), null, null)
        ));

        users.forEach(s -> this.userRepository.save(s));


        conversations = new ArrayList<>(List.of(
                new Conversation("hello world",
                        new HashSet<>(Set.of(
                                users.get(0),
                                users.get(2),
                                users.get(3)
                        )),
                        Conversation.ConversationType.PUBLIC),
                new Conversation("hello world",
                        new HashSet<>(Set.of(
                                users.get(0),
                                users.get(1),
                                users.get(3)
                        )),
                        Conversation.ConversationType.PUBLIC),
                new Conversation("hello world",
                        new HashSet<>(Set.of(
                                users.get(0),
                                users.get(3),
                                users.get(4)
                        )),
                        Conversation.ConversationType.PUBLIC)
        ));

        conversations.forEach(s -> conversationRepository.save(s));

    }


    @AfterEach
    void destroy() {
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void createMessage() throws Exception {
        AuthResponse user0 = authenticate(new LoginRequest(this.users.get(0).getUsername(), "a"));
        MessageRequest request = new MessageRequest(
                this.users.get(1).getId(),
                "hello friends user 1",
                false
        );
        getFromRequest(user0.getUuid(), request);
    }

    MessageModelView getFromRequest(String token, MessageRequest request) throws Exception {
        String contentAsString = this.mockMvc.perform(MockMvcRequestBuilders.multipart(
                                HttpMethod.POST, "/api/v1/messenger/messages"
                        )
                        .param("messageRequest", this.objectMapper.writeValueAsString(request))
                        .header("Authorization", "UUID " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("CREATE MESSAGE"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return this.objectMapper
                .readValue(contentAsString, new TypeReference<ApiResponse<MessageModelView>>() {
                })
                .getData();
    }

    private AuthResponse authenticate(LoginRequest a) throws Exception {
        return this.objectMapper.readValue(
                this.mockMvc.perform(MockMvcRequestBuilders.post(
                                        "/api/v1/auth/login"
                                ).contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsBytes(a)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<ApiResponse<AuthResponse>>() {
                }
        ).getData();
    }

    @Test
    void getMessageGalleries() throws Exception {
        //login from user0
        List<AuthResponse> authResponses = this.users.stream().map(s -> {
                    try {
                        return authenticate(new LoginRequest(s.getUsername(), "a"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        List<MessageRequest> messageRequests = List.of(
                new MessageRequest(this.users.get(1).getId(), "hello", false), //0
                new MessageRequest(this.users.get(2).getId(), "hello", false), //1
                new MessageRequest(this.users.get(0).getId(), "hello", false), // 2
                new MessageRequest(this.conversations.get(0).getId(), "hello", false), //user: 0,2,3 //3
                new MessageRequest(this.conversations.get(2).getId(), "hello", false), //user: 0,1,3 //4
                new MessageRequest(this.users.get(4).getId(), "hello", false), //5
                new MessageRequest(this.users.get(1).getId(), "hello", false) //user: 0,3,4 //6
        );

        getFromRequest(authResponses.get(3).getUuid(), messageRequests.get(3));
        getFromRequest(authResponses.get(3).getUuid(), messageRequests.get(4));
        getFromRequest(authResponses.get(4).getUuid(), messageRequests.get(2));
        getFromRequest(authResponses.get(0).getUuid(), messageRequests.get(1));
        getFromRequest(authResponses.get(0).getUuid(), messageRequests.get(4));

        String contentAsString = this.mockMvc.perform(MockMvcRequestBuilders.get(
                        "/api/v1/messenger/messages/galleries"
                ).header("Authorization", "UUID " + authResponses.get(0).getUuid()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ApiListResponse<MessageModelView> apiResponse = this.objectMapper.readValue(
                contentAsString,
                new TypeReference<ApiListResponse<MessageModelView>>() {}
        );

        assertThat(apiResponse.getData().size()).isEqualTo(4);
    }

    @Test
    void getAllMessageOfConversation() throws Exception {
        List<AuthResponse> authResponses = this.users.stream().map(s -> {
                    try {
                        return authenticate(new LoginRequest(s.getUsername(), "a"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        MessageRequest messageModelView = new MessageRequest(
                this.conversations.get(0).getId(),
                "test",
                false
        );

        getFromRequest(authResponses.get(0).getUuid(), messageModelView);
        getFromRequest(authResponses.get(2).getUuid(), messageModelView);
        getFromRequest(authResponses.get(3).getUuid(), messageModelView);
        getFromRequest(authResponses.get(0).getUuid(), messageModelView);
        getFromRequest(authResponses.get(2).getUuid(), messageModelView);
        getFromRequest(authResponses.get(3).getUuid(), messageModelView);
        getFromRequest(authResponses.get(0).getUuid(), messageModelView);
        getFromRequest(authResponses.get(2).getUuid(), messageModelView);
        getFromRequest(authResponses.get(3).getUuid(), messageModelView);
        getFromRequest(authResponses.get(0).getUuid(), messageModelView);
        getFromRequest(authResponses.get(2).getUuid(), messageModelView);
        getFromRequest(authResponses.get(3).getUuid(), messageModelView);

        String contentAsString = this.mockMvc.perform(MockMvcRequestBuilders.get(
                        "/api/v1/messenger/messages/conversations/" + this.conversations.get(0).getId()
                ).header("Authorization", "UUID " + authResponses.get(0).getUuid()))
//                ).header("Authorization", "UUID " + authResponses.get(1).getUuid()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ApiListResponse<MessageModelView> response = this.objectMapper.readValue(
                contentAsString,
                new TypeReference<ApiListResponse<MessageModelView>>() {}
        );
        assertThat(response.getMessage()).isEqualTo("get details message of specific chat");
        assertThat(response.getData().size()).isEqualTo(12);
//        assertThat(response.getMessage()).isEqualTo("You haven't participated group yet");
//        assertThat(response.getData()).isEqualTo(null);

    }

    @Test
    void createConversation() throws Exception {
        ConversationRequest conversationRequest = new ConversationRequest(
                new HashSet<>(Set.of(
                        this.users.get(0).getId(),
                        this.users.get(1).getId(),
                        this.users.get(2).getId()
                )),
                "hello world"
        );
        AuthResponse authResponse = authenticate(
                new LoginRequest(this.users.get(0).getUsername(), "a")
        );
        this.mockMvc.perform(MockMvcRequestBuilders.post(
                "/api/v1/messenger/conversations"
        ).contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(conversationRequest))
                .header("Authorization", "UUID " + authResponse.getUuid()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("created group"));

    }
}
