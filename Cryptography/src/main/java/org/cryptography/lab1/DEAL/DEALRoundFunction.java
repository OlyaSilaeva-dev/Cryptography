package org.cryptography.lab1.DEAL;

import org.cryptography.lab1.interfaces.RoundFunction;
import org.cryptography.lab1.symmetricCipherContext.SymmetricCipherContext;

public class DEALRoundFunction implements RoundFunction {
    private static final int blockSize = 16;

    @Override
    public byte[] roundConversion(byte[] inputBlock, byte[] roundKey) {
        SymmetricCipherContext symmetricCipherContext = DESCipherForDEAL.createSymmetricCipherContext(roundKey);
        return symmetricCipherContext.encrypt(inputBlock);
    }

    @Override
    public int getBlockSize() {
        return blockSize;
    }
}
