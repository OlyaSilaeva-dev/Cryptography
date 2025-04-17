package org.cryptography.lab1.DES;

public class DESUtils {
    public static int toUnsignedByte(byte b) {
        return b & 0xFF;
    }

    public static byte[] xor(byte[] firstBlock, byte[] secondBlock) {
        if (firstBlock.length != secondBlock.length) {
            throw new IllegalArgumentException("Длины массивов должны совпадать");
        }
        byte[] result = new byte[firstBlock.length];
        for (int i = 0; i < firstBlock.length; i++) {
            result[i] = (byte) (firstBlock[i] ^ secondBlock[i]);
        }
        return result;
    }

}
