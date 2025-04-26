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

    //Constructor to initialize the bettingSystem with a minimum bet and player balance.
    public BettingSystem(double minimumBet, double playerBalance) {
        this.minimumBet = minimumBet;
        this.playerBalance = playerBalance;
        this.currentBet = 0;
        this.doubledDown = false;
    }

    //Attempt to place a bet. If successful returns true, otherwise returns false.
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

    //Payout player if they won.
    public void payout(boolean isWin) {
        if (isWin) {
            playerBalance += currentBet * 2; //Will return twice the bet for the winnings. 
            System.out.println("You win! You earned: $" + currentBet);
        } 
        resetBet();  
    }

    //Allows the player to double down if they have the funds to do so.
    public void doubleDown() {
    if (playerBalance >= currentBet) {
        playerBalance -= currentBet;
        currentBet *= 2;
        System.out.println("You doubled down! Your new bet is: $" + currentBet);
    } else {
        throw new IllegalStateException("Not enough balance to double down.");
    }
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
    
    
    //Add funds to players balance.
    public void addFunds(double amount) {
        
    if (amount > 0) {
        playerBalance += amount;
        System.out.println("New balance: $" + playerBalance);
    } else {
        System.out.println("Invalid amount! Must be greater than 0.");
    }
}
    

}
