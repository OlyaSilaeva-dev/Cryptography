package org.cryptography.lab1.Task2;

public interface RoundFunction {
    /**
     * Выполняет раундовое преобразование входного блока с использованием раундового ключа.
     *
     * @param inputBlock Входной блок данных (массив байтов).
     * @param roundKey Раундовый ключ (массив байтов).
     * @return Выходной блок данных (массив байтов).
     */
    byte[] roundConversion(byte[] inputBlock, byte[] roundKey);
}
