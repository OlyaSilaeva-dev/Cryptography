package org.cryptography.lab1;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.DES.DES;
import org.cryptography.lab1.enums.EncryptionMode;
import org.cryptography.lab1.enums.PaddingMode;
import org.cryptography.lab1.symmetricCipherContext.SymmetricCipherContext;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class Main {
    public static void main(String[] args) {
        byte[] message = "Hello World!".getBytes(StandardCharsets.UTF_8);
        System.out.println("Plaintext: " + new String(message));
        log.info("input length: {}", message.length);

        byte[] iv = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        byte[] key = generateKey();

        SymmetricCipherContext symmetricCipherContext = new SymmetricCipherContext(
                new DES(),
                key,
                EncryptionMode.RandomDelta,
                PaddingMode.PKCS7,
                iv,
                (byte) 0x11
        );

        CompletableFuture<Void> future = symmetricCipherContext.encryptAsync(message)
                .thenApply(ciphertext -> {
                    System.out.println("Encrypted: " + new String(ciphertext.toString().getBytes(StandardCharsets.UTF_8)));
                    return ciphertext;
                })
                .thenCompose(symmetricCipherContext::decryptAsync)
                .thenAccept(decrypted -> {
                    System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
                })
                .exceptionally(ex -> {
                    log.error("Exception during crypto: {}", ex.getMessage(), ex);
                    return null;
                });

        future.join();
        symmetricCipherContext.shutdown(); 

    }

    public static void printBytes(byte[] data) {
        for (byte b : data) {
            System.out.print(Integer.toBinaryString(b & 0xFF) + " ");
        }
        System.out.println();
    }

    public static byte[] generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[7]; // 7 байт = 56 бит
        random.nextBytes(key);
        for (int i = 0; i < key.length; i++) {
            key[i] = (byte) (key[i] & 0xFF);
        }
        return key;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}