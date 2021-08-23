package com.company;

public class FeistelNetwork {

    private final int KEY_LENGTH = 14; // длина ключа в битах
    private final int ROUNDS = 2; // количество раундов
    private final int BLOCK_LENGTH = 8;

    /** Функция для кодирования сообщения message с помощью ключа key */
    public String encrypt(String message, int key) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i); // считываем 8 бит информации

            /* Разделяем на левый (L) и правый (R) подблок по 4 бита */
            int L = c >> 4;
            int R = c & 0x0F;
            for (int round = 0; round < ROUNDS; round++) {
                /* Ячейка Фейстеля */
                int X = f(L, key, round);
                X = X ^ R;
                R = L;
                L = X;
            }
            /* На последней итерации меняем местами X и R */
            int X = R;
            R = L;
            L = X;
            builder.append((char) ((L << (BLOCK_LENGTH / 2)) | R));
        }
        return builder.toString();
    }

    /** Функция для декодирования шифротекста cipher с помощью ключа key */
    public String decrypt(String cipher, int key) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cipher.length(); i++) {
            char c = cipher.charAt(i); // считываем 8 бит информации

            /* Разделяем на левый (L) и правый (R) подблок по 4 бита */
            int L = c >> 4;
            int R = c & 0x0F;
            for (int round = ROUNDS - 1; round >= 0; round--) {
                /* Ячейка Фейстеля */
                int X = f(L, key, round);
                X = X ^ R;
                R = L;
                L = X;
            }
            /* На последней итерации меняем местами X и R */
            int X = R;
            R = L;
            L = X;
            builder.append((char) ((L << (BLOCK_LENGTH / 2)) | R));
        }
        return builder.toString();
    }

    private int f(int l, int k, int round) {
        if (round >= 0) {
            if (round >= KEY_LENGTH / 2) {
                round = KEY_LENGTH / 2 - 1;
            }
        } else {
            round = 0;
        }
        /* Берем round-ную пару бит в ключе k */
        int msk = 0x3000 >> (round * 2); // сдвигаем маску 0b11000000000000 на round * 2 позиций вправо
        int offset = (k & msk) >> (KEY_LENGTH - round * 2 - 2);

        /* Осуществляем циклический сдвиг l на offset позиций влево */
        int lNew = l << offset;
        int msk1 = (0x0F << (BLOCK_LENGTH / 2)) & (0x0F << offset);
        return (lNew | ((lNew & msk1) >> (BLOCK_LENGTH / 2))) & 0x0F;
    }

}
