package com.cryptography.messenger.algorithms;

import com.cryptography.messenger.algorithms.MacGuffin.MacGuffin;
import com.cryptography.messenger.algorithms.RC5.RC5;
import com.cryptography.messenger.algorithms.enums.EncryptionMode;
import com.cryptography.messenger.algorithms.enums.PaddingMode;
import com.cryptography.messenger.algorithms.symmetricCipherContext.SymmetricCipherContext;
import com.cryptography.messenger.algorithms.utils.Utils;

public class TestMacGuffin {
    public static void main(String[] args) {

        byte[] key = new byte[] {
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F
        };
        SymmetricCipherContext cipher = new SymmetricCipherContext(
                new MacGuffin(),
                key,
                EncryptionMode.ECB,
                PaddingMode.Zeros,
                new byte[0]
        );

        byte[] ciphertext = cipher.encrypt("Hello".getBytes());
        byte[] plaintext = cipher.decrypt(ciphertext);
        System.out.println(new String(plaintext));
    }

    private static void printBytes(byte[] res) {
        for (int i = 0; i < res.length; i++) {
            System.out.print(Integer.toBinaryString(res[i] & 0xff) + " ");
        }
        System.out.println();
    }
}
