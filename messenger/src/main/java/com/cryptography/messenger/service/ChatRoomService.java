package com.cryptography.messenger.service;

import com.cryptography.messenger.dto.ChatRoomResponse;
import com.cryptography.messenger.dto.NewChatRoomDTO;
import com.cryptography.messenger.exceptions.ChatAlreadyExistsException;
import com.cryptography.messenger.exceptions.UnknownUserException;
import com.cryptography.messenger.model.ChatRoom;
import com.cryptography.messenger.model.User;
import com.cryptography.messenger.repository.ChatRoomRepository;
import com.cryptography.messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoom createChatRoom(NewChatRoomDTO dto) {
        String firstUserId = dto.getFirstUserId();
        String secondUserId = dto.getSecondUserId();

        if (firstUserId == null || secondUserId == null) {
            throw new UnknownUserException();
        }

        if (!userRepository.existsById(firstUserId) || !userRepository.existsById(secondUserId)) {
            throw new UnknownUserException();
        }

        if (chatRoomRepository.existsByFirstUserIdAndSecondUserId(firstUserId, secondUserId)) {
            throw  new ChatAlreadyExistsException();
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .firstUserId(firstUserId)
                .secondUserId(secondUserId)
                .build();

        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public ChatRoom getRoomById(String id) {
        return chatRoomRepository.findById(id).orElse(null);
    }

    public List<ChatRoomResponse> getAllChats(String userId) {
        List<ChatRoom> chats = chatRoomRepository.findAll();
        List<ChatRoomResponse> result = new ArrayList<>();
        userRepository.findById(userId);

        for (ChatRoom chatRoom : chats) {
            if (chatRoom.getFirstUserId().equals(userId)) {
                User user = userRepository.findById(chatRoom.getSecondUserId()).orElseThrow(UnknownUserException::new);
                String roomName = user.getUsername();
                result.add(new ChatRoomResponse(chatRoom, roomName));
            }
            if (chatRoom.getSecondUserId().equals(userId)) {
                User user = userRepository.findById(chatRoom.getFirstUserId()).orElseThrow(UnknownUserException::new);
                String roomName = user.getUsername();
                result.add(new ChatRoomResponse(chatRoom, roomName));
            }
        }
        return result;
    }
}
