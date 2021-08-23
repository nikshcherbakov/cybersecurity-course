package com.company;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private static String originalText = "FINDING SOMEWHERE AFFORDABLE TO LIVE IN BRITAIN IS HARD. SOME PARTS OF THE COUNTRY ARE CHEAPER THAN OTHERS, OF COURSE, BUT THE COST OF RENTING A HOME IS HORRENDOUS, ESPECIALLY IN LONDON AND THE SOUTH. NORMALLY, THE ONLY ANSWER IS TO SHARE A HOUSE OR A FLAT: YOU GET A ROOM OF YOUR OWN, BUT YOU HAVE TO SHARE THE KITCHEN AND BATHROOM.";

    private static String textToDecode = "THERE ARE DIFFERENT KINDS OF ANIMALS ON OUR PLANET, AND ALL OF THEM ARE VERY IMPORTANT FOR IT";

    private static String messageFilePath = "C:\\Users\\pc\\YandexDisk\\Учеба\\" +
            "4 курс\\8 семестр\\ОЗИ\\Лаб. раб. 3\\Message.txt";

    private static String codedFilePath = "C:\\Users\\pc\\YandexDisk\\Учеба\\" +
            "4 курс\\8 семестр\\ОЗИ\\Лаб. раб. 3\\Coded Message.txt";

    private static String pathOfFileToDecode = "C:\\Users\\pc\\YandexDisk\\Учеба\\" +
            "4 курс\\8 семестр\\ОЗИ\\Лаб. раб. 3\\MessageToBeDecoded.txt";

    private static String decodedMessagePath = "C:\\Users\\pc\\YandexDisk\\Учеба\\" +
            "4 курс\\8 семестр\\ОЗИ\\Лаб. раб. 3\\Decoded Message.txt";


    public static void main(String[] args) {
        TrisemusHelper helper = new TrisemusHelper(codedFilePath);
        TrisemusHelper helper1 = new TrisemusHelper(pathOfFileToDecode);
        helper.codeToFile(originalText);
        helper1.codeToFile(textToDecode);

        writeMessageToFile(messageFilePath, originalText);
        hideSymbols(messageFilePath, 100);
        String messageCoded = readMessageFromFile(codedFilePath);
        String messageWithStars = readMessageFromFile(messageFilePath);
        System.out.print("Message with stars: ");
        System.out.println(messageWithStars);
        char[][] tableRestored = TrisemusHelper.restoreTrisemusTable(messageWithStars, messageCoded);
        System.out.print("Message decoded: ");
        System.out.println(TrisemusHelper.decode(pathOfFileToDecode, tableRestored));
        TrisemusHelper.decodeToFile(decodedMessagePath, pathOfFileToDecode, tableRestored);
    }

    /** Прячет symbols символов звездочками ('*') в сообщении с директорией path */
    public static boolean hideSymbols(String path, int symbols) {
        /* Открываем файл и считываем зашифрованное сообщение */
        StringBuilder codeBuilder = new StringBuilder();
        try {
            FileInputStream inputStream = new FileInputStream(path);
            while (inputStream.available() > 0) {
                codeBuilder.append((char) inputStream.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if ((symbols > 0) && (symbols <= codeBuilder.length())) {
            /* Заменяем symbols символов в зашифрованном сообщении слуайным образом */
            int count = 0;
            int ind;
            ArrayList<Integer> list = new ArrayList<>();
            while (count < symbols) {
                /* Генерируем уникальные индексы */
                do {
                    ind = (int) ((double) codeBuilder.length() * Math.random());
                } while (list.contains(ind));
                list.add(ind);
                /* Заменяем символ с индексом ind звездочкой */
                codeBuilder.replace(ind, ind + 1, "*");
                count++;
            }
            /* Открываем файл для записи */
            try {
                FileOutputStream outputStream = new FileOutputStream(path);
                outputStream.write(codeBuilder.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /** Считывает сообщение из файла */
    public static String readMessageFromFile(String path) {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            StringBuilder builder = new StringBuilder();
            while (inputStream.available() > 0) {
                builder.append((char) inputStream.read());
            }
            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Записывает сообщение в файл */
    public static boolean writeMessageToFile(String path, String message) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            outputStream.write(message.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
