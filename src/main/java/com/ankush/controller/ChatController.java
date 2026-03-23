package com.ankush.controller;

import com.ankush.model.ChatRequest;
import com.ankush.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public String chat(@RequestBody ChatRequest request) throws Exception {

        return chatService.getBotResponse(request.getMessage());

    }
}