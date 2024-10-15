package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {


    @Query(value = "select c.*\n" +
            "from conversations c\n" +
            "where c.id in (\n" +
            "    select \n" +
            "        p.conversation_id\n" +
            "    from participates p\n" +
            "    where p.user_id = (\n" +
            "        select u.id\n" +
            "        from users u\n" +
            "        where u.email = :username\n" +
            "    )\n" +
            ")", nativeQuery = true)
    Page<Conversation> findAllByUserUsername(
            @Param("username") String username,
            Pageable pageable
    );
}
