package com.company;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TrisemusHelper {

    private String TRISEMUS_TABLE_KEY = "INFORMATION";

    private String codePath;
    private TrisemusTable trisemusTable;

    /** Конструктор класса */
    TrisemusHelper(String codePath) {
        this.codePath = codePath;
        trisemusTable = new TrisemusTable(TRISEMUS_TABLE_KEY);
    }

    /** Кодирует сообщение */
    public String code(String message) {
        StringBuilder codeBuilder = new StringBuilder();
        /* Кодируем сообщение message по таблице Трисемуса */
        for (int i = 0; i < message.length(); i++) {
            if (TrisemusTable.ENGLISH_ALPHABET.indexOf(message.charAt(i)) != -1) {
                // Если в английском алфавите присутствует текущий символ, то кодируем его
                for (int r = 0; r < 2; r++) {
                    for (int c = 0; c < 13; c++) {
                        // Ищем текущий символ в таблице
                        if (trisemusTable.getChar(r, c) == message.charAt(i)) {
                            // Нашли символ => кодируем его
                            char charCoded = (r == 0)? trisemusTable.getChar(1, c) :
                                    trisemusTable.getChar(0, c);
                            codeBuilder.append(charCoded);
                            break;
                        }
                    }
                }
            } else {
                // если нет - переносим текущий символ в выходной билдер без кодирования
                codeBuilder.append(message.charAt(i));
            }
        }
        return codeBuilder.toString();

    }

    /** Кодирует сообщение в файл */
    public boolean codeToFile(String message) {
        if (codePath != null) {
            String messageCoded = code(message);
            /* Пытаемся открыть файл для записи */
            try {
                FileOutputStream outputStream = new FileOutputStream(codePath);
                outputStream.write(messageCoded.getBytes()); // записываем закодированное сообщение в файл
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /** Декодирует сообщение в соответствии с восстановленной таблицей Трисемуса */
    public static String decode(String codePath, char[][] tableRestored) {
        /* Открываем файл для чтения */
        StringBuilder originalTextBuilder = new StringBuilder();
        try {
            StringBuilder codeBuilder = new StringBuilder();
            /* Считываем файл */
            FileInputStream inputStream = new FileInputStream(codePath);
            while (inputStream.available() > 0) {
                codeBuilder.append((char) inputStream.read());
            }
            /* Декодируем */
            String codeString = codeBuilder.toString();
            for (int i = 0; i < codeString.length(); i++) {
                char c = codeString.charAt(i);
                if (TrisemusTable.ENGLISH_ALPHABET.indexOf(c) != -1) {
                    // Если текущий символ - буква английского алфивита, то раскодируем ее по таблице
                    originalTextBuilder.append("*");
                    for (int k = 0; k < tableRestored[1].length; k++) {
                        if (tableRestored[1][k] == c) {
                            originalTextBuilder.replace(i, i + 1,
                                    String.valueOf(tableRestored[0][k]));
                        }
                    }
                }
                else {
                    // если это не буква английского алфавита - просто переносим ее
                    originalTextBuilder.append(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return originalTextBuilder.toString();
    }
    /** Декодирует сообщение в файл */
    public static boolean decodeToFile(String decodePath, String codePath, char[][] tableRestored) {
        /* Открываем файл для записи */
        try {
            FileOutputStream outputStream = new FileOutputStream(decodePath);
            String decodedText = decode(codePath, tableRestored);
            outputStream.write(decodedText.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Восставнавливает таблицу трисемуса по исходному и закодированному сообщениям */
    public static char[][] restoreTrisemusTable(String message, String messageCoded) {
        char[][] table = new char[2][];
        table[0] = null;
        table[1] = null;

        if (message.length() == messageCoded.length()) {
            StringBuilder originalSymbols = new StringBuilder(); // символы, занесенные в таблицу
            StringBuilder codedSymbols = new StringBuilder();
            for (int i = 0; i < message.length(); i++) {
                char c = message.charAt(i);
                if ((originalSymbols.toString().indexOf(c) == -1) && (TrisemusTable.ENGLISH_ALPHABET.indexOf(c) != -1)) {
                    // Если текущий символ еще не занесен в таблицу - заносим его
                    originalSymbols.append(c);
                    codedSymbols.append(messageCoded.charAt(i));
                }
            }
            System.out.println("Trisimus Table Restored:"); // TODO delete after
            System.out.println(originalSymbols.toString().toCharArray()); // TODO delete after
            System.out.println(codedSymbols.toString().toCharArray()); // TODO delete after
            System.out.println(); // TODO delete after
            table[0] = originalSymbols.toString().toCharArray();
            table[1] = codedSymbols.toString().toCharArray();
            return table;
        } else {
            return table;
        }
    }
}
