package pdc_assignment;
import java.util.Scanner;

public class CheckInput {

    private final Scanner scanner = new Scanner(System.in);

    public enum Choice {
        H, S;
    }
    
    public double getBetAmount(String message) {
        double amount = 0;
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.println("Invalid input! Please enter a positive amount (number).");
                } else {
                    return amount; 
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid amount (number).");
            }
        }
    }

   


    public Choice getChoice(String message) {
        while (true) {
            System.out.print(message);  

            try {
                return Choice.valueOf(scanner.next().toUpperCase());  
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input! Please enter 'H' (Hit) or 'S' (Stand).");
            }
        }
    }

    public boolean playAgainResponse(String message) {
        while (true) {
            System.out.print(message);  
            String response = scanner.next().trim().toUpperCase();  

            switch (response) {
                case "Y":
                    return true;
                case "N":
                    return false;
                default:
                    System.out.println("Invalid input! Please enter 'Y' for Yes or 'N' for No.");
            }
        }
    }
}
