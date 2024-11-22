package com.example.video_chat.common;

import com.example.video_chat.domain.entities.User;
import com.example.video_chat.handler.exception.GeneralException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    public static User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken) {
            throw new GeneralException("You aren't login");
        }
        return (User) authentication.getPrincipal();
    }

    public static Long getLoginUserId() {
        return getLoginUser().getId();
    }
}
