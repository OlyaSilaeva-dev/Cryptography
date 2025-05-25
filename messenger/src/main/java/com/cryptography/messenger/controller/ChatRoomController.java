package com.cryptography.messenger.controller;

import com.cryptography.messenger.dto.ChatRoomResponse;
import com.cryptography.messenger.dto.NewChatRoomDTO;
import com.cryptography.messenger.model.ChatRoom;
import com.cryptography.messenger.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/add")
    public ResponseEntity<ChatRoom> addChat(@RequestBody NewChatRoomDTO dto) {
        ChatRoom createdRoom = chatRoomService.createChatRoom(dto);
        return ResponseEntity.ok(createdRoom);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getChat(@PathVariable String roomId) {
        return ResponseEntity.ok(chatRoomService.getRoomById(roomId));
    }

    @GetMapping
    public ResponseEntity<Collection<ChatRoomResponse>> getAllChats(@RequestParam String userId) {
        return ResponseEntity.ok(chatRoomService.getAllChats(userId));
    }
}

