package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
