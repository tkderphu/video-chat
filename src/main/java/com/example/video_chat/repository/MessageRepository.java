package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findAllByConversationId(Long conversationId,
                                          Pageable pageable);

    @Query("select m from Message m " +
            "where m.id = (select max(mx.id) " +
            "from Message mx " +
            "where mx.conversation.id = :conversationId)")
    Optional<Message> findLatestMessageByConversationId(
            @Param("conversationId") Long conversationId
    );

    @Modifying
    void deleteAllByConversationId(Long id);
}
