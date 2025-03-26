package org.cryptography.lab1;

import org.cryptography.lab1.Task1.BitsOrder;

import java.util.ArrayList;
import java.util.List;

import static org.cryptography.lab1.Task1.Task1.rearrangingBits;

public class Main {
    public static void main(String[] args) {
        List<Byte> data = new ArrayList<>();
        data.add((byte) 0b00111111);
        System.out.println(Integer.toBinaryString(data.getFirst() & 0xFF));

        int[] pBox = {4, 2, 1, 3, 7, 5, 8, 6};

        byte[] arr = rearrangingBits(data, pBox, BitsOrder.LSB_FIRST, 1);
        for (byte b : arr) {
            System.out.println(Integer.toBinaryString(b & 0xFF));
        }
    }
}