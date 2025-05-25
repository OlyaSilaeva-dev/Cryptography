package com.cryptography.messenger.algorithms;

import com.cryptography.messenger.algorithms.RC5.RC5;
import com.cryptography.messenger.algorithms.RC5.RC5KeyExpansion;
import com.cryptography.messenger.algorithms.enums.EncryptionMode;
import com.cryptography.messenger.algorithms.enums.PaddingMode;
import com.cryptography.messenger.algorithms.symmetricCipherContext.SymmetricCipherContext;

public class TestRC5 {
    public static void main(String[] args) {
        byte[] key = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F
        };
        SymmetricCipherContext cipher = new SymmetricCipherContext(
                new RC5(32, 12,16),
                key,
                EncryptionMode.ECB,
                PaddingMode.Zeros,
                new byte[0]
        );

        byte[] ciphertext = cipher.encrypt("Hello".getBytes());
        byte[] plaintext = cipher.decrypt(ciphertext);
        System.out.println(new String(plaintext));
//
////
//        RC5KeyExpansion keyExpansion = new RC5KeyExpansion(32, 12);
//        byte[][] result = keyExpansion.keyExpansion(key);
//
//        for (int i = 0; i < result.length; i++) {
//            long value = bytesToLong(result[i]);
//            System.out.printf("S[%02d] = 0x%08X%n", i, value);  // Верхний регистр, с ведущими нулями
//        }
//    }
//
//
//    private static long bytesToLong(byte[] bytes) {
//        long result = 0;
//        for (int i = 0; i < bytes.length; i++) {
//            result |= (bytes[i] & 0xFFL) << (8 * i);  // little-endian
//        }
//        return result;
    }
}
