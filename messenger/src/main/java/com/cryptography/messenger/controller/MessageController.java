package com.cryptography.messenger.controller;

import com.cryptography.messenger.dto.KeyExchangeMessage;
import com.cryptography.messenger.model.ClientSession;
import com.cryptography.messenger.repository.MessageRepository;
import com.cryptography.messenger.service.DiffieHellman;
import com.cryptography.messenger.service.KeyExchangeService;
import com.cryptography.messenger.service.MessageEncryptionService;
import com.cryptography.messenger.service.brocker.Sender;
import com.cryptography.messenger.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
public class MessageController {

    private final Sender sender;
    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageRepository messageRepository;
    private final MessageEncryptionService messageEncryptionService;
    private final KeyExchangeService keyExchangeService;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    public MessageController(Sender sender, SimpMessageSendingOperations messagingTemplate, MessageRepository messageRepository, MessageEncryptionService messageEncryptionService, KeyExchangeService keyExchangeService) {
        this.sender = sender;
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.messageEncryptionService = messageEncryptionService;
        this.keyExchangeService = keyExchangeService;
    }

    @MessageMapping("/chat.send-message")
    public void sendMessage(
            @Payload Message chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        if (chatMessage == null || headerAccessor == null) {
            logger.error("Received null message or headerAccessor");
            return;
        }

        chatMessage.setSessionId(headerAccessor.getSessionId());
        chatMessage.setContent(messageEncryptionService.encrypt(chatMessage.getContent(), keyExchangeService.getSharedSecret(chatMessage.getSessionId())));

        messageRepository.save(chatMessage);

        Message toSend = Message.builder()
                .id(chatMessage.getId())
                .type(chatMessage.getType())
                .roomId(chatMessage.getRoomId())
                .sender(chatMessage.getSender())
                .sessionId(chatMessage.getSessionId())
                .content(messageEncryptionService.decrypt(chatMessage.getContent(), keyExchangeService.getSharedSecret(chatMessage.getSessionId())))
                .build();

        String topic = "/topic/" + chatMessage.getRoomId();
        messagingTemplate.convertAndSend(topic, toSend);

        logger.info("Message sent to {} : {}", topic, toSend);
    }

    @MessageMapping("/chat.key-exchange")
    public void handleKeyExchange(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalStateException("Principal is null");
        }

        String username = principal.getName();
        log.info("Received key exchange: {}", username);
        ClientSession session = keyExchangeService.createSession(username);

        Map<String, Object> payload = new HashMap<>();
        payload.put("p", session.getP());
        payload.put("g", session.getG());
        payload.put("publicKey", keyExchangeService.getPublicKey(username));

        messagingTemplate.convertAndSendToUser(username, "/queue/key-exchange", payload);
    }

    @MessageMapping("/chat.public-key")
    public void receivePublicKey(@Payload Map<String, Object> msg, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return;
        }

        String sessionId = principal.getName();
        long publicKey = Long.parseLong(msg.get("publicKey").toString());
        keyExchangeService.setPeerPublicKey(sessionId, publicKey);

        long sharedSecret = keyExchangeService.getSharedSecret(sessionId);
        System.out.println("Shared secret for " + sessionId + ": " + sharedSecret);
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId) {
        return ResponseEntity.ok(messageRepository.findByRoomId(roomId));
    }
}