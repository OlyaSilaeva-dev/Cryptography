package org.cryptography.lab1.DEAL;

import org.cryptography.lab1.enums.DEALModes;
import org.cryptography.lab1.feistelNet.FeistelNet;

public class DEAL extends FeistelNet {
    DEALModes mode;

    public DEAL(DEALModes dealMode) {
        super(new DEALKeyExpansion(dealMode), new DEALRoundFunction());
        this.mode = dealMode;
    }
}
