package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findAllByToConversationId(Long toConversationId,
                                            Pageable pageable);


    /*
        This method get a message of each conversation(user, group)
        that is chatted recently
     */
    @Query(value = "select m.*\n" +
            "from messages m\n" +
            "where m.id in (select max(mx.id)\n" +
            "               from messages mx\n" +
            "               where exists(select 1\n" +
            "                            from participates mem\n" +
            "                            where mem.user_id = :userId\n" +
            "                              and mem.conversation_id = mx.to_conversation_id)\n" +
            "               group by mx.to_conversation_id)\n" +
            "order by m.created_date",
    nativeQuery = true)
    Page<Message> findAllMessage(@Param("userId") Long userId,
                                 Pageable pageable);

}
