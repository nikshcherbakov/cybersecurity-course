package com.company;

public class Main {

    public static void main(String[] args) {
        FeistelNetwork network = new FeistelNetwork();
        String message = "I WILL GO TO THE SHOP AND YOU STAY AT HOME";
        System.out.println("Message: " + message);
        String cipher = network.encrypt(message, 9678);
        System.out.println("Cipher: " + cipher);
        String messageDecrypted = network.decrypt(cipher, 3320);
        System.out.println("Decrypted Message: " + messageDecrypted);
    }
}
