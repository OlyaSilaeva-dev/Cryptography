package org.cryptography.lab1.DES;

import org.cryptography.lab1.enums.BitsOrder;
import org.cryptography.lab1.interfaces.KeyExpansion;
import org.cryptography.lab1.rearrangingBits.RearrangingBits;

public final class DESKeyExpansion implements KeyExpansion {

    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9, 1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 27, 19, 11, 3,
            60, 52, 44, 36, 63, 55, 47, 39,
            31, 23, 15, 7, 62, 54, 46, 38,
            30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 28, 20, 12, 4
    };

    private static final int[] SHIFTS = {
            1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1
    };

    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
    };


    @Override
    public byte[][] keyExpansion(byte[] key) {
        if (key.length != 7) {
            throw new IllegalArgumentException("DES key must be 64 bits (8 bytes) long");
        }
        byte[] expandedKey = addBits(key);

        byte[] pc1Key = RearrangingBits.rearrangingBits(expandedKey, PC1, BitsOrder.MSB_FIRST, 1);

        byte[] C = extractBits(pc1Key, 0, 28);
        byte[] D = extractBits(pc1Key, 28, 28);

        byte[][] roundKeys = new byte[16][6];

        for (int i = 0; i < 16; i++) {
            C = shiftLeft(C, 28, SHIFTS[i]);
            D = shiftLeft(D, 28, SHIFTS[i]);

            byte[] CD = joinBitArrays(C, D, 28);

            byte[] roundKey = RearrangingBits.rearrangingBits(expandedKey, PC2, BitsOrder.MSB_FIRST, 1);

            roundKeys[i] = roundKey;
        }

        return roundKeys;
    }

    private static byte[] addBits(byte[] key) {
        byte[] result = new byte[8];
        int bitIndex = 0;

        for (int i = 0; i < 8; i++) {
            int b = 0;
            int onesCnt = 0;

            for (int j = 0; j < 7; j++) {
                int byteIndex = bitIndex / 8;
                int bitInByte = 7 - (bitIndex % 8);

                int bit = (key[byteIndex] >> bitInByte) & 1;
                b = (b << 1) | bit;

                onesCnt += bit;
                bitIndex++;
            }

            int parity = (onesCnt % 2 == 0) ? 1 : 0;
            result[i] = (byte) ((b << 1) | parity);
        }

        return result;
    }

    // Возвращает битовый подмассив длиной `bitLength`, начиная с bitOffset
    private static byte[] extractBits(byte[] src, int bitOffset, int bitLength) {
        byte[] result = new byte[(bitLength + 7) / 8];
        for (int i = 0; i < bitLength; i++) {
            int srcByte = (bitOffset + i) / 8;
            int srcBit = 7 - ((bitOffset + i) % 8);
            int bit = (src[srcByte] >> srcBit) & 1;

            int dstByte = i / 8;
            int dstBit = 7 - (i % 8);
            result[dstByte] |= (byte) (bit << dstBit);
        }
        return result;
    }

    // Сдвиг влево на n бит для массива длиной bitLength
    private static byte[] shiftLeft(byte[] data, int bitLength, int n) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < bitLength; i++) {
            int srcIndex = (i + n) % bitLength;

            int srcByte = srcIndex / 8;
            int srcBit = 7 - (srcIndex % 8);
            int bit = (data[srcByte] >> srcBit) & 1;

            int dstByte = i / 8;
            int dstBit = 7 - (i % 8);
            result[dstByte] |= (byte) (bit << dstBit);
        }
        return result;
    }

    // Объединяет два битовых массива длиной leftBits бит каждый
    private static byte[] joinBitArrays(byte[] a, byte[] b, int bitLengthEach) {
        int totalBits = bitLengthEach * 2;
        byte[] result = new byte[(totalBits + 7) / 8];

        for (int i = 0; i < totalBits; i++) {
            byte[] src = i < bitLengthEach ? a : b;
            int srcBitIndex = i % bitLengthEach;

            int srcByte = srcBitIndex / 8;
            int srcBit = 7 - (srcBitIndex % 8);
            int bit = (src[srcByte] >> srcBit) & 1;

            int dstByte = i / 8;
            int dstBit = 7 - (i % 8);
            result[dstByte] |= bit << dstBit;
        }
        return result;
    }
}
