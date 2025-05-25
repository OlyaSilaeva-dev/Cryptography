package com.cryptography.messenger.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "message")
public class Message {
    @Id
    private String id;
    private MessageType type;
    private String content;
    private String sender;
    private String sessionId;
    private String roomId;
}