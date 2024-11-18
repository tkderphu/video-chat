package com.example.video_chat.controller;

import com.example.video_chat.domain.modelviews.request.LoginRequest;
import com.example.video_chat.domain.modelviews.request.RegisterRequest;
import com.example.video_chat.domain.modelviews.response.ApiListResponse;
import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ApiResponse<?> loginIntoSystem(@RequestBody LoginRequest request) {
        return userService.authenticate(request);
    }

    @PostMapping("/auth/register")
    public ApiResponse<?> registerAccount(@RequestBody RegisterRequest request) {
        return this.userService.register(request);
    }


    @GetMapping
    public ApiListResponse<?> getAllUser(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "30") int limit
    ) {
        return this.userService.getAllUser(page, limit);
    }

    @PostMapping("/uploads/avatar")
    public ApiResponse<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return userService.uploadAvatar(file);
    }

}
