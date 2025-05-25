package com.cryptography.messenger.configuration;
import com.cryptography.messenger.model.Message;
import com.cryptography.messenger.model.MessageType;
import com.cryptography.messenger.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = null;
        String roomId = null;

        if (headerAccessor.getSessionAttributes() != null) {
            username = (String) headerAccessor.getSessionAttributes().get("username");
            roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        }

        if (username != null && roomId != null) {
            log.info("user disconnected: {}", username);
            var chatMessage = Message.builder()
                    .type(MessageType.DISCONNECT)
                    .sender(username)
                    .roomId(roomId)
                    .build();
            messagingTemplate.convertAndSend("/topic/" + roomId, chatMessage);
        }
    }
}