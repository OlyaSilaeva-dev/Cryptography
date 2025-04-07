package org.cryptography.lab1.rearrangingBits;

import org.cryptography.lab1.enums.BitsOrder;

public class RearrangingBits {
    public static byte[] rearrangingBits(byte[] data, int[] pBox, BitsOrder bitsOrder, int indexFirstBit) {
        byte[] result = new byte[(pBox.length + 7) / 8];

        for (int i = 0; i < pBox.length; i++) {
            int bitIndex = (pBox[i] - indexFirstBit);
            int byteIndex = bitIndex / 8;
            int bitPos = bitIndex % 8;

            if (byteIndex < 0 || byteIndex >= data.length) {
                throw new IllegalArgumentException("pBox index out of range.");
            }

            boolean curBit;


            if (bitsOrder == BitsOrder.LSB_FIRST) {
                curBit = ((data[byteIndex] >> bitPos) & 1) == 1;
            } else {
                curBit = ((data[byteIndex] >> (7 - bitPos)) & 1) == 1;
            }

            int destByteIndex = i / 8;
            int destBitPos = (bitsOrder == BitsOrder.LSB_FIRST) ? (i % 8) : 7 - (i % 8);

            if (curBit) {
                result[destByteIndex] |= (byte) (1 << destBitPos);
            }
        }
        return result;
    }
}
