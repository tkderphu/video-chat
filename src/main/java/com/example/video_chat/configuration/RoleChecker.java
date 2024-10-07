package com.example.video_chat.configuration;

import com.example.video_chat.repository.PermissionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
@Component
public class RoleChecker {
    private Supplier<Set<AntPathRequestMatcher>> supplier;
    private final PermissionRepository permissionRepository;
    private final Logger log = LoggerFactory.getLogger(RoleChecker.class);

    public RoleChecker(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean check(Authentication authentication, HttpServletRequest request) {
        log.info("Request: {}", request.getPathInfo());
        if(request.getMethod().compareTo("GET") == 0) {
            log.info("get request");
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("authorities = " + authorities);
        return true;
    }

}
