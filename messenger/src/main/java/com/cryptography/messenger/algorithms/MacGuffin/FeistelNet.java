package com.cryptography.messenger.algorithms.MacGuffin;

import lombok.extern.slf4j.Slf4j;
import com.cryptography.messenger.algorithms.interfaces.KeyExpansion;
import com.cryptography.messenger.algorithms.interfaces.RoundFunction;
import com.cryptography.messenger.algorithms.interfaces.SymmetricCipher;

import java.util.Arrays;

import static com.cryptography.messenger.algorithms.utils.Utils.xor;

/**
 * Реализация функционала сети Фейстеля
 */
@Slf4j
public class FeistelNet implements SymmetricCipher {
    private final KeyExpansion keyExpansion;
    private final RoundFunction roundFunction;
    private byte[][] roundKeys;

    private static final int quarterBlockSize = 2;

    public FeistelNet(KeyExpansion keyExpansion, RoundFunction roundFunction) {
        this.keyExpansion = keyExpansion;
        this.roundFunction = roundFunction;
    }

    @Override
    public void setRoundKeys(byte[] key) {
        this.roundKeys = keyExpansion.keyExpansion(key);
    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        log.info("Encrypt plaintext: {}", plaintext);

        byte[] leftBlock = Arrays.copyOfRange(plaintext, 0, quarterBlockSize);
        byte[] rightBlock = Arrays.copyOfRange(plaintext, quarterBlockSize, plaintext.length);

        for (byte[] roundKey : roundKeys) {
            log.info("Round key: {}, Left block: {}, Right block {}", Arrays.toString(roundKey), Arrays.toString(leftBlock), Arrays.toString(rightBlock));
            byte[] newRight = xor(leftBlock, roundFunction.roundConversion(rightBlock, roundKey));
            leftBlock = rightBlock;
            rightBlock = newRight;
        }

        return concatenate(leftBlock, rightBlock);
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        int halfSize = ciphertext.length / 2;
        byte[] leftBlock = Arrays.copyOfRange(ciphertext, 0, halfSize);
        byte[] rightBlock = Arrays.copyOfRange(ciphertext, halfSize, ciphertext.length);

        for (int i = roundKeys.length - 1; i >= 0; i--) {
            byte[] newLeft = xor(rightBlock, roundFunction.roundConversion(leftBlock, roundKeys[i]));
            rightBlock = leftBlock;
            leftBlock = newLeft;
        }
        return concatenate(leftBlock, rightBlock);
    }

    private byte[] concatenate(byte[] leftBlock, byte[] rightBlock) {
        byte[] result = new byte[leftBlock.length + rightBlock.length];
        System.arraycopy(leftBlock, 0, result, 0, leftBlock.length);
        System.arraycopy(rightBlock, 0, result, leftBlock.length, rightBlock.length);
        return result;
    }

    @Override
    public int getBlockSize() {
        return roundFunction.getBlockSize();
    }
}