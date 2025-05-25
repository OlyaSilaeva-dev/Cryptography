package com.cryptography.messenger.dto;

import lombok.Data;

@Data
public class KeyExchangeMessage {
    private String sessionId;
    private Long publicKey;
    private Long p;
    private Long g;
}
