package com.cryptography.messenger.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatroom")
public class ChatRoom {
    @Id
    private String id;
    private String firstUserId;
    private String secondUserId;
}
