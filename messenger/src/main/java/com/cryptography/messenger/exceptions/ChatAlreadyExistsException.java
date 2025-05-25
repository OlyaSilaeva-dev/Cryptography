package com.cryptography.messenger.exceptions;

public class ChatAlreadyExistsException extends RuntimeException {
    public ChatAlreadyExistsException(String message) {
        super(message);
    }

    public ChatAlreadyExistsException() { this("Chat already exists!"); }
}
