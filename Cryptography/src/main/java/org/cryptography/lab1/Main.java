package org.cryptography.lab1;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.DES.DES;
import org.cryptography.lab1.enums.EncryptionMode;
import org.cryptography.lab1.enums.PaddingMode;
import org.cryptography.lab1.symmetricCipherContext.SymmetricCipherContext;

import java.nio.file.Files;
import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class Main {
    public static void main(String[] args) throws RuntimeException {
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

        byte[] plainTextAfterDecryption = encryptionDecryptionProcess(message, symmetricCipherContext);
        System.out.println("Decrypted: " + new String(plainTextAfterDecryption, StandardCharsets.UTF_8));

        /*File fi = new File("src/main/resources/test1.jpg");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        try {
            byte[] fileContent = Files.readAllBytes(fi.toPath());
            byte[] plainTextAfterDecryption = encryptionDecryptionProcess(fileContent, symmetricCipherContext);

            Path outputPath = Paths.get("decrypted_output.jpg");
            Files.write(outputPath, plainTextAfterDecryption);
            log.info("Decrypted image saved to {}", outputPath.toAbsolutePath());
        } catch (Exception e) {
            log.error("Exception during crypto: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }*/

        symmetricCipherContext.shutdown();
    }

    private static byte[] encryptionDecryptionProcess(byte[] message, SymmetricCipherContext symmetricCipherContext) {
        final byte[][] plainTextAfterDecryption = {new byte[1]};
        CompletableFuture<Void> future = symmetricCipherContext.encryptAsync(message)
                .thenApply(ciphertext -> {
                    System.out.println("Encrypted (hex): " + bytesToHex(ciphertext));
                    return ciphertext;
                })
                .thenCompose(symmetricCipherContext::decryptAsync)
                .thenAccept(decrypted -> {
                    plainTextAfterDecryption[0] = decrypted;
                })
                .exceptionally(ex -> {
                    log.error("Exception during crypto: {}", ex.getMessage(), ex);
                    return null;
                });
        future.join();

        return plainTextAfterDecryption[0];
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