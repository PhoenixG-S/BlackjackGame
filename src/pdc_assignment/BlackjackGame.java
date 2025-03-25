/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

/**
 *
 * @author raghavbhalla
 */

public class BettingSystem {

    private double minimumBet;
    private double playerBalance;
    private double currentBet;
    private boolean doubledDown;


    public BettingSystem(double minimumBet, double playerBalance) {
        this.minimumBet = minimumBet;
        this.playerBalance = playerBalance;
        this.currentBet = 0;
        this.doubledDown = false;
    }

    public boolean placeBet(double amount) {
        if (amount < minimumBet) {
            System.out.println("Bet must be at least " + minimumBet);
            return false;
        }
        if (amount > playerBalance) {
            System.out.println("Insufficient balance.");
            return false;
        }
        currentBet = amount;
        playerBalance -= amount;
        return true;
    }


    public void payout(boolean isWin) {
        if (isWin) {
            playerBalance += currentBet * 2;  
            System.out.println("You win! You earned: $" + currentBet);
        } 
        resetBet();  
    }


    public void doubleDown() {
        if (doubledDown) {
            throw new IllegalStateException("You cannot double down twice in the same round.");
        }
        if (currentBet * 2 > playerBalance) {
            throw new IllegalArgumentException("Insufficient balance to double down.");
        }
        playerBalance -= currentBet;
        currentBet *= 2;
        doubledDown = true;
        System.out.println("You doubled down! Your new bet is: $" + currentBet);
    }


    public void resetBet() {
        currentBet = 0;
        doubledDown = false;
    }


    public double getPlayerBalance() {
        return playerBalance;
    }


    public double getCurrentBet() {
        return currentBet;
    }


    public double getMinimumBet() {
        return minimumBet;
    }

 
    public boolean hasDoubledDown() {
        return doubledDown;
    }
    
    public void addFunds(double amount) {
    if (amount > 0) {
        playerBalance += amount;
        System.out.println("New balance: $" + playerBalance);
    } else {
        System.out.println("Invalid amount! Must be greater than 0.");
    }
}

}
