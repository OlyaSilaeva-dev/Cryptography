import org.cryptography.lab1.Task1.BitsOrder;
import org.cryptography.lab1.Task4.SBoxConversion;
import org.junit.Test;
import static org.junit.Assert.*;

public class SBoxConversionTest {

    private final SBoxConversion converter = new SBoxConversion();

//    @Test
//    public void testSBoxConversion_LSBFirst() {
//        byte[] input = {
//                (byte) 0b10101010, (byte) 0b11001100, (byte) 0b11110000, (byte) 0b00001111,
//                (byte) 0b00110011, (byte) 0b01010101, (byte) 0b10011001, (byte) 0b01100110
//        };
//
//        byte[] expected = {
//                0b00000111, 0b00001111, 0b00000010, 0b00001100,
//                0b00001010, 0b00000010, 0b00000011, 0b00000001
//        };
//
//        byte[] result = converter.sBoxConversion(input, BitsOrder.LSB_FIRST);
//        assertArrayEquals(expected, result);
//    }

//    @Test
//    public void testSBoxConversion_MSBFirst() {
//        byte[] input = {
//                (byte) 0b10101010, (byte) 0b11001100, (byte) 0b11110000, (byte) 0b00001111,
//                (byte) 0b00110011, (byte) 0b01010101, (byte) 0b10011001, (byte) 0b01100110
//        };
//
//        byte[] expected = {
//                0b00001110, 0b00001101, 0b00000110, 0b00001100,
//                0b00000111, 0b00000011, 0b00000010, 0b00000010
//        };
//
//        byte[] result = converter.sBoxConversion(input, BitsOrder.MSB_FIRST);
//        assertArrayEquals(expected, result);
//    }

    @Test
    public void testSBoxConversion_AllZeros() {
        byte[] input = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] expected = { 14, 15, 10, 7, 2, 12, 4, 13};

        byte[] result = converter.sBoxConversion(input, BitsOrder.LSB_FIRST);
        assertArrayEquals(expected, result);
    }

//    @Test
//    public void testSBoxConversion_AllOnes() {
//        byte[] input = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
//        byte[] expected = { 6, 3, 2, 3, 15, , ,  };
//
//        byte[] result = converter.sBoxConversion(input, BitsOrder.LSB_FIRST);
//        assertArrayEquals(expected, result);
//    }

    @Test(expected = IllegalArgumentException.class)
    public void testSBoxConversion_InvalidLength() {
        byte[] input = { (byte) 0b10101010, (byte) 0b11001100 }; // Длина меньше 8
        converter.sBoxConversion(input, BitsOrder.LSB_FIRST);
    }
}
