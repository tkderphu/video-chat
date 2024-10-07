package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUuid(String uuid);

    List<Token> findAllByUserId(Long userId);
}
