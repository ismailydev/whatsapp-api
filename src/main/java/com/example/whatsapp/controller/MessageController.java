package com.example.whatsapp.controller;

import com.example.whatsapp.model.dto.MessageDto;
import com.example.whatsapp.model.dto.MessageEditDto;
import com.example.whatsapp.model.enums.MessageResponse;
import com.example.whatsapp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping(path = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createMessage(@ModelAttribute MessageDto messageDto) {
        return messageService.createMessage(messageDto);
    };

    @PostMapping("open/{id}")
    public ResponseEntity<?> openMessage(@PathVariable("id") Long messageId, Long selfId) {
        return messageService.openMessage(messageId, selfId);
    }

    @PutMapping(path = "edit/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> editMessage(@PathVariable("id") Long messageId,
            @ModelAttribute MessageEditDto messageEditDto) {
        return messageService.editMessage(messageId, messageEditDto);
    }

    @PutMapping("react/{id}")
    public ResponseEntity<?> reactToMessage(@PathVariable("id") Long messageId, Long userId,
            MessageResponse messageResponse) {
        return messageService.messageReaction(messageId, userId, messageResponse);
    }

    @DeleteMapping("delete/{forEveryone}")
    public ResponseEntity<?> deleteMessage(List<Long> messageId, @PathVariable("forEveryone") boolean forEveryone) {
        return messageService.deleteMessage(messageId, forEveryone);
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<?> getConvMessages(@PathVariable("conversationId") Long conversationId, Long selfId, int page,
            int size) {
        return messageService.getConvMessages(conversationId, selfId, PageRequest.of(page, size));
    }

}
