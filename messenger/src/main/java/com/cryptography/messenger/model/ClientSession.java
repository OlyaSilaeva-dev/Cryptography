package com.cryptography.messenger.model;

import com.cryptography.messenger.service.DiffieHellman;
import lombok.Data;

@Data
public class ClientSession {
    private final String sessionId;        // ID WebSocket-сессии
    private final long privateKey;         // Секретный ключ клиента
    private long publicKeyPeer;            // Публичный ключ второго клиента
    private long p;                        // Общее простое число
    private long g;                        // Примитивный корень
    private long sharedSecret;             // Общий секретный ключ (вычисляется после обмена)

    public ClientSession(String sessionId, long privateKey, long p, long g) {
        this.sessionId = sessionId;
        this.privateKey = privateKey;
        this.p = p;
        this.g = g;
    }

    public void setPublicKeyPeer(long publicKeyPeer) {
        this.publicKeyPeer = publicKeyPeer;
        this.sharedSecret = DiffieHellman.modPow(publicKeyPeer, privateKey, p);
    }
}
