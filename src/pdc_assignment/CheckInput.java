/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;
import java.util.Scanner;

public class CheckInput {
    public enum Choice { H, S, D }

    public double getBetAmount(String prompt) {
        Scanner scanner = new Scanner(System.in);
        double amount = 0;
        while (true) {
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount >= 0) return amount;
                System.out.println("Amount must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a number.");
            }
        }
    }

    public Choice getChoice(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            switch (input) {
                case "H": return Choice.H;
                case "S": return Choice.S;
                default: System.out.println("Invalid input. Enter H or S.");
            }
        }
    }

    public Choice getDoubleDownChoice(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            switch (input) {
                case "H": return Choice.H;
                case "S": return Choice.S;
                case "D": return Choice.D;
                default: System.out.println("Invalid input. Enter H, S, or D.");
            }
        }
    }

    public boolean playAgainResponse(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
            System.out.println("Invalid input. Enter Y or N.");
        }
    }
}

