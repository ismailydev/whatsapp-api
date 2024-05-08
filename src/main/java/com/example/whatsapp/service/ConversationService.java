package com.example.whatsapp.service;

import com.example.whatsapp.model.Conversation;
import com.example.whatsapp.model.UserData;
import com.example.whatsapp.model.enums.ConversationType;
import com.example.whatsapp.repository.ConversationRepository;
import com.example.whatsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> createConversation(List<Long> users, ConversationType conversationType) {
        try {
            List<UserData> members = userRepository.findAllById(users);
            if (members.size() < 2) {
                throw new Exception("Minimum of two users required");
            }
            if (conversationType == ConversationType.PRIVATE) {
                if (members.size() != 2) {
                    throw new Exception("Private conversations should only contain two users");
                }
                List<Conversation> conversations = conversationRepository
                        .findByExactMembers(
                                members.get(0),
                                members.get(1),
                                conversationType,
                                PageRequest.of(0, 1));
                if (conversations.size() > 0) {
                    return ResponseEntity.status(200).body(conversations.get(0));
                }
            }
            Conversation conversation = Conversation
                    .builder()
                    .members(members)
                    .type(conversationType)
                    .build();
            // members.forEach((m)->{
            // List<Conversation> conversations =
            // Objects.requireNonNullElse(m.getConversations(),new ArrayList<>());
            // conversations.add(conversation);
            // m.setConversations(conversations);
            // });
            // userRepository.saveAll(members);

            return ResponseEntity.status(200).body(conversationRepository.save(conversation));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public ResponseEntity<?> getConversation(Long id) {
        Conversation conversation = conversationRepository.findById(id).orElse(null);
        if (Objects.nonNull(conversation)) {
            return ResponseEntity.ok(conversation);
        } else {
            return ResponseEntity.badRequest().body("Not found");
        }
    }

    public ResponseEntity<?> getConversationsByPhone(String phone, PageRequest pageRequest) {
        UserData user = userRepository.findFirstByPhone(phone).orElse(null);
        if (Objects.isNull(user)) {
            return ResponseEntity.badRequest().body("User Not found");
        }
        Page<Conversation> conversation = conversationRepository.findBySingleUser(user, pageRequest);
        return ResponseEntity.ok(conversation);
    }
}
