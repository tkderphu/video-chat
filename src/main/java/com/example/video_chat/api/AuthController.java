package com.example.video_chat.api;

import com.example.video_chat.domain.modelviews.request.LoginRequest;
import com.example.video_chat.domain.modelviews.request.RegisterRequest;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.service.IAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/v1/auth/login")
    public ApiResponse<?> authenticate(LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/api/v1/auth/register")
    public ApiResponse<?> register(RegisterRequest request) {
        return this.authService.register(request);
    }

}
