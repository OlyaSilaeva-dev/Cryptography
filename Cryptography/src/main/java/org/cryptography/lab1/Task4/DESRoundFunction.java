package org.cryptography.lab1.Task4;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.Task1.BitsOrder;
import org.cryptography.lab1.Task1.RearrangingBits;
import org.cryptography.lab1.Task2.RoundFunction;

import java.util.Arrays;

@Slf4j
public class DESRoundFunction implements RoundFunction {
    private static final int blockSize = 8;
    private final BitsOrder bitsOrder;
    EBoxConversion eBoxConversion = new EBoxConversion();
    SBoxConversion sBoxConversion = new SBoxConversion();
    RearrangingBits rearrangingBits = new RearrangingBits();

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
        byte[] eBitSelectionOfInputBlock = eBoxConversion.eBitSelection(inputBlock, bitsOrder);
        byte[] xorBlocks = xor(eBitSelectionOfInputBlock, roundKey);
        byte[] sixBitsBoxes = splitIntoBoxes(xorBlocks);
        byte[] result = sBoxConversion.sBoxConversion(sixBitsBoxes, bitsOrder);
        result = RearrangingBits.rearrangingBits(result, pBox, bitsOrder, 1);
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
                    int bit = (input[byteIndex] >> (7 - bitOffset)) & 1;
                    value = (value << 1) | bit;
                }
                bitIndex++;
            }
            result[i] = (byte) value;
        }
        log.info("6-битовые блоки: {}", Arrays.toString(result));
        return result;
    }

    private byte[] xor(byte[] firstBlock, byte[] secondBlock) {
        if (firstBlock.length != secondBlock.length) {
            throw new IllegalArgumentException("Длины массивов должны совпадать");
        }
        byte[] result = new byte[firstBlock.length];
        for (int i = 0; i < firstBlock.length; i++) {
            result[i] = (byte) (firstBlock[i] ^ secondBlock[i]);
        }
        return result;
    }

    @Override
    public int getBlockSize() {
        return blockSize;
    }
}
