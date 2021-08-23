package com.company;

import java.io.FileInputStream;
import java.io.IOException;

public class FileBreaker {

    private FileInputStream inputStream;
    private StringBuilder bitsSequence;
    private StringBuilder originalText;
    private StringBuilder brokenText;

    FileBreaker(FileInputStream stream) {
        this.inputStream = stream;
    }

    FileBreaker(StringBuilder bitsSequence) {
        this.bitsSequence = bitsSequence;
    }

    public String breakFile(int percents) {
        if ((inputStream != null) || (percents > 100)) {
            try {
                int bytesAvailable = inputStream.available();
                // Выводим поток битов в строку
                originalText = new StringBuilder();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < bytesAvailable; i++) {
                    char c = (char) inputStream.read();
                    originalText.append(c);
                    String symbolBits = HammingCodeManager.applyBitsMask(c, 8);
                    stringBuilder.append(symbolBits);
                }
                bitsSequence = stringBuilder;
                stringBuilder = breakBitsSequence(percents);
                /* Группируем биты обратно в байты */
                brokenText = new StringBuilder();
                for (int i = 0; i < bytesAvailable * 8; i += 8) {
                    String symbolBits = stringBuilder.substring(i, i + 8);
                    brokenText.append((char) Integer.parseInt(symbolBits, 2));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return brokenText.toString();
        } else {
            return null;
        }

    }

    public StringBuilder breakBitsSequence(int percent) {
        int interval = 100 / percent; // интервал инвертирования битов
        /* Инвертируем биты */
        for (int i = interval - 1; i < bitsSequence.length(); i += interval) {
            int bit = Character.getNumericValue(bitsSequence.toString().charAt(i));
            bit = (bit == 0)? 1 : 0; // инвертируем бит
            bitsSequence.replace(i, i + 1, String.valueOf(bit)); // подменяем исходный бит на инвертированный
        }
        return bitsSequence;
    }

    public String getOriginalText() {
        return originalText.toString();
    }
}