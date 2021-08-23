package com.company;

import javafx.beans.binding.StringBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static int compareToOriginal(String broken, String original) {
        if ((broken != null) && (original != null)) {
            int count = 0;
            for (int i = 0; i < original.length(); i++){
                if (broken.charAt(i) != original.charAt(i)) {
                    count++; }
            }
            return count;
        } else {
            return -1;
        }
    }

    public static void main(String[] args) throws IOException {

        List<Integer> percents = new ArrayList<>();
        List<Integer> brokenChars = new ArrayList<>();

        String originalText = null;
        /* *************** Без кодирования Хэмминга *******************/
        /* Собираем данные для построения графика */
        int p = 1; // проценты сломанных битов
        while (p <= 100) {
            FileInputStream inputStream = new FileInputStream("C:\\Users\\pc\\Desktop\\Msg.txt");
            FileBreaker breaker = new FileBreaker(inputStream);
            String brokenText = breaker.breakFile(p);
            originalText = breaker.getOriginalText();
            int bc = compareToOriginal(brokenText, originalText);
            percents.add(p);
            brokenChars.add(bc);
            inputStream.close();
            p = (p <= 20)? (p + 1) : (p + 10);
        }

        /* Записываем полученные данные в файл */
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\pc\\Desktop\\DataWithoutHamming.txt");
        for (int percent : percents) {
            outputStream.write(String.valueOf(percent).getBytes());
            outputStream.write(" ".getBytes());
        }
        outputStream.write(0b1101); // символ переноса строки в формате .txt
        outputStream.write(0b1010);
        for (int brokenChar : brokenChars) {
            outputStream.write(String.valueOf(brokenChar).getBytes());
            outputStream.write(" ".getBytes());
        }

        /* *********** С кодированием Хэмминга *************/
        percents.clear();
        brokenChars.clear();
        p = 1;

        while (p < 100) {
            FileInputStream inputStream = new FileInputStream("C:\\Users\\pc\\Desktop\\Msg.txt");
            HammingCodeManager manager = new HammingCodeManager(inputStream);
            StringBuilder hammingCode = manager.code(2);
            FileBreaker breaker = new FileBreaker(hammingCode);
            hammingCode = breaker.breakBitsSequence(p);
            manager.decode(hammingCode, 2);
            String decodedCode = manager.getDecodedText();
            int bc = compareToOriginal(decodedCode, originalText);
            percents.add(p);
            brokenChars.add(bc);
            inputStream.close();
            p = (p <= 20)? (p + 1) : (p + 10);
        }

        /* Записываем полученные данные в файл */
        outputStream = new FileOutputStream("C:\\Users\\pc\\Desktop\\DataWithHamming.txt");
        for (int percent : percents) {
            outputStream.write(String.valueOf(percent).getBytes());
            outputStream.write(" ".getBytes());
        }
        outputStream.write(0b1101); // символ переноса строки в формате .txt
        outputStream.write(0b1010);
        for (int brokenChar : brokenChars) {
            outputStream.write(String.valueOf(brokenChar).getBytes());
            outputStream.write(" ".getBytes());
        }
    }
}