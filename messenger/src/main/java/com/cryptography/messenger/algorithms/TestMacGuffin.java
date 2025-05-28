package com.cryptography.messenger.algorithms;

import com.cryptography.messenger.algorithms.utils.Utils;

public class TestMacGuffin {
    public static void main(String[] args) {

    }

    private static void printBytes(byte[] res) {
        for (int i = 0; i < res.length; i++) {
            System.out.print(Integer.toBinaryString(res[i] & 0xff) + " ");
        }
        System.out.println();
    }
}
