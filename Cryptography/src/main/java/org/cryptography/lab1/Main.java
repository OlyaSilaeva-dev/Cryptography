package org.cryptography.lab1;

import org.cryptography.lab1.DES.DES;
import org.cryptography.lab1.enums.BitsOrder;
import org.cryptography.lab1.rearrangingBits.RearrangingBits;
import org.junit.platform.commons.annotation.Testable;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class Main {
    public static void main(String[] args) {
        byte[] data1 = "Hello World".getBytes(StandardCharsets.UTF_8);
        System.out.println(bytesToHex(data1));
        DES d = new DES();
        d.setRoundKeys(generateKey());
        byte[] encryptText = d.encrypt(data1);
        System.out.println(bytesToHex(encryptText));
        byte[] decryptText = d.decrypt(data1);
        System.out.println(bytesToHex(decryptText));

//        byte [] data = {(byte) 0b10011011};
//        int[] pBox = {7, 6, 5, 4, 3, 2, 1, 8};
//        System.out.println(Integer.toBinaryString(data[0] & 0xFF));
//        byte[] res = RearrangingBits.rearrangingBits(data, pBox, BitsOrder.LSB_FIRST, 1);
//        System.out.println(Integer.toBinaryString(res[0] & 0xFF));
    }

    private static void printResultByUTF8(byte[] data) {
        String decryptedText = new String(data, StandardCharsets.UTF_8);
        System.out.println("Дешифрованный текст: " + decryptedText);
    }

    public static byte[] generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[7]; // 7 байт = 56 бит
        random.nextBytes(key);
        return key;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}