package org.cryptography.lab1.Task3;

import org.cryptography.lab1.Task2.KeyExpansion;
import org.cryptography.lab1.Task2.RoundFunction;
import org.cryptography.lab1.Task2.SymmetricCipher;

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
        int halfSize = plaintext.length / 2;
        byte[] leftBlock = Arrays.copyOfRange(plaintext, 0, halfSize);
        byte[] rightBlock = Arrays.copyOfRange(plaintext, halfSize, plaintext.length);

        for (int i = 0; i < roundKeys.length; i++) {
            byte[] newRight = xor(leftBlock, roundFunction.roundConversion(rightBlock, roundKeys[i]));
            leftBlock = rightBlock;
            rightBlock = newRight;
        }
        return concatenate(leftBlock, rightBlock);
    }

    private byte[] xor(byte[] firstBlock, byte[] secondBlock) {
        if (firstBlock.length != secondBlock.length) {
            throw new IllegalArgumentException("Длины массивов должны совпадать");
        }
        byte[] result = new byte[firstBlock.length];
        for (int i = 0; i < firstBlock.length; i++) {
            result[i] = (byte) (firstBlock[i] ^ secondBlock[i]);
        }
        return result;
    }

    private byte[] concatenate(byte[] leftBlock, byte[] rightBlock) {
        byte[] result = new byte[leftBlock.length + rightBlock.length];
        System.arraycopy(leftBlock, 0, result, 0, leftBlock.length);
        System.arraycopy(rightBlock, 0, result, leftBlock.length, rightBlock.length);
        return result;
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        int halfSize = ciphertext.length / 2;
        byte[] leftBlock = Arrays.copyOfRange(ciphertext, 0, halfSize);
        byte[] rightBlock = Arrays.copyOfRange(ciphertext, halfSize, ciphertext.length);

        for (int i = roundKeys.length - 1; i >= 0; i--) {
            byte[] newRight = xor(leftBlock, roundFunction.roundConversion(rightBlock, roundKeys[i]));
            leftBlock = rightBlock;
            rightBlock = newRight;
        }
        return concatenate(leftBlock, rightBlock);
    }

    @Override
    public int getBlockSize() {
        return roundFunction.getBlockSize();
    }
}