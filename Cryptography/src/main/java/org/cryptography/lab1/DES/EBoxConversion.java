package org.cryptography.lab1.DES;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.enums.BitsOrder;

import static org.cryptography.lab1.Main.printBytes;
import static org.cryptography.lab1.rearrangingBits.RearrangingBits.rearrangingBits;

@Slf4j
public class EBoxConversion {

    private static final int[] eBitSelectionTable = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    public static byte[] eBitSelection(byte[] data, BitsOrder bitsOrder) {
        return rearrangingBits(data, eBitSelectionTable, bitsOrder, 1);
    }
}
