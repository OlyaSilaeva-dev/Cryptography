import lombok.extern.slf4j.Slf4j;
import org.cryptography.lab1.Task1.BitsOrder;
import org.cryptography.lab1.Task1.RearrangingBits;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class RearrangingBitsTest {

    @Test
    public void testRearrange_LSB_FIRST_Simple() {
        byte[] data = { (byte) 0b11001100 };  // Один байт: 11001100
        int[] pBox = { 7, 6, 5, 4, 3, 2, 1, 0 }; // Инвертируем порядок битов
        byte[] expected = { (byte) 0b00110011 }; // Должно быть 00110011

        byte[] result = RearrangingBits.rearrangingBits(data, pBox, BitsOrder.LSB_FIRST, 0);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testRearrange_LSB_FIRST_MultiByte() {
        byte[] data = { (byte) 0b10101010, (byte) 0b11001100 };
        int[] pBox = { 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
        byte[] expected = { (byte) 0b00110011, (byte) 0b01010101 };

        byte[] result = RearrangingBits.rearrangingBits(data, pBox, BitsOrder.LSB_FIRST, 0);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testRearrange_MSB_FIRST_Simple() {
        byte[] data = { (byte) 0b11001100 };
        int[] pBox = { 0, 1, 2, 3, 4, 5, 6, 7 };
        byte[] expected = { (byte) 0b11001100 };

        byte[] result = RearrangingBits.rearrangingBits(data, pBox, BitsOrder.MSB_FIRST, 0);

        for (byte b : result) {
            System.out.print(Integer.toBinaryString(b) + " ");
        }

        assertArrayEquals(expected, result);
    }

    @Test
    public void testRearrange_MSB_FIRST_Reverse() {
        byte[] data = { (byte) 0b11001100 };
        int[] pBox = { 7, 6, 5, 4, 3, 2, 1, 0 };
        byte[] expected = { (byte) 0b00110011 };

        byte[] result = RearrangingBits.rearrangingBits(data, pBox, BitsOrder.MSB_FIRST, 0);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testRearrange_WithOffset() {
        byte[] data = { (byte) 0b10101010, (byte) 0b11001100 };
        int[] pBox = { 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 5, 6, 7 };
        byte[] expected = { (byte) 0b11001100, (byte) 0b10101010 };

        byte[] result = RearrangingBits.rearrangingBits(data, pBox, BitsOrder.LSB_FIRST, 0);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testRearrange_InvalidPBox() {
        byte[] data = { (byte) 0b10101010 };
        int[] pBox = { 8, 9, 10, 11, 12, 13, 14, 15 }; // Выходит за пределы

        assertThrows(IllegalArgumentException.class, () ->
                RearrangingBits.rearrangingBits(data, pBox, BitsOrder.LSB_FIRST, 0)
        );
    }

    @Test
    public void testRearrange_eBoxSelection() {
        byte[] data = { (byte) 0b10101010, (byte) 0b01101111 };
        int[] pBox = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 10, 11, 12, 13}; // Исправленные индексы
        byte[] expected = { (byte) 0b10101010, (byte) 0b01101111, (byte) 0b00001011}; // 8-битные значения

        byte[] result = RearrangingBits.rearrangingBits(data, pBox, BitsOrder.LSB_FIRST, 0);

        for (byte b : result) {
            System.out.print(Integer.toBinaryString(b & 0xFF) + " ");
        }

        assertArrayEquals(expected, result);
    }

}
