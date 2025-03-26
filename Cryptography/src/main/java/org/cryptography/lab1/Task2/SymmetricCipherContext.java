package org.cryptography.lab1.Task2;

public class SymmetricCipherContext {
    private final SymmetricCipher cipher;
    private final byte[] key;
    private final EncryptionMode encryptionMode;
    private final PaddingMode paddingMode;
    private byte[] iv; //

    public SymmetricCipherContext(SymmetricCipher symmetricCipher, byte[] key, EncryptionMode encryptionMode, PaddingMode paddingMode, byte[] iv, byte ... params) {
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
        byte[] outputBytes = new byte[plaintext.length];

        switch (encryptionMode) {
            case ECB -> {
                outputBytes = cipher.encrypt(plaintext);
                break;
            }
            case CBC -> {

            }
            case PCBC -> {

            }
            case CFB -> {

            }
            case OFB -> {

            }
            case CTR -> {

            }
        }
        return outputBytes;
    }

    public byte[] decrypt(byte[] ciphertext) {
        return cipher.decrypt(ciphertext);
    }
}
