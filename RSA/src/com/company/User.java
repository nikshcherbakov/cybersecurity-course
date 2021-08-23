package com.company;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final int BLOCK_LENGTH = 16;
    private List<Integer> cipherReceived; // буфер для хранения принятого шифротекста
    private int N;
    private int Ko = 0;
    private int Kc = 0;

    User(int p, int q, int Ko) {
        this.N = p * q;
        int phi = (p-1) * (q-1); // функция Эйлера
        if ((Ko > 1) && (Ko < phi)) {
            this.Ko = Ko;
            this.Kc = calculateKc(Ko, phi);
        }
    }

    /** Функция для передачи сообщения другому пользователю */
    public boolean sendCipher(User user, String message) {
        if ((user != this) && (user != null)) { // передача сообщений самому себе запрещена
            int N = user.getN(); // запрашиваем у пользователя пару значений (N,Ko)
            int Ko = user.getKo();
            /* Разбиваем сообщение на блоки по 4 бита и шифруем их*/
            if ((N >= BLOCK_LENGTH) && (Ko != 0)) {
                for (int i = 0; i < message.length(); i++) {
                    /* Шифруем символ */
                    char c = message.charAt(i);
                    int m1 = (0xF0 & c) >> 4; // первая половина символа
                    int m2 = (0x0F & c); // вторая половина символа
                    int c1 = (int) Math.pow(m1, Ko) % N; // шифровка
                    int c2 = (int) Math.pow(m2, Ko) % N;
                    boolean isReceived = user.receiveCipher(c1, c2);
                    if (!isReceived) {return false;}
                }
                return true;
            }
        }
        // передача сообщения запрещена
        return false;
    }

    /** Организуем прием сообщений */
    public boolean receiveCipher(int c1, int c2) {
        if (cipherReceived == null) {
            cipherReceived = new ArrayList<>();
        }
        cipherReceived.add(c1);
        cipherReceived.add(c2);
        return true;
    }

    /** Метод для расшифровки полученного шифра, храняещегося в cipherReceived */
    public String decryptCipherReceived() {
        /* Расшифровка шифротекста  */
        StringBuilder builder = new StringBuilder();
        if (Kc > 0) {
            List<Integer> encryptedBits = new ArrayList<>();
            for (int ci : cipherReceived) {
                int mi = (int) Math.pow(ci, Kc) % N;
                encryptedBits.add(mi);
            }
            for (int i = 0; i < encryptedBits.size(); i += 2) {
                char c = (char) ((encryptedBits.get(i) << 4) | encryptedBits.get(i+1));
                builder.append(c);
            }
            return builder.toString();
        }
        return null;
    }

    /** Функция отправляет пару чисел (N, Ko) для получателя */
    public int getN() {
        return N;
    }

    public int getKo() {
        return Ko;
    }

    /** Функция для расчета Kc */
    private int calculateKc(int a, int N) {
        /* Расширенный алгоритм Евклида */
        int x1 = 0;
        int x2 = 1;
        int x3 = N;
        int y1 = 1;
        int y2 = 0;
        int y3 = a;

        while (x3 != 1) {
            int q = x3 / y3; // q - целая часть от деления
            int z1 = x1 - q * y1;
            int z2 = x2 - q * y2;
            int z3 = x3 - q * y3;
            x1 = y1;
            x2 = y2;
            x3 = y3;
            y1 = z1;
            y2 = z2;
            y3 = z3;
        }
        return x1;
    }

    /** Функция выводит шифрограмму из буфера на терминал */
    public void printCipherReceived() {
        System.out.print("Cipher: ");
        for (int i : cipherReceived) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }

}
