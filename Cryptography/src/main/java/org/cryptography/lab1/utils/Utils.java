package org.cryptography.lab1.utils;

public class Utils {
    public static int toUnsignedByte(byte b) {
        return b & 0xFF;
    }

    public static byte[] xor(byte[] firstBlock, byte[] secondBlock) {
        int maxLength = Math.max(firstBlock.length, secondBlock.length);

        firstBlock = padWithZeros(firstBlock, maxLength);
        secondBlock = padWithZeros(secondBlock, maxLength);

        byte[] result = new byte[firstBlock.length];
        for (int i = 0; i < firstBlock.length; i++) {
            result[i] = (byte) (firstBlock[i] ^ secondBlock[i]);
        }
        return result;
    }

    private static byte[] padWithZeros(byte[] input, int length) {
        if (input.length >= length) return input;

        byte[] padded = new byte[length];
        System.arraycopy(input, 0, padded,length - input.length, input.length);
        return padded;
    }

}
