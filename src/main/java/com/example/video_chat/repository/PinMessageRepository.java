package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.PinMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinMessageRepository extends JpaRepository<PinMessage, Long> {
}
