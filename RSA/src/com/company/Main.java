package com.company;

public class Main {

    public static void main(String[] args) {
        int P = 5;
        int Q = 7;
        int Ko = 5;
        User A = new User(P, Q, Ko);
        User B = new User(P, Q, Ko);
        String message = "Hello, world!";
        boolean isSent = A.sendCipher(B, message);
        if (isSent) {
            B.printCipherReceived();
            String messageDecrypted = B.decryptCipherReceived();
            System.out.print("Decrypted Text: ");
            System.out.println(messageDecrypted);
        }
    }
}
