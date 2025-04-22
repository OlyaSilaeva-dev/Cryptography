package org.cryptography.lab1.DES;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.enums.BitsOrder;
import org.cryptography.lab1.interfaces.KeyExpansion;
import org.cryptography.lab1.rearrangingBits.RearrangingBits;

@Slf4j
public final class DESKeyExpansion implements KeyExpansion {

    static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9, 1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 27, 19, 11, 3,
            60, 52, 44, 36, 63, 55, 47, 39,
            31, 23, 15, 7, 62, 54, 46, 38,
            30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 28, 20, 12, 4
    };

    static final int[] SHIFTS = {
            1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1
    };

    static final int[] PC2 = {
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
    };

    private static String byteArrayToBinaryString(byte[] byteArray) {
        StringBuilder binaryString = new StringBuilder();
        for (byte b : byteArray) {
            String binaryByte = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binaryString.append(binaryByte).append(" ");
        }
        return binaryString.toString().trim();
    }

    @Override
    public byte[][] keyExpansion(byte[] key) {
        if (key.length != 8) {
            throw new IllegalArgumentException("DES key must be 64 bits (8 bytes) long");
        }

        byte[] key56 = RearrangingBits.rearrangingBits(key, PC1, BitsOrder.MSB_FIRST, 1); // 64 -> 56 бит

        // Разбиваем на C и D
        long keyBits = toLong(key56); // 56 бит в long
        int C = (int) ((keyBits >>> 28) & 0x0FFFFFFF);
        int D = (int) (keyBits & 0x0FFFFFFF);

        byte[][] roundKeys = new byte[16][6];

        for (int i = 0; i < 16; i++) {
            C = leftShift28(C, SHIFTS[i]);
            D = leftShift28(D, SHIFTS[i]);

            long combined = (((long) C) << 28) | (D & 0x0FFFFFFF);
            byte[] cdBytes = toByteArray(combined); // 56 бит в 7 байт

            roundKeys[i] = RearrangingBits.rearrangingBits(cdBytes, PC2, BitsOrder.MSB_FIRST, 1); // 56 -> 48 бит
        }

        return roundKeys;
    }

    private static int leftShift28(int value, int shift) {
        return ((value << shift) | (value >>> (28 - shift))) & 0x0FFFFFFF;
    }

    private static long toLong(byte[] bytes) {
        long result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);
        }
        return result;
    }

    private static byte[] toByteArray(long value) {
        byte[] result = new byte[7];
        for (int i = 0; i < 7; i++) {
            result[i] = (byte) ((value >>> ((6 - i) * 8)) & 0xFF);
        }
        return result;
    }
}
