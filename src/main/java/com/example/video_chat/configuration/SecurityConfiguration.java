package com.example.video_chat.configuration;

import com.example.video_chat.domain.entities.Token;
import com.example.video_chat.repository.PermissionRepository;
import com.example.video_chat.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final PermissionRepository permissionRepository;
    private final RoleChecker roleChecker;
    private final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final FilterRequest filterRequest;
    public SecurityConfiguration(PermissionRepository permissionRepository,
                                 RoleChecker roleChecker,
                                 FilterRequest filterRequest) {
        this.permissionRepository = permissionRepository;
        this.roleChecker = roleChecker;
        this.filterRequest = filterRequest;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/api/v1/messenger/messages",
                                    "/**")
                            .access((authentication, request) -> {
                                boolean check = roleChecker.check(authentication.get(), request.getRequest());
                                return new AuthorizationDecision(check);
                            });
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(excep -> {
                    excep.authenticationEntryPoint(new AuthenEntryPoint());
                })
                .addFilterBefore(filterRequest, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}

@Component
class FilterRequest extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;

    FilterRequest(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("UUID")) {
            String uuid = authorization.substring(5);
            Optional<Token> optionToken = tokenRepository.findByUuid(uuid);
            if (optionToken.isPresent()) {
                if (optionToken.get().getExpiredTime() >= System.currentTimeMillis()) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            optionToken.get().getUser().getUsername(),
                            null,
                            optionToken.get().getUser().getAuthorities()
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

class AuthenEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Un authorized");
    }
}
