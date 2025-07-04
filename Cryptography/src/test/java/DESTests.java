import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.DEAL.DEAL;
import org.cryptography.lab1.DES.DES;
import org.cryptography.lab1.enums.DEALModes;
import org.cryptography.lab1.enums.EncryptionMode;
import org.cryptography.lab1.enums.PaddingMode;
import org.cryptography.lab1.symmetricCipherContext.SymmetricCipherContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class DESTests {

    @Test
    void textEncryptionDESTest() {
        byte[] message = "Hello World!".getBytes();
        byte[] iv = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        byte[] key = addParityBits(generateKey());
        SymmetricCipherContext symmetricCipherContext = new SymmetricCipherContext(
                new DES(),
                key,
                EncryptionMode.RandomDelta,
                PaddingMode.PKCS7,
                iv,
                (byte) 0x11
        );

        byte[] decryptText = encryptionDecryptionProcess(message, symmetricCipherContext);
        Assertions.assertArrayEquals(message, decryptText);

        symmetricCipherContext.shutdown();
    }

    @Test
    void fileEncryptionDESTest() {
        byte[] iv = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        byte[] key = addParityBits(generateKey());
        SymmetricCipherContext symmetricCipherContext = new SymmetricCipherContext(
                new DES(),
                key,
                EncryptionMode.PCBC,
                PaddingMode.ANSI_X923,
                iv
        );

        File fi = new File("src/main/resources/test1.jpg");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        byte[] fileContent;
        byte[] plainTextAfterDecryption;
        try {
            fileContent = Files.readAllBytes(fi.toPath());
            plainTextAfterDecryption = encryptionDecryptionProcess(fileContent, symmetricCipherContext);
            Path outputPath = Paths.get("decrypted_output.jpg");
            Files.write(outputPath, plainTextAfterDecryption);
            log.info("Decrypted image saved to {}", outputPath.toAbsolutePath());
        } catch (Exception e) {
            log.error("Exception during crypto: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        Assertions.assertArrayEquals(fileContent, plainTextAfterDecryption);

        symmetricCipherContext.shutdown();
    }

    @Test
    void fileEncryptionDEALTest() {
        byte[] iv = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        byte[] key = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

        SymmetricCipherContext symmetricCipherContext = new SymmetricCipherContext(
                new DEAL(DEALModes.DEAL_128),
                key,
                EncryptionMode.ECB,
                PaddingMode.Zeros,
                iv
        );

        File fi = new File("src/main/resources/test1.jpg");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        byte[] fileContent;
        byte[] plainTextAfterDecryption;
        try {
            fileContent = Files.readAllBytes(fi.toPath());
            plainTextAfterDecryption = encryptionDecryptionProcess(fileContent, symmetricCipherContext);
            Path outputPath = Paths.get("decrypted_output.jpg");
            Files.write(outputPath, plainTextAfterDecryption);
            log.info("Decrypted image saved to {}", outputPath.toAbsolutePath());
        } catch (Exception e) {
            log.error("Exception during crypto: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        Assertions.assertArrayEquals(fileContent, plainTextAfterDecryption);

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


    private static byte[] addParityBits(byte[] key56) {
        byte[] key64 = new byte[8];
        for (int i = 0; i < 7; i++) {
            int b = key56[i] & 0xFE;
            int parity = Integer.bitCount(b) % 2 == 0 ? 1 : 0;
            key64[i] = (byte) (b | parity);
        }

        int lastBits = ((key56[6] & 0xFE) << 1);
        int parity = Integer.bitCount(lastBits >> 1) % 2 == 0 ? 1 : 0;
        key64[7] = (byte) ((lastBits & 0xFE) | parity);

        return key64;
    }
}
