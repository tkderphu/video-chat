package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
