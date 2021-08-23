package com.company;

import java.io.FileInputStream;
import java.io.IOException;

public class HammingCodeManager {

    private FileInputStream inputStream;
    private StringBuilder originalText;
    private StringBuilder code;

    HammingCodeManager(FileInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public StringBuilder code(int informBytes) {
        StringBuilder hammingCode = new StringBuilder();
        originalText = new StringBuilder();
        try {
            while (inputStream.available() > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < informBytes; i++) {
                    char c = (char) inputStream.read();
                    originalText.append(c);
                    builder.append(applyBitsMask(c, 8)); // собираем informBytes байт информации
                }
                // высчитываем контрольные биты и добавляем их в StringBuilder
                hammingCode.append(addControlBits(builder.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hammingCode;
    }

    public StringBuilder decode(StringBuilder hammingCode, int informBytes) { // TODO 2. ОТЛАДИТЬ
        // рассчитываем количество контрольных бит k и формируем маску контрольных битов
        int k = 0;
        int ctrBitPos = 0;
        StringBuilder controlBitsMaskBuilder = new StringBuilder();
        // формируем маску контрольных бит
        for (int i = 0; i < 8 * informBytes; i++) { controlBitsMaskBuilder.append("0"); }

        while ((ctrBitPos = (int) (Math.pow(2, k) - 1)) < (8 * informBytes)) {
            k++;
            controlBitsMaskBuilder.insert(ctrBitPos, "1");
        }
        int controlBitsMask = Integer.parseInt(controlBitsMaskBuilder.toString(), 2); // маска контрольных битов

        // фромируем пакеты байт по pkgLen бит
        int pkgLen = (8 * informBytes + k);
        for (int basicPos = 0; basicPos < hammingCode.length(); basicPos += pkgLen) {
            String b = hammingCode.substring(basicPos,
                    basicPos + 8 * informBytes + k);
            String check = calculateControlBits(new StringBuilder(b)).toString();
            // переводим строку в число и выполняем операцию XOR
            int bits = Integer.parseInt(b, 2);
            int checkBits = Integer.parseInt(check, 2);
            int res = (bits ^ checkBits) & controlBitsMask; // XOR и маскирование

            String xorRes = applyBitsMask(res, (8 * informBytes + k));
            // ищем индекс ошибочного бита
            if (xorRes.contains("1")) {
                int brokenBitInd = -1; // todo переменная brokenBitInd работает неверно
                for (int i = 0; i < xorRes.length(); i++) {
                    if (xorRes.charAt(i) == '1') {
                        brokenBitInd += i + 1;
                    }
                }
                if (brokenBitInd < pkgLen) {
                    hammingCode.replace(basicPos + brokenBitInd, basicPos + brokenBitInd + 1,
                            (hammingCode.charAt(basicPos + brokenBitInd) == '0')? "1" : "0"); // инвертируем бит
                }
            }
        }
        /* Убираем контрольные биты */
        code = deleteControlBits(hammingCode, pkgLen); // todo работает правильно
        return code;
    }

    public static String applyBitsMask(int c, int mskBits) {
        String symbolBits = Integer.toBinaryString(c);
        if (symbolBits.length() < mskBits) {
            StringBuilder bitMskBuilder = new StringBuilder(symbolBits);
            for (int k = 0; k < mskBits - symbolBits.length(); k++) {
                bitMskBuilder.insert(0, "0");
            }
            symbolBits = bitMskBuilder.toString();
        }
        return symbolBits;
    }

    private String addControlBits(String src) {

        StringBuilder hammingCode = new StringBuilder(src);
        int k = 0;
        int pos;
        /* Добавляем контрольные биты */
        while ((pos = (int) Math.pow(2, k) - 1) < hammingCode.length()) {
            hammingCode.insert(pos, "0");
            k++;
        }
        hammingCode = calculateControlBits(hammingCode);

        return hammingCode.toString();
    }

    private StringBuilder deleteControlBits(StringBuilder code, int pkgLen) {
        StringBuilder decodedCode = new StringBuilder();
        int basicPos = 0;
        while (basicPos < code.length()) {
            // выделяем pkgLen бит
            StringBuilder pkg = new StringBuilder(code.substring(basicPos,
                    basicPos + pkgLen));
            // удаляем контрольные биты из пакета
            int k = 0;
            int pos;
            while ((pos = (int) Math.pow(2, k) - 1 - k) < pkgLen) {
                pkg.delete(pos, pos + 1);
                k++;
            }
            decodedCode.append(pkg);
            basicPos += pkgLen;
        }
        return decodedCode;
    }


    private StringBuilder calculateControlBits(StringBuilder hammingCode) {
        /* Вычисляем значения контрольных битов */
        int k = 0;
        int pos;
        while ((pos = (int) Math.pow(2, k) - 1) < hammingCode.length()) {
            hammingCode.replace(pos, pos + 1, "0"); // обнуляем контрольные биты перед проверкой
            int sum = 0;
            int currPos = pos;
            while (currPos < hammingCode.length()) {
                int basicPos = currPos;
                // Копируем pos битов, начиная с позиции pos
                while ((currPos < basicPos + pos + 1) && (currPos < hammingCode.length())) {
                    sum += Character.getNumericValue(hammingCode.charAt(currPos));
                    currPos++;
                }
                basicPos = currPos;
                // Пропускаем pos битов
                while ((currPos < basicPos + pos + 1) && (currPos < hammingCode.length())) {
                    currPos++;
                }
            }
            // вставляем посчитанный контрольный бит на позицию pos
            hammingCode.replace(pos, pos + 1, String.valueOf(sum % 2));
            k++;
        }
        return hammingCode;
    }

    public String getOriginalText() {
        return originalText.toString();
    }

    public String getDecodedText() {
        if (code != null) {
            /* Собираем биты в байты */
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < code.length(); i += 8) {
                builder.append((char) Integer.parseInt(code.substring(i, i + 8), 2));
            }
            return builder.toString();
        } else {
            return null;
        }
    }

}