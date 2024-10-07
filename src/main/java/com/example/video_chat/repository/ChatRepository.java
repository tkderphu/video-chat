package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
