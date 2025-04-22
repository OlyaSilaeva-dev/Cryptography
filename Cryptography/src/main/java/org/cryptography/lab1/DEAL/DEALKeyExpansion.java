package org.cryptography.lab1.DEAL;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.enums.DEALModes;
import org.cryptography.lab1.interfaces.KeyExpansion;
import org.cryptography.lab1.symmetricCipherContext.SymmetricCipherContext;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.cryptography.lab1.utils.Utils.xor;

@Slf4j
public class DEALKeyExpansion implements KeyExpansion {
    private final DEALModes dealMode;
    SymmetricCipherContext des = DESCipherForDEAL.createSymmetricCipherContext(DESCipherForDEAL.deskey);
    public DEALKeyExpansion(DEALModes dealMode) {
        this.dealMode = dealMode;
    }

    @Override
    public byte[][] keyExpansion(byte[] key) {
        int keyBlockCount;
        int roundKeyCount;

        switch (dealMode) {
            case DEAL_128 -> {
                keyBlockCount = 2;
                roundKeyCount = 6;
            }
            case DEAL_192 -> {
                keyBlockCount = 3;
                roundKeyCount = 6;
            }
            case DEAL_256 -> {
                keyBlockCount = 4;
                roundKeyCount = 8;
            }
            default -> throw new IllegalArgumentException("Unsupported DEAL mode");
        }

        if (key.length != keyBlockCount * 8) {
            throw new IllegalArgumentException("Invalid key length for " + dealMode);
        }

        byte[][] Ki = new byte[keyBlockCount][8];
        for (int i = 0; i < keyBlockCount; i++) {
            Ki[i] = Arrays.copyOfRange(key, i * 8, (i + 1) * 8);
        }
        

        byte[][] RK = new byte[roundKeyCount][8];

        if (dealMode == DEALModes.DEAL_128) {
            RK[0] = des.encrypt(Ki[0]);
            RK[1] = des.encrypt(xor(Ki[1], RK[0]));
            RK[2] = des.encrypt(xor(xor(Ki[0], toBytes(1)), RK[1]));
            RK[3] = des.encrypt(xor(xor(Ki[1], toBytes(2)), RK[2]));
            RK[4] = des.encrypt(xor(xor(Ki[0], toBytes(4)), RK[3]));
            RK[5] = des.encrypt(xor(xor(Ki[1], toBytes(8)), RK[4]));
        } else if (dealMode == DEALModes.DEAL_192) {
            RK[0] = des.encrypt(Ki[0]);
            RK[1] = des.encrypt(xor(Ki[1], RK[0]));
            RK[2] = des.encrypt(xor(Ki[0], RK[1]));
            RK[3] = des.encrypt(xor(xor(Ki[1], toBytes(1)), RK[2]));
            RK[4] = des.encrypt(xor(xor(Ki[0], toBytes(2)), RK[3]));
            RK[5] = des.encrypt(xor(xor(Ki[1], toBytes(4)), RK[4]));
        } else {
            RK[0] = des.encrypt(Ki[0]);
            RK[1] = des.encrypt(xor(Ki[1], RK[0]));
            RK[2] = des.encrypt(xor(Ki[2], RK[1]));
            RK[3] = des.encrypt(xor(Ki[3], RK[2]));
            RK[4] = des.encrypt(xor(xor(Ki[0], toBytes(1)), RK[3]));
            RK[5] = des.encrypt(xor(xor(Ki[1], toBytes(2)), RK[4]));
            RK[6] = des.encrypt(xor(xor(Ki[2], toBytes(4)), RK[5]));
            RK[7] = des.encrypt(xor(xor(Ki[3], toBytes(8)), RK[6]));
        }
        log.info("Round keys: ");
        printBytes(RK);
        return RK;
    }

    private byte[] toBytes(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(value);
        return buffer.array();
    }

    private void printBytes(byte[][] bytes) {
        for (byte[] b : bytes) {
            for (byte b1 : b) {
                System.out.print(String.format("%02X ", b1));
            }
            System.out.println();
        }
    }
}
