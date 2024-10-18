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

    @Query(value = "select c.*\n" +
            "from conversations c\n" +
            "where conversation_type = 'PRIVATE'\n" +
            "and c.id = (\n" +
            "    select cx.id\n" +
            "    from (\n" +
            "        select\n" +
            "            p.conversation_id as id,\n" +
            "            sum(p.user_id) as totalId\n" +
            "        from participates p\n" +
            "        group by p.conversation_id\n" +
            "     ) as cx\n" +
            "    where cx.totalId = :totalId\n" +
            ")\n", nativeQuery = true)
    Optional<Conversation> findPrivateConversation(
            @Param("totalId") Long totalId
    );
}
