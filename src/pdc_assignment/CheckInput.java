package pdc_assignment;
import java.util.Scanner;

public class CheckInput {

    private final Scanner scanner = new Scanner(System.in);

    public enum Choice {
        H, S;
    }

    public double getBetAmount(String message) {
        double betAmount = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print(message); 
            try {
                betAmount = scanner.nextDouble();  

          
                if (betAmount <= 0) {
                    System.out.println("Bet must be greater than 0.");
                } else {
                    validInput = true;  
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next();  
            }
        }

        return betAmount; 
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
