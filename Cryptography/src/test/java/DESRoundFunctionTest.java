import org.cryptography.lab1.DES.DESKeyExpansion;
import org.cryptography.lab1.DES.EBoxConversion;
import org.cryptography.lab1.DES.SBoxConversion;
import org.cryptography.lab1.enums.BitsOrder;
import org.cryptography.lab1.rearrangingBits.RearrangingBits;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.cryptography.lab1.DES.DES.DESKeyExpansion;
import static org.cryptography.lab1.DES.DES.DESRoundFunction;
import static org.cryptography.lab1.DES.SBoxConversion.sBoxConversion;
import static org.junit.jupiter.api.Assertions.*;

public class DESRoundFunctionTest {

    @Test
    void testRearrangingBits_shouldApplyPermutationCorrectly() {
        byte[] input = {(byte) 0b11001010}; // Bit pattern: 11001010
        int[] pBox = {1, 2, 3, 4, 5, 6, 7, 8}; // identity
        byte[] result = RearrangingBits.rearrangingBits(input, pBox, BitsOrder.MSB_FIRST, 1);

        assertEquals(1, result.length);
        assertEquals((byte) 0b11001010, result[0]);
    }

    @Test
    void testEBitSelection_shouldExpandTo48Bits() {
        byte[] input = new byte[]{(byte) 0b00001111, (byte) 0b11110000, 0x00, 0x00}; // 32 бита
        byte[] result = EBoxConversion.eBitSelection(input, BitsOrder.MSB_FIRST);

        assertEquals(6, result.length); // 48 бит = 6 байт
    }

//    @Test
//    void testSplitIntoBoxes_shouldSplitToSixBitsChunks() {
//        byte[] input = {(byte) 0b111111, (byte) 0b000000}; // 12 бит
//        byte[] result = DESRoundFunction.splitIntoBoxes(input);
//
//        assertEquals(2, result.length);
//        assertEquals(0b111111, result[0] & 0x3F);
//        assertEquals(0b000000, result[1] & 0x3F);
//    }

    @Test
    void testSBoxConversion_shouldReturnCorrectSize() {
        byte[] input = new byte[8];
        Arrays.fill(input, (byte) 0b101010); // заполняем 6-битными значениями

        byte[] result = SBoxConversion.sBoxConversion(input, BitsOrder.MSB_FIRST);
        assertEquals(4, result.length); // 8 блоков по 4 бита = 32 бита = 4 байта
    }

    @Test
    void testKeyExpansion_shouldReturn16RoundKeys() {
        byte[] key = new byte[]{0x13, 0x34, 0x57, 0x79, (byte) 0x9B, (byte) 0xBC, (byte) 0xDF};

        DESKeyExpansion desKeyExpansion = new DESKeyExpansion();
        byte[][] roundKeys = desKeyExpansion.keyExpansion(key);

        assertEquals(16, roundKeys.length);
//        for (byte[] rk : roundKeys) {
//            assertEquals(6, rk.length, "Each round key should be 48 bits");
//        }
    }

    @Test
    void testRoundConversion_shouldReturn32Bits() {
        byte[] input = new byte[4]; // 32 бита
        byte[] roundKey = new byte[6]; // 48 бит

        byte[] result = DESRoundFunction.roundConversion(input, roundKey);

        assertEquals(4, result.length); // 32 бита
    }

}

