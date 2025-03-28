package org.cryptography.lab1.Task2;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class SymmetricCipherContext {
    private final SymmetricCipher cipher;
    private final byte[] key;
    private final EncryptionMode encryptionMode;
    private final PaddingMode paddingMode;
    private byte[] iv; //

    public SymmetricCipherContext(SymmetricCipher symmetricCipher, byte[] key, EncryptionMode encryptionMode,
                                  PaddingMode paddingMode, byte[] iv, byte ... params) {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        this.key = key;
        this.cipher = symmetricCipher;
        this.encryptionMode = encryptionMode;
        this.paddingMode = paddingMode;
        this.iv = iv;
    }

    public byte[] encrypt(byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        this.cipher.setRoundKeys(key);
        int blockSize = this.cipher.getBlockSize();
        int blockCnt = plaintext.length / blockSize;

        byte[][] blocks = new byte[blockCnt][blockSize];
        for (int i = 0; i < blockCnt; i++) {
            blocks[i] = Arrays.copyOfRange(plaintext, i * blockSize, (i + 1) * blockSize);
        }
        log.info(Arrays.toString(blocks));

        switch (encryptionMode) {
            case ECB -> ciphertext = ECBEncrypt(blocks, blockSize, blockCnt);
            case CBC -> ciphertext = CBCEncrypt(blocks, blockSize, blockCnt);
            case PCBC -> ciphertext = PCBCEncrypt(blocks, blockSize, blockCnt);
            case CFB -> ciphertext = CFBEncrypt(blocks, blockSize, blockCnt);
            case OFB -> {

            }
            case CTR -> {

            }
        }
        return ciphertext;
    }

    private byte[] XOR2Blocks(byte[] a, byte[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Lengths don't match");
        }

        for (int i = 0; i < a.length; i++) {
            a[i] ^= b[i];
        }
        return a;
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
        byte[] nextBlock = XOR2Blocks(blocks[0], iv);
        byte[] ciphertext = new byte[blockCnt * blockSize];

        for (int i = 0; i < blockCnt; i++) {
            byte[] cipherBlock = cipher.encrypt(nextBlock);
            System.arraycopy(cipherBlock, 0, ciphertext, i * blockSize, blockSize);
            if (i < blockCnt - 1) {
                nextBlock = XOR2Blocks(cipherBlock, blocks[i + 1]);
            }
        }

        return ciphertext;
    }

    private byte[] PCBCEncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] nextBlock = XOR2Blocks(blocks[0], iv);
        byte[] ciphertext = new byte[blockCnt];

        for (int i = 0; i < blockCnt; i++) {
            byte[] cipherBlock = cipher.encrypt(nextBlock);
            System.arraycopy(cipherBlock, 0, ciphertext, i * blockSize, blockSize);
            if (i < blockCnt - 1) {
                for (int j = 0; j < blockSize; j++) {
                    nextBlock[j] = (byte) (blocks[i][j] ^ cipherBlock[j] ^ blocks[i + 1][j]);
                }
            }
        }

        return ciphertext;
    }

    private byte[] CFBEncrypt(byte[][] blocks, int blockSize, int blockCnt) {
        byte[] ciphertext = new byte[blockCnt * blockSize];

        byte[] cipherBlock = cipher.encrypt(iv);
        for (int i = 0; i < blockCnt; i++) {
            XOR2Blocks(cipherBlock, blocks[i]);
            System.arraycopy(cipherBlock, 0, ciphertext, i * blockSize, blockSize);
            cipherBlock = cipher.encrypt(cipherBlock);
        }

        return ciphertext;
    }

    



    public byte[] decrypt(byte[] ciphertext) {
        return cipher.decrypt(ciphertext);
    }
}
