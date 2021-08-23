package com.company;

/** Класс для создания таблицы Трисемуса для работы с английскими прописными буквами*/
public class TrisemusTable {

    public static String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private char[][] table;

    /** Конструктор */
    TrisemusTable(String key) {
        table = new char[2][13];
        /* Генерируем таблицу трисемуса для сообщения key */
        StringBuilder builder = new StringBuilder(key);
        StringBuilder alphabetBuilder = new StringBuilder(ENGLISH_ALPHABET);
        int i = 0;
        while (i < builder.length()) {
            /* Удаляем из строки key повторяющиеся символы */
            int j = i + 1;
            int ind = 0;
            while ((ind = builder.indexOf(builder.substring(i,i + 1), j)) != -1) {
                builder.deleteCharAt(ind);
            }
            /* Удаляем текущий символ из алфавита */
            alphabetBuilder.deleteCharAt(alphabetBuilder.indexOf(
                    builder.substring(i,i + 1)));
            i++;
        }
        /* Объединяем builder и alphabetBuilder */
        builder.append(alphabetBuilder);
        if (builder.length() == ENGLISH_ALPHABET.length()) {
            // длина таблицы должна быть равна длине английского алфавита
            System.out.println(builder.substring(0, 13).toCharArray()); // TODO delete after
            System.out.println(builder.substring(13, 26).toCharArray()); // TODO delete after
            table[0] = builder.substring(0, 13).toCharArray(); // заполняем таблицу
            table[1] = builder.substring(13, 26).toCharArray();
        }
    }

    public char[][] getTable() {
        return table;
    }

    /** Возвращает символ из таблицы трисемуса */
    public char getChar(int i, int j) {
        if ((table[0] != null) && (table[1] != null)) {
            return table[i][j];
        } else {
            return '\0';
        }
    }
}
