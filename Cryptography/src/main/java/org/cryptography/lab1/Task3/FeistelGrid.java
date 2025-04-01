package org.cryptography.lab1.Task3;

import org.cryptography.lab1.Task2.KeyExpansion;
import org.cryptography.lab1.Task2.RoundFunction;
import org.cryptography.lab1.Task2.SymmetricCipher;
import org.cryptography.lab1.Task2.SymmetricCipherContext;

import java.util.Arrays;

/**
 * Реализация функционала сети Фейстеля
 */

public class FeistelGrid implements SymmetricCipher {
    private final KeyExpansion keyExpansion;
    private final RoundFunction roundFunction;
    private byte[][] roundKeys;

    public FeistelGrid(KeyExpansion keyExpansion, RoundFunction roundFunction) {
        this.keyExpansion = keyExpansion;
        this.roundFunction = roundFunction;
    }

    @Override
    public void setRoundKeys(byte[] key) {
        this.roundKeys = keyExpansion.keyExpansion(key);
    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        byte[] leftBlock = Arrays.copyOfRange(plaintext, 0, plaintext.length / 2);
        byte[] rightBlock = Arrays.copyOfRange(plaintext, plaintext.length / 2, plaintext.length);



        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        return new byte[0];
    }

    @Override
    public int getBlockSize() {
        return 0;
    }
}
