package com.cryptography.messenger;

import com.cryptography.messenger.algorithms.RC5.RC5KeyExpansion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RC5FinalSKeyTableTest {

    @Test
    public void testFinalExpandedSTable_RC5_32_12_16() {
        int w = 32;
        int r = 12;
        byte[] key = new byte[16];

        for (int i = 0; i < 16; i++) {
            key[i] = (byte) i;
        }

        RC5KeyExpansion expander = new RC5KeyExpansion(w, r);
        byte[][] S_bytes = expander.keyExpansion(key);

        // Ожидаемые финальные значения S после перемешивания (из RFC 2040 Appendix A)
        int[] expectedFinalS = {
                0xB7B3B18B, 0x32BA5D3B, 0xF3E2C92E, 0xA0A25CE3,
                0x49C26B1D, 0x3F84D5B5, 0x8FCB57C5, 0x6876B9EC,
                0xF295C03B, 0xCBF3A6F4, 0x23A96A96, 0xD12A66D2,
                0x7B5FBEE0, 0xF9058C1F, 0xA3D0B1A1, 0xD37E9906,
                0x5BE0C5D6, 0x09E860FD, 0x1DEBAE9D, 0x28D5A491,
                0x43DF2677, 0xF2C2F598, 0xC67F5531, 0x702DAA63,
                0x57FDE8F9, 0x9D71DEB9
        };

        assertEquals(expectedFinalS.length, S_bytes.length, "S length mismatch");

        for (int i = 0; i < expectedFinalS.length; i++) {
            long actual = bytesToLong(S_bytes[i]);
            long expected = Integer.toUnsignedLong(expectedFinalS[i]);
            assertEquals(expected, actual, "Mismatch at final S[" + i + "]");
        }
    }

    private long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result |= (bytes[i] & 0xFFL) << (8 * i);  // little-endian
        }
        return result;
    }
}
