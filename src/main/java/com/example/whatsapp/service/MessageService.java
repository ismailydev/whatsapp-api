package com.example.whatsapp.service;

import com.example.whatsapp.model.*;
import com.example.whatsapp.model.dto.MessageDto;
import com.example.whatsapp.model.dto.MessageEditDto;
import com.example.whatsapp.model.enums.MessageResponse;
import com.example.whatsapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final SeenAtRepository seenAtRepository;
    private final ReactionRepository reactionRepository;
    private final Utils utils;

    public ResponseEntity<?> createMessage(MessageDto messageDto) {
        try {
            Conversation conversationOptional;
            UserData userDataOptional;
            if (messageDto.getConversationId() != null && messageDto.getSenderId() != null) {
                conversationOptional = conversationRepository.findById(messageDto.getConversationId()).orElse(null);
                userDataOptional = userRepository.findById(messageDto.getSenderId()).orElse(null);
                if (conversationOptional != null && userDataOptional != null) {
                    Message message = Message
                            .builder()
                            .sender(userDataOptional)
                            .text(messageDto.getText())
                            .conversation(conversationOptional)
                            .build();
                    if (messageDto.getAttachment() != null) {
                        List<Media> mediaList = new ArrayList<>();
                        messageDto.getAttachment().forEach(a -> {
                            mediaList.add(utils.saveFile(a));
                        });
                        message.setAttachment(mediaList);
                    }
                    message = messageRepository.save(message);
                    conversationOptional.setLastMessage(message);
                    conversationRepository.save(conversationOptional);
                    return ResponseEntity.status(200).body(message);
                } else {
                    throw new Exception("User/conversation not found");
                }
            } else {
                throw new Exception("User id and conversation id required");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public ResponseEntity<?> openMessage(Long messageId, Long userId) {
        try {
            Message messageOptional;
            UserData userDataOptional;
            if (messageId != null && userId != null) {
                messageOptional = getMessage(messageId);
                userDataOptional = userRepository.findById(userId).orElse(null);
                if (messageOptional != null && userDataOptional != null) {
                    List<SeenAt> seen = messageOptional.getSeen();
                    SeenAt seenAt = SeenAt.builder().user(userDataOptional).build();
                    seen.add(seenAtRepository.save(seenAt));
                    messageOptional.setSeen(seen);
                    return ResponseEntity.status(200).body(messageRepository.save(messageOptional));
                } else {
                    throw new Exception("User/message not found");
                }
            } else {
                throw new Exception("User id and message id required");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public ResponseEntity<?> editMessage(Long messageId, MessageEditDto messageEditDto) {
        try {
            Message messageOptional;
            if (messageId != null) {
                messageOptional = getMessage(messageId);
                if (messageOptional != null) {
                    messageOptional.setText(messageEditDto.getText());
                    if (!messageEditDto.getAttachment().isEmpty()) {
                        List<Media> mediaList = new ArrayList<>();
                        messageEditDto.getAttachment().forEach(a -> mediaList.add(utils.saveFile(a)));
                        if (!messageOptional.getAttachment().isEmpty()) {
                            messageOptional.getAttachment().forEach(a -> utils.removeFile(a.getLocation()));
                        }
                        messageOptional.setAttachment(mediaList);
                    }
                    return ResponseEntity.status(200).body(messageRepository.save(messageOptional));
                } else {
                    throw new Exception("Message not found");
                }
            } else {
                throw new Exception("Message id required");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public ResponseEntity<?> deleteMessage(List<Long> messageId, boolean forEveryone) {
        try {
            if (messageId != null && !messageId.isEmpty()) {
                List<Message> messages = messageRepository.findAllById(messageId);
                if (messages.isEmpty()) {
                    throw new Exception("Message not found");
                }
                for (Message messageOptional : messages) {
                    if (forEveryone) {
                        Conversation conversation = messageOptional.getConversation();
                        if (conversation.getLastMessage().equals(messageOptional)) {
                            Page<Message> lastMsg = messageRepository.getMessageByConversation(conversation,
                                    PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt")));
                            if (lastMsg.getContent().size() > 0) {
                                conversation.setLastMessage(lastMsg.getContent().get(0));
                            } else {
                                conversation.setLastMessage(null);
                            }
                            conversationRepository.save(conversation);

                        }
                        messageRepository.delete(messageOptional);
                    } else {
                        messageOptional.setSelfDelete(true);
                        messageRepository.save(messageOptional);
                    }
                }
                return ResponseEntity.ok("Deleted");
            } else {
                throw new Exception("Message id required");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public ResponseEntity<?> messageReaction(Long messageId, Long userId, MessageResponse messageResponse) {
        try {
            Message messageOptional;
            UserData userDataOptional;

            if (messageId != null && userId != null) {
                messageOptional = getMessage(messageId);
                userDataOptional = userRepository.findById(userId).orElse(null);
                if (messageOptional != null && userDataOptional != null) {
                    List<Reaction> reactions = messageOptional.getReactions();
                    Reaction reactionRemove = null;
                    if (reactions.size() > 0) {
                        reactionRemove = reactions.stream()
                                .filter(r -> r.getUser().getId() == userId)
                                .findFirst()
                                .orElse(null);
                        reactions = reactions.stream()
                                .filter(r -> r.getUser().getId() != userId)
                                .collect(Collectors.toList());

                    }
                    if (messageResponse != null) {
                        Reaction reaction = Reaction
                                .builder()
                                .response(messageResponse)
                                .user(userDataOptional).build();
                        reactions.add(reactionRepository.save(reaction));
                    }

                    messageOptional.setReactions(reactions);
                    messageOptional = messageRepository.save(messageOptional);
                    if (reactionRemove != null) {
                        reactionRepository.delete(reactionRemove);
                    }
                    return ResponseEntity.status(200).body(messageOptional);
                } else {
                    throw new Exception("Message not found");
                }
            } else {
                throw new Exception("Message id required");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public Message getMessage(Long id) {
        if (id != null) {
            return messageRepository.findById(id).orElse(null);
        } else {
            return null;
        }
    }

    public ResponseEntity<?> getConvMessages(Long conversationId, Long selfId, PageRequest of) {
        try {
            Conversation conversationOptional;
            UserData userDataOptional;
            if (conversationId != null && selfId != null) {
                conversationOptional = conversationRepository
                        .findById(conversationId).orElse(null);
                userDataOptional = userRepository.findById(selfId).orElse(null);

                if (conversationOptional != null && userDataOptional != null) {
                    return ResponseEntity
                            .ok(messageRepository.getAllMessagesOfConv(conversationOptional, userDataOptional, of));
                } else {
                    throw new Exception("Self id or Conversation not found");
                }
            } else {
                throw new Exception("Self id or Conversation id required");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
