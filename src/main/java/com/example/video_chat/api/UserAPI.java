package com.example.video_chat.api;

import com.example.video_chat.domain.modelviews.request.LoginRequest;
import com.example.video_chat.domain.modelviews.request.RegisterRequest;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.service.IAuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class UserAPI {

    private final IAuthService authService;

    public UserAPI(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public ApiResponse<?> loginIntoSystem(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/auth/register")
    public ApiResponse<?> registerAccount(@RequestBody RegisterRequest request) {
        return this.authService.register(request);
    }

}
