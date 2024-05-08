package com.example.whatsapp.repository;

import com.example.whatsapp.model.Conversation;
import com.example.whatsapp.model.Message;
import com.example.whatsapp.model.UserData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "select m from Message m where" +
            " m.conversation = :conversation " +
            "and (m.sender <> :userData or not m.selfDelete)")
    Page<Message> getAllMessagesOfConv(Conversation conversation, UserData userData, Pageable pageable);

    Page<Message> getMessageByConversation(Conversation conversation, Pageable pageable);

}
