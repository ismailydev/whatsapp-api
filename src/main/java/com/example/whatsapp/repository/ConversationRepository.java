package com.example.whatsapp.repository;

import com.example.whatsapp.model.Conversation;
import com.example.whatsapp.model.UserData;
import com.example.whatsapp.model.enums.ConversationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query(value = "select c from Conversation c where" +
            "   c.type = :type and :firstMember member of c.members" +
            " and :secondMember member of c.members")
    List<Conversation> findByExactMembers(
            UserData firstMember, UserData secondMember,
            ConversationType type, Pageable pageable);

    @Query(value = "select c from Conversation c where" +
            "  :firstMember member of c.members")
    Page<Conversation> findBySingleUser(
            UserData firstMember, Pageable pageable);
}
