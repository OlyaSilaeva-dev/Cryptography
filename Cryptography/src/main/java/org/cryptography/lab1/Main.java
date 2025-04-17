package org.cryptography.lab1;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.DES.DES;
import org.cryptography.lab1.enums.EncryptionMode;
import org.cryptography.lab1.enums.PaddingMode;
import org.cryptography.lab1.symmetricCipherContext.SymmetricCipherContext;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class Main {
    public static void main(String[] args) {
        byte[] message = "Hello World!".getBytes(StandardCharsets.UTF_8);
        log.info(Arrays.toString(message));
        printBytes(message);
        log.info("input length: {}", message.length);

        byte[] key = "1234567".getBytes(StandardCharsets.UTF_8);
        for (byte b : key) {
            System.out.print(b + " ");
        }
        System.out.println();

        SymmetricCipherContext symmetricCipherContext = new SymmetricCipherContext(
                new DES(),
                key,
                EncryptionMode.ECB,
                PaddingMode.Zeros,
                new byte[8]
        );


        CompletableFuture<Void> future = symmetricCipherContext.encryptAsync(message)
                .thenApply(ciphertext -> {
                    System.out.println("Encrypted (base64): " + Base64.getEncoder().encodeToString(ciphertext));
                    printBytes(ciphertext);
                    return ciphertext;
                })
                .thenCompose(symmetricCipherContext::decryptAsync)
                .thenAccept(decrypted -> {
                    System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
                    printBytes(decrypted);
                    System.out.println("Output length: " + decrypted.length);
                })
                .exceptionally(ex -> {
                    log.error("Exception during crypto: {}", ex.getMessage(), ex);
                    return null;
                });

        future.join();
        symmetricCipherContext.shutdown(); 

    }

    private static void printResultByUTF8(byte[] data) {
        String decryptedText = new String(data, StandardCharsets.UTF_8);
        System.out.println("Дешифрованный текст: " + decryptedText);
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
            System.out.print(Integer.toBinaryString(key[i] & 0xFF) + " ");
        }
        System.out.println();
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