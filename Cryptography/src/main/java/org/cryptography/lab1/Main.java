package org.cryptography.lab1;

import org.cryptography.lab1.Task1.BitsOrder;

import java.util.ArrayList;
import java.util.List;

import static org.cryptography.lab1.Task1.RearrangingBits.rearrangingBits;

public class Main {
    public static void main(String[] args) {
        byte [] data = {(byte) 0b10011011};
        System.out.println(Integer.toBinaryString(data[0] & 0xFF));
//        boolean firstBit = ((data[0] & 1) == 1);
//        boolean sixthBit = ((data[0] >> 5) & 1) == 1;
//        System.out.println((firstBit ? 1: 0) + " " + (sixthBit ? 1: 0));
//        int row = (firstBit ? 1 : 0) | (sixthBit ? 2 : 0);
//        byte byte1 = (byte) ((data[0] >> 1) & 15);
//        System.out.println(Integer.toBinaryString(byte1 & 0xFF));
//        int col = firstBit ? ((data[0] >> 1) & 15) : 0;
//        System.out.println("row: " + row);
//        System.out.println("col: " + col);

        boolean firstBit = ((data[0] >> 7) & 1) == 1;
        boolean sixthBit = ((data[0] >> 2) & 1) == 1;
        System.out.println((firstBit ? 1: 0) + " " + (sixthBit ? 1: 0));
        int row = (firstBit ? 2 : 0) | (sixthBit ? 1 : 0);
        int col = firstBit ? ((data[0] >> 3) & 7) : 0;
        System.out.println("row: " + Integer.toBinaryString(row));
        System.out.println("col: " + Integer.toBinaryString(col));

    }
}