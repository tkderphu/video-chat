package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {


    /*
        This method select all messages of a specific conversation
     */
    Page<Message> findAllByChatId(
            Long chatId,
            Pageable pageable
    );


    /*
        This method get a message of each conversation(user, group)
        that is chatted recently
     */
    @Query(value = "select m.*\n" +
            "from messages m\n" +
            "         inner join (select max(mx.id)  as maxId,\n" +
            "                            c.chat_type as chatType\n" +
            "                     from messages mx\n" +
            "                              inner join chat c\n" +
            "                                         on mx.chat_id = c.id\n" +
            "                     group by mx.chat_id, c.chat_type) as info\n" +
            "                    on m.id = info.maxId\n" +
            "where if(info.chatType = 'USER_CHAT',\n" +
            "         exists(select 1\n" +
            "                from chat c\n" +
            "                where c.id = m.chat_id and\n" +
            "                      c.user_one_id = :userId\n" +
            "                   or c.user_two_id = :userId),\n" +
            "         :userId in (select c.user_id\n" +
            "                     from conversation c\n" +
            "                     where c.group_id = (select cx.group_id\n" +
            "                                         from chat cx\n" +
            "                                         where cx.id = m.chat_id)))\n" +
            "order by m.created_date desc\n",
    nativeQuery = true)
    Page<Message> findAllMessage(@Param("userId") Long userId,
                                 Pageable pageable);

}
