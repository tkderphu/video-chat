package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query(value = "select c.*\n" +
            "from conversations c\n" +
            "where c.conversation_type = 'PRIVATE'\n" +
            "  and exists (select 1\n" +
            "              from members m\n" +
            "              where m.conversation_id = c.id\n" +
            "                and m.user_id = :userId)",
            nativeQuery = true)
    Optional<Conversation> findByUserId(@Param("userId") Long userId);
}
