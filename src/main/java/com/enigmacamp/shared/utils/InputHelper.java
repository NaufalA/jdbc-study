package com.enigmacamp.shared.utils;

import java.util.Scanner;

public class InputHelper {
    public static String inputString(Scanner scanner, boolean allowEmpty) {
        String input = "";
        boolean inputValid;
        do {
            try {
                input = scanner.nextLine();
                if (!allowEmpty && input.equals("")) {
                    System.out.println("Invalid Input");
                    inputValid = false;
                    StringHelper.printInputPrompt("");
                    continue;
                }
                inputValid = true;
            } catch (Exception e) {
                System.out.println("Invalid Input");
                inputValid = false;
                StringHelper.printInputPrompt("");
            }
        } while (!inputValid);

        return input;
    }

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
                StringHelper.printInputPrompt("");
            }
        } while (!inputValid);

        return input;
    }

    public static Integer inputInt(Scanner scanner, int min, int max) {
        int input = -1;
        boolean inputValid;
        do {
            try {
                String inputString = scanner.nextLine();
                if (!inputString.equals("")) {
                    input = Integer.parseInt(inputString);
                }
                if (input < min || input > max) {
                    System.out.println("Invalid Input");
                    inputValid = false;
                    StringHelper.printInputPrompt("");
                    continue;
                }
                inputValid = true;
            } catch (Exception e) {
                System.out.println("Invalid Input");
                inputValid = false;
                StringHelper.printInputPrompt("");
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

    public static Boolean confirmation(String message, Scanner scanner) {
        StringHelper.printInputPrompt(message + " (y/n)");
        String restartInput;
        do {
            restartInput = InputHelper.inputString(scanner, false).toLowerCase();
            switch (restartInput) {
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    System.out.println("Invalid Input");
                    StringHelper.printInputPrompt("");
                    break;
            }
        } while (true);
    }
}
