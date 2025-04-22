package org.cryptography.lab1.DEAL;

import org.cryptography.lab1.enums.DEALModes;
import org.cryptography.lab1.feistelGrid.FeistelGrid;

public class DEAL extends FeistelGrid {
    DEALModes mode;

    public DEAL(DEALModes dealMode) {
        super(new DEALKeyExpansion(dealMode), new DEALRoundFunction());
        this.mode = dealMode;
    }
}
