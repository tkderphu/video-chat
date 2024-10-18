package com.example.video_chat.service.impl;

import com.example.video_chat.common.SystemUtils;
import com.example.video_chat.domain.entities.Token;
import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.request.LoginRequest;
import com.example.video_chat.domain.modelviews.request.RegisterRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.response.AuthResponse;
import com.example.video_chat.domain.modelviews.views.UserModelView;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.repository.TokenRepository;
import com.example.video_chat.repository.UserRepository;
import com.example.video_chat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenRepository tokenRepository;
    @Value("${auth.token.expired-time}")
    private long expiredTime;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder,
                           TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public ApiResponse<AuthResponse> authenticate(LoginRequest request) {
        User user = this.userRepository
                .findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        if (!this.encoder.matches(request.getPassword(), user.getPassword())) {
            throw new GeneralException("Password which you type not match");
        }

        /**
         * Revoke all token previous
         */
        this.tokenRepository.findAllByUserId(user.getId()).forEach(token -> {
            token.setRevoked(true);
            this.tokenRepository.save(token);
        });

        Token token = new Token(UUID.randomUUID().toString(),
                System.currentTimeMillis() + expiredTime,
                user,
                false);

        this.tokenRepository.save(token);

        return new ApiResponse<>("User login successfully",
                200,
                0,
                new AuthResponse(user.getId(), token.getUuid(), token.getExpiredTime(), user.getFullName()));

    }

    @Override
    @Transactional
    public ApiResponse<?> register(RegisterRequest request) {
        User user = new User(
                request.getEmail(),
                this.encoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()
        );
        userRepository.save(user);
        return new ApiResponse<>("User register account successfully",
                200,
                0,
                null);
    }

    @Override
    public ApiListResponse<?> getAllUser(int page, int limit) {
        Page<User> userPage = this.userRepository
                .findAllByEmailIsNotContainingIgnoreCase(
                        SystemUtils.getUsername(),
                        PageRequest.of(page - 1, limit)
                );
        LOG.info(SystemUtils.getUsername() + " get all user");
        return new ApiListResponse<>(
                "get all user",
                200,
                0,
                userPage.getTotalPages(),
                page,
                limit,
                userPage.getContent()
                        .stream()
                        .map(UserModelView::new)
                        .collect(Collectors.toList())
        );
    }
}
