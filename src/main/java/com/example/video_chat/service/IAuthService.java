package com.example.video_chat.service;

import com.example.video_chat.domain.modelviews.request.LoginRequest;
import com.example.video_chat.domain.modelviews.request.RegisterRequest;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.domain.modelviews.response.AuthResponse;

public interface IAuthService {
    ApiResponse<AuthResponse> authenticate(LoginRequest request);
    ApiResponse<?> register(RegisterRequest request);
}
