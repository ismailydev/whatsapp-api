package com.example.whatsapp.controller;

import com.example.whatsapp.model.enums.ConversationType;
import com.example.whatsapp.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("/create")
    public ResponseEntity<?> createConv(@RequestBody List<Long> members, ConversationType type) {
        return conversationService.createConversation(members, type);
    };

    @GetMapping("single/{id}")
    public ResponseEntity<?> getConv(@PathVariable("id") Long id) {
        return conversationService.getConversation(id);
    }

    @GetMapping("byPhone/{phone}")
    public ResponseEntity<?> getConversationsByPhone(@PathVariable("phone") String phone, int page, int size) {
        return conversationService.getConversationsByPhone(phone, PageRequest.of(page, size));
    }
}
