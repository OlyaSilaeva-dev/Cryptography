package org.cryptography.lab1.DES;

import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.enums.BitsOrder;
import org.cryptography.lab1.interfaces.RoundFunction;
import org.cryptography.lab1.feistelGrid.FeistelGrid;
import org.cryptography.lab1.rearrangingBits.RearrangingBits;

@Slf4j
public class DES extends FeistelGrid {

    private static final BitsOrder bitsOrder = BitsOrder.MSB_FIRST;

    private static final int[] IP = {
            58,	50,	42,	34,	26,	18,	10,	2,	60,	52,	44,	36,	28,	20,	12,	4,
            62,	54,	46,	38,	30,	22,	14,	6,	64,	56,	48,	40,	32,	24,	16,	8,
            57,	49,	41,	33,	25,	17,	9,	1,	59,	51,	43,	35,	27,	19,	11,	3,
            61,	53,	45,	37,	29,	21,	13,	5,	63,	55,	47,	39,	31,	23,	15,	7
    };

    private static final int[] IP_1 = {
            40,	8,	48,	16,	56,	24,	64,	32,	39,	7,	47,	15, 55,	23,	63,	31,
            38,	6,  46,	14,	54,	22,	62,	30,	37,	5,	45,	13,	53,	21,	61,	29,
            36,	4,	44,	12,	52,	20,	60,	28,	35,	3,	43,	11,	51,	19,	59,	27,
            34,	2,	42,	10,	50,	18,	58,	26,	33,	1,  41,	9,	49,	17,	57,	25
    };

    public static final RoundFunction DESRoundFunction = new DESRoundFunction(bitsOrder);
    public static final DESKeyExpansion DESKeyExpansion = new DESKeyExpansion();

    public DES() {
        super(DESKeyExpansion, DESRoundFunction);
    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        log.info("Encrypt plaintext");
        byte[] result = RearrangingBits.rearrangingBits(plaintext, IP, bitsOrder, 1);
        result = super.encrypt(result);
        result = RearrangingBits.rearrangingBits(result, IP_1, bitsOrder, 1);
        return result;
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        log.info("Decrypt ciphertext");
        byte[] result = RearrangingBits.rearrangingBits(ciphertext, IP, bitsOrder, 1);
        result = super.decrypt(result);
        result = RearrangingBits.rearrangingBits(result, IP_1, bitsOrder, 1);
        return result;
    }
}
