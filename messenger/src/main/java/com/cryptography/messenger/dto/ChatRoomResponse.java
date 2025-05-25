package com.cryptography.messenger.dto;

import com.cryptography.messenger.model.ChatRoom;
import lombok.Data;

@Data
public class ChatRoomResponse {
    private ChatRoom chatRoom;
    private String roomName;

    public ChatRoomResponse(ChatRoom chatRoom, String roomName) {
        this.chatRoom = chatRoom;
        this.roomName = roomName;
    }
}
