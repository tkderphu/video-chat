package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {


    /*
        This method select all messages of a specific conversation
     */
    @Query(value = "select\n" +
            "    m.*\n" +
            "from chats c inner join messages m on c.id = m.chat_id\n" +
            "    inner join users u on m.from_user_id = u.id\n" +
            "where if(c.type = 'USER', (m.from_user_id = :fromId and m.chat_id = :chatId)\n" +
            "      or (m.from_user_id = :chatId and m.chat_id = :fromId),\n" +
            "      m.chat_id = :chatId)\n" +
            "and c.type = :type",
    nativeQuery = true)
    Page<Message> findAllMessage(
            @Param("fromId") Long fromId,
            @Param("chatId") Long chatId,
            @Param("type") String type,
            Pageable pageable
    );


    /*
        This method get a message of each conversation(user, group)
        that is chatted recently
     */
    @Query(value = "select m.*\n" +
            "from messages m\n" +
            "    inner join chats c on m.chat_id = c.id\n" +
            "where if(c.type = 'USER',\n" +
            "         (m.id in (select max(mx.id)\n" +
            "                   from messages mx\n" +
            "                   where mx.chat_id = :userId or mx.from_user_id = :userId\n" +
            "                   group by mx.chat_id + mx.from_user_id)),\n" +
            "         (m.id = (select max(mx.id)\n" +
            "                  from messages mx\n" +
            "                  where mx.chat_id = m.chat_id)\n" +
            "             and :userId in (select con.user_id\n" +
            "                      from conversation con\n" +
            "                      where con.group_id = c.id)))\n" +
            "order by m.created_date desc\n",
    nativeQuery = true)
    Page<Message> findAllMessage(@Param("userId") Long userId,
                                 Pageable pageable);

}
