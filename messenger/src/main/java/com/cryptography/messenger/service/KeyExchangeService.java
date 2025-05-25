package com.cryptography.messenger.service;

import com.cryptography.messenger.model.ClientSession;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeyExchangeService {
    private final Map<String, ClientSession> sessions = new ConcurrentHashMap<>();

    public ClientSession createSession(String sessionId) {
        long p = DiffieHellman.generatePrime(10000, 50000);
        long g = DiffieHellman.findPrimitiveRoot(p);
        long privateKey = 2 + Math.abs(new Random().nextLong()) % (p - 2);
        ClientSession session = new ClientSession(sessionId, privateKey, p, g);
        sessions.put(sessionId, session);
        return session;
    }

    public ClientSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void setPeerPublicKey(String sessionId, long peerPublicKey) {
        ClientSession session = sessions.get(sessionId);
        if (session != null) {
            session.setPublicKeyPeer(peerPublicKey);
        }
    }

    public long getPublicKey(String sessionId) {
        ClientSession session = sessions.get(sessionId);
        if (session == null) return -1;
        return DiffieHellman.modPow(session.getG(), session.getPrivateKey(), session.getP());
    }

    public long getSharedSecret(String sessionId) {
        ClientSession session = sessions.get(sessionId);
        return session != null ? session.getSharedSecret() : -1;
    }
}

