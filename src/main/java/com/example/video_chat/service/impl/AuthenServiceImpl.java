package com.example.video_chat.service.impl;

import com.example.video_chat.domain.entities.Chat;
import com.example.video_chat.domain.entities.Token;
import com.example.video_chat.domain.entities.User;
import com.example.video_chat.domain.modelviews.request.LoginRequest;
import com.example.video_chat.domain.modelviews.request.RegisterRequest;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.response.AuthResponse;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.repository.ChatRepository;
import com.example.video_chat.repository.TokenRepository;
import com.example.video_chat.repository.UserRepository;
import com.example.video_chat.service.IAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthenServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ChatRepository chatRepository;
    private final TokenRepository tokenRepository;
    @Value("${auth.token.expired-time}")
    private long expiredTime;

    public AuthenServiceImpl(UserRepository userRepository,
                             PasswordEncoder encoder,
                             ChatRepository chatRepository,
                             TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.chatRepository = chatRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public ApiResponse<AuthResponse> authenticate(LoginRequest request) {
        User user = this.userRepository
                .findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        if(!this.encoder.matches(request.getPassword(), user.getPassword())) {
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
                new AuthResponse(user.getId(), token.getUuid(), token.getExpiredTime()));

    }

    @Override
    public ApiResponse<?> register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(this.encoder.encode(request.getPassword()));

        userRepository.save(user);

        return new ApiResponse<>("User register account successfully",
                200,
                0,
                null);
    }
}
