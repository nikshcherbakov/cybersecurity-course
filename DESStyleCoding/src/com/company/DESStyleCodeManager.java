package com.company;

public class DESStyleCodeManager {

    final int ROUNDS = 16; // количество раундов

    /** Функция для кодирования строки message */
    public String code(String message, int key) {
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            String bin = Integer.toBinaryString(c | 256).substring(1, 9);

        }
        return null; // stub
    }

    /** Функция для декодирования шифротекста code */
    public String decode(String code, int key) {
        return null; // STUB
    }

    /** Функция, осуществляющая перевод бинарного кода в символьный */
    private String binCodeToString(String binCode) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < binCode.length(); i += 8) {
            String b = binCode.substring(i, i + 8);
            builder.append((char) Integer.parseInt(b, 2));
        }
        return builder.toString();
    }
}
