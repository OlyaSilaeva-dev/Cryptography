import org.cryptography.lab1.DES.DESKeyExpansion;
import org.cryptography.lab1.interfaces.KeyExpansion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DESKeyExpansionTest {

    private final KeyExpansion keyExpansion = new DESKeyExpansion();

    @Test
    void testKeyExpansionReturns16KeysOf48BitsEach() {
        byte[] key = new byte[]{0x13, 0x34, 0x57, 0x79, (byte) 0x9B, (byte) 0xBC, (byte) 0xDF, (byte) 0xF1};

        byte[][] roundKeys = keyExpansion.keyExpansion(key);

        assertEquals(16, roundKeys.length, "Должно быть 16 раундовых ключей");

        for (int i = 0; i < 16; i++) {
            assertNotNull(roundKeys[i], "Ключ " + (i + 1) + " не должен быть null");
            assertEquals(6, roundKeys[i].length, "Ключ " + (i + 1) + " должен быть 48 бит (6 байт)");
        }
    }

    @Test
    void testKeyExpansionThrowsExceptionOnInvalidKeyLength() {
        byte[] invalidKey = new byte[]{0x01, 0x02, 0x03, 0x04}; // длина < 8

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            keyExpansion.keyExpansion(invalidKey);
        });

        assertEquals("DES key must be 56 bits (7 bytes) long", exception.getMessage());
    }

    @Test
    void testKeyExpansionIsDeterministic() {
        byte[] key = new byte[]{0x13, 0x34, 0x57, 0x79, (byte) 0x9B, (byte) 0xBC, (byte) 0xDF, (byte) 0xF1};

        byte[][] first = keyExpansion.keyExpansion(key);
        byte[][] second = keyExpansion.keyExpansion(key);

        for (int i = 0; i < 16; i++) {
            assertArrayEquals(first[i], second[i], "Раундовый ключ №" + (i + 1) + "должен быть одинаковым");
        }
    }

    @Test
    void testRoundKeysMatchKnownReferenceValues() {
        byte[] key = new byte[]{0x13, 0x34, 0x57, 0x79, (byte) 0x9B, (byte) 0xBC, (byte) 0xDF, (byte) 0xF1};

        byte[][] roundKeys = keyExpansion.keyExpansion(key);

        // Эталонные значения из твоего лога
        byte[] expectedK1 = new byte[]{0x1B, 0x02, (byte) 0xEF, (byte) 0xFC, 0x70, 0x72};
        byte[] expectedK2 = new byte[]{0x79, 0x6A, (byte) 0xCB, 0x24, 0x26, 0x5C};

        assertArrayEquals(expectedK1, roundKeys[0], "K1 не совпадает с эталоном");
        assertArrayEquals(expectedK2, roundKeys[1], "K2 не совпадает с эталоном");
    }
}
