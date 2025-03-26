package org.cryptography.lab1.Task1;

import java.util.List;

public class Task1 {
    public static byte[] rearrangingBits (List<Byte> data, int[] pBox,BitsOrder bitsOrder, int indexFirstBit) {
        byte[] result = new byte[data.size()];

        if (bitsOrder == BitsOrder.MSB_FIRST) {
            for (int i = 0; i < pBox.length; i++) {
                int iByte = (pBox[i] - indexFirstBit) >> 3;
                int iBit = (pBox[i] - indexFirstBit) % 8;

                boolean bit = (data.get(iByte) & (1 << (7 - iBit))) != 0;

                int resByte = i >> 3;
                int resBit = i % 8;

                if (bit) {
                    result[resByte] |= (byte) (1 << (7 - resBit));
                }

                System.out.println(Integer.toBinaryString(result[0] & 0xFF));
            }
        } else {
            for (int i = pBox.length - 1; i >= 0; i--) {
                int iByte = (pBox[i] - indexFirstBit) >> 3;
                int iBit = (pBox[i] - indexFirstBit) % 8;

                boolean bit = (data.get(iByte) & (1 << iBit)) != 0;

                int resByte = i >> 3;
                int resBit = i % 8;

                if (bit) {
                    result[resByte] |= (byte) (1 << resBit);
                }

                System.out.println(Integer.toBinaryString(result[0] & 0xFF));
            }
        }
        return result;
    }
}
