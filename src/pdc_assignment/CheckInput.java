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
            String input = scanner.nextLine().trim().toUpperCase(); 

        try {
            return Choice.valueOf(input);  
            } catch (IllegalArgumentException e) {
            System.out.println("Invalid input! Please enter 'H' (Hit) or 'S' (Stand).");
        }
    }
}


    public boolean playAgainResponse(String message) {
        while (true) {
            System.out.print(message);
            String response = scanner.nextLine().trim().toUpperCase(); 

        if (response.equals("Y")) {
            return true;
        } else if (response.equals("N")) {
            return false;
        } else {
            System.out.println("Invalid input! Please enter 'Y' for Yes or 'N' for No.");
        }
      }
    }
}
