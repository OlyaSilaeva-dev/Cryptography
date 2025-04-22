package org.cryptography.lab1.DEAL;

import org.cryptography.lab1.DES.DES;
import org.cryptography.lab1.enums.EncryptionMode;
import org.cryptography.lab1.enums.PaddingMode;
import org.cryptography.lab1.symmetricCipherContext.SymmetricCipherContext;

public class DESCipherForDEAL {
    public static final byte[] deskey =
            new byte[]{
                    (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67,
                    (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef
            };

    public static SymmetricCipherContext createSymmetricCipherContext(byte[] key) {
        return new SymmetricCipherContext(
                new DES(),
                key,
                EncryptionMode.ECB,
                PaddingMode.PKCS7,
                new byte[0]
        );
    }
}
