package org.cryptography.lab1.DES;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.enums.BitsOrder;
import org.cryptography.lab1.rearrangingBits.RearrangingBits;
import org.cryptography.lab1.interfaces.RoundFunction;

import java.awt.*;
import java.util.Arrays;

import static org.cryptography.lab1.DES.DESUtils.toUnsignedByte;
import static org.cryptography.lab1.DES.DESUtils.xor;
import static org.cryptography.lab1.DES.EBoxConversion.*;

@Slf4j
public class DESRoundFunction implements RoundFunction {
    private static final int blockSize = 8;
    private final BitsOrder bitsOrder;

    private static final int[] pBox = {
            16,  7, 20, 21,
            29, 12, 28, 17,
             1, 15, 23, 26,
             5, 18, 31, 10,
             2,  8, 24, 14,
            32, 27,  3,  9,
            19, 13, 30,  6,
            22, 11,  4, 25
    };

    public DESRoundFunction(BitsOrder bitsOrder) {
        this.bitsOrder = bitsOrder;
    }

    @Override
    public byte[] roundConversion(byte[] inputBlock, byte[] roundKey) {
//        log.info("Round conversion started with inputBlock = {}, roundKey = {}", inputBlock, roundKey);
        byte[] eBitSelectionOfInputBlock = EBoxConversion.eBitSelection(inputBlock, bitsOrder);
//        log.info("After eBitSelection = {}", eBitSelectionOfInputBlock);
        byte[] xorBlocks = xor(eBitSelectionOfInputBlock, roundKey);
//        log.info("After xorBlocks = {}", xorBlocks);
        byte[] sixBitsBoxes = splitIntoBoxes(xorBlocks);
//        log.info("After splitIntoBoxes = {}", sixBitsBoxes);
        byte[] result = SBoxConversion.sBoxConversion(sixBitsBoxes, bitsOrder);
//        log.info("After sBoxConversion = {}", result);
        result = RearrangingBits.rearrangingBits(result, pBox, bitsOrder, 1);
        log.info("Round result: {}", result);
        return result;
    }

    public byte[] splitIntoBoxes(byte[] input) {
        int bitCount = input.length * 8;
        int resultSize = (bitCount + 5) / 6;
        byte[] result = new byte[resultSize];

        int bitIndex = 0;
        for (int i = 0; i < resultSize; i++) {
            int value = 0;
            for (int j = 0; j < 6; j++) {
                int byteIndex = bitIndex / 8;
                int bitOffset = bitIndex % 8;
                if (byteIndex < input.length) {
                    int currentByte = toUnsignedByte(input[byteIndex]);
                    int bit = (currentByte >>> (7 - bitOffset)) & 1;
                    value = (value << 1) | bit;
                }
                bitIndex++;
            }
            result[i] = (byte) value;
        }
        return result;
    }

    @Override
    public int getBlockSize() {
        return blockSize;
    }
}
