package org.cryptography.lab1.Task4;

import org.cryptography.lab1.Task1.BitsOrder;
import org.cryptography.lab1.Task2.KeyExpansion;
import org.cryptography.lab1.Task2.RoundFunction;
import org.cryptography.lab1.Task3.FeistelGrid;

public class DES extends FeistelGrid {

    private static final RoundFunction DESRoundFunction = new DESRoundFunction(BitsOrder.LSB_FIRST);

    public DES(KeyExpansion keyExpansion) {
        super(keyExpansion, DESRoundFunction);
    }
}
