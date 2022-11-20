package com.enigmacamp.shared.utils;

public class StringHelper {
    public static void printHeader(String title) {
        System.out.println("========================================");
        System.out.println(title);
        System.out.println("----------------------------------------");
    }

    public static void printInputPrompt(String prompt) {
        System.out.print(prompt+"\n> ");
    }
}
