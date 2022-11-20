package com.enigmacamp.shared.utils;

import java.util.Scanner;

public class InputHelper {
    public static Integer inputInt(Scanner scanner) {
        int input = -1;
        boolean inputValid;
        do {
            try {
                String inputString = scanner.nextLine();
                if (!inputString.equals("")) {
                    input = Integer.parseInt(inputString);
                }
                inputValid = true;
            } catch (Exception e) {
                System.out.println("Invalid Input");
                inputValid = false;
            }
        } while (!inputValid);

        return input;
    }

    public static Float inputFloat(Scanner scanner) {
        float input = -1f;
        boolean inputValid;
        do {
            try {
                String inputString = scanner.nextLine();
                if (!inputString.equals("")) {
                    input = Float.parseFloat(inputString);
                }
                inputValid = true;
            } catch (Exception e) {
                System.out.println("Invalid Input");
                inputValid = false;
            }
        } while (!inputValid);

        return input;
    }
}
