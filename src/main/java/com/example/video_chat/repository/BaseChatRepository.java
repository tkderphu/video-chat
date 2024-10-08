package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.BaseChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseChatRepository extends JpaRepository<BaseChat, Long> {
}
