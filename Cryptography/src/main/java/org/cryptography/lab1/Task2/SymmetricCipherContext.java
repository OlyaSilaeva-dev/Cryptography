package org.cryptography.lab1.Task2;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SymmetricCipherContext {
    private final SymmetricCipher cipher;
    private final byte[] key;
    private final EncryptionMode encryptionMode;
    private final PaddingMode paddingMode;
    private byte[] iv;
    private final byte delta;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public SymmetricCipherContext(SymmetricCipher symmetricCipher, byte[] key, EncryptionMode encryptionMode,
                                  PaddingMode paddingMode, byte[] iv, byte... params) {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        this.key = key;
        this.cipher = symmetricCipher;
        this.encryptionMode = encryptionMode;
        this.paddingMode = paddingMode;
        this.iv = iv;
        if (encryptionMode == EncryptionMode.RandomDelta && params.length > 0) {
            this.delta = params[0];
        } else {
            this.delta = generateRandomDelta();
        }
    }

    private byte generateRandomDelta() {
        SecureRandom random = new SecureRandom();
        return (byte) (random.nextInt(256) - 128); // От -128 до 127
    }

    public CompletableFuture<byte[]> encryptAsync(byte[] plaintext) {
        return CompletableFuture.supplyAsync(() -> encrypt(plaintext), executor);
    }

    public byte[] encrypt(byte[] plaintext) {
        this.cipher.setRoundKeys(key);
        int blockSize = this.cipher.getBlockSize();

        plaintext = applyPadding(plaintext, blockSize);

        int blockCnt = (int) Math.ceil(plaintext.length / (double) blockSize);
        byte[][] blocks = new byte[blockCnt][blockSize];

        for (int i = 0; i < blockCnt; i++) {
            System.arraycopy(plaintext, i * blockSize, blocks[i], 0, blockSize);
        }

        log.info(Arrays.toString(blocks));
        return switch (encryptionMode) {
            case ECB -> ECBEncrypt(blocks, blockSize, blockCnt);
            case CBC -> CBCEncrypt(blocks, blockSize, blockCnt);
            case PCBC -> PCBCEncrypt(blocks, blockSize, blockCnt);
            case CFB -> CFBEncrypt(blocks, blockSize, blockCnt);
            case OFB -> OFBEncrypt(blocks, blockSize, blockCnt);
            case CTR -> CTREncrypt(blocks, blockSize, blockCnt);
            case RandomDelta -> RandomDeltaEncrypt(blocks, blockSize, blockCnt);
        };
    }

    private byte[] applyPadding(byte[] data, int blockSize) {
        int paddingSize = blockSize - (data.length % blockSize);
        byte[] padded = Arrays.copyOf(data, data.length + paddingSize);

        switch (paddingMode) {
            case Zeros -> {
                Arrays.fill(padded, data.length, padded.length, (byte) 0);
            }
            case ANSI_X923 -> {
                Arrays.fill(padded, data.length, padded.length - 1, (byte) 0);
                padded[padded.length - 1] = (byte) paddingSize;
            }
            case PKCS7 -> {
                Arrays.fill(padded, data.length, padded.length - 1, (byte) paddingSize);
            }
            case ISO_10126 -> {
                SecureRandom random = new SecureRandom();
                byte[] randomBytes = new byte[paddingSize - 1];
                random.nextBytes(randomBytes);
                System.arraycopy(randomBytes, 0, padded, padded.length, randomBytes.length);
                padded[padded.length - 1] = (byte) paddingSize;
            }
        }
        return padded;
    }

    private byte[] XOR2Blocks(byte[] a, byte[] b) {
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    private byte[] ECBEncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] ciphertext = new byte[blockCnt];
        for (int i = 0; i < blockCnt; i++) {
            byte[] cipherBlock = cipher.encrypt(blocks[i]);
            System.arraycopy(cipherBlock, 0, ciphertext, i * blockSize, blockSize);
        }
        return ciphertext;
    }

    private byte[] CBCEncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] prevCipherBlock = iv.clone();
        byte[] ciphertext = new byte[blockCnt * blockSize];

        for (int i = 0; i < blockCnt; i++) {
            byte[] xorBlock = XOR2Blocks(blocks[i], prevCipherBlock);
            byte[] cipherBlock = cipher.encrypt(xorBlock);
            System.arraycopy(cipherBlock, 0, ciphertext, i * blockSize, blockSize);
            prevCipherBlock = cipherBlock.clone();
        }
        return ciphertext;
    }

    private byte[] PCBCEncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] prevCipherBlock = iv.clone();
        byte[] ciphertext = new byte[blockCnt * blockSize];

        for (int i = 0; i < blockCnt; i++) {
            byte[] xorBlock = XOR2Blocks(blocks[i], prevCipherBlock);
            byte[] cipherBlock = cipher.encrypt(xorBlock);
            System.arraycopy(cipherBlock, 0, ciphertext, i * blockSize, blockSize);

            if (i < blockCnt - 1) {
                prevCipherBlock = XOR2Blocks(blocks[i], cipherBlock); ///??? blocks[i]
            }
        }

        return ciphertext;
    }

    private byte[] CFBEncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] ciphertext = new byte[blockCnt * blockSize];
        byte[] feedback = iv.clone();

        for (int i = 0; i < blockCnt; i++) {
            byte[] cipherBlock = cipher.encrypt(feedback);
            byte[] xorResult = XOR2Blocks(blocks[i], cipherBlock);
            System.arraycopy(cipherBlock, 0, ciphertext, i * blockSize, blockSize);
            feedback = xorResult.clone();
        }
        return ciphertext;
    }

    private byte[] OFBEncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] ciphertext = new byte[blockCnt * blockSize];
        byte[] feedback = iv.clone();

        for (int i = 0; i < blockCnt; i++) {
            feedback = cipher.encrypt(feedback);
            byte[] xorResult = XOR2Blocks(blocks[i], feedback);
            System.arraycopy(xorResult, 0, ciphertext, i * blockSize, blockSize);
        }
        return ciphertext;
    }

    private byte[] CTREncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] ciphertext = new byte[blockCnt * blockSize];
        byte[] counter = iv.clone();

        for (int i = 0; i < blockCnt; i++) {
            byte[] encryptedBlock = cipher.encrypt(blocks[i]);
            byte[] xorResult = XOR2Blocks(blocks[i], encryptedBlock);
            System.arraycopy(xorResult, 0, ciphertext, i * blockSize, blockSize);
            incrementCounter(counter);
        }
        return ciphertext;
    }

    private void incrementCounter(byte[] counter) {
        for (int i = counter.length - 1; i >= 0; i--) {
            if (++counter[i] != 0) break;
        }
    }

    private byte[] RandomDeltaEncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] ciphertext = new byte[blockCnt * blockSize];
        byte[] counter = iv.clone();

        for (int i = 0; i < blockCnt; i++) {
            byte[] xorBlock = XOR2Blocks(blocks[i], iv);
            byte[] cipherBlock = cipher.encrypt(xorBlock);
            System.arraycopy(cipherBlock, 0, ciphertext, i * blockSize, blockSize);
            increaseCounterByDelta(counter);
        }
        return ciphertext;
    }

    private void increaseCounterByDelta(byte[] counter) {
        int carry = delta;
        for (int i = counter.length - 1; i >= 0; i--) {
            int sum = (counter[i] & 0xFF) + carry;
            counter[i] = (byte) sum;
            carry = sum >> 8;
            if (carry == 0) break;
        }
    }

    public byte[] decrypt(byte[] ciphertext) {
        //TODO реализация
        return cipher.decrypt(ciphertext);
    }
}
