package com.cryptography.messenger.algorithms.MacGuffin;

import com.cryptography.messenger.algorithms.interfaces.SymmetricCipher;

public class MacGuffin implements SymmetricCipher {
//    private static final


    @Override
    public void setRoundKeys(byte[] key) {

    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        return new byte[0];
    }

    @Override
    public int getBlockSize() {
        return 0;
    }
}
