package pdc_assignment;

import java.util.Scanner;

/**
 *
 * @author raghav
 */

public class BlackjackGame {

    private Deck deck;
    private Player player;
    private Dealer dealer;
    private CheckInput checkInput;
    private BettingSystem bettingSystem;

    public BlackjackGame(String playerName, double initialBalance) {
        deck = new Deck();
        player = new Player(playerName);  
        dealer = new Dealer();
        checkInput = new CheckInput();
        bettingSystem = new BettingSystem(0.0, initialBalance); 
    }

    public void playGame() {
        while (true) {
            resetGame();

            System.out.println("Welcome to Blackjack, " + player.getName() + "!\n");
            System.out.println("Your current betting balance: $" + bettingSystem.getPlayerBalance());

      
            double betAmount = getBetAmount();
            bettingSystem.placeBet(betAmount);  
            
   
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());

            System.out.println(player);
            dealer.showFirstCard();

            playerTurn();

            if (player.calculateHandScore() > 21) {
                
                FileIO.updateStats(player.getName(), false, false);
                bettingSystem.payout(false); 
              
            } else {
                dealerTurn();
                determineWinner();
            }

            deck.reShuffle();

            if (!askToPlayAgain()) {
                showFinalStats();
                System.out.println("Thanks for playing! Final balance: $" + bettingSystem.getPlayerBalance());
                break;
            }
        }
    }

    private double getBetAmount() {
        double betAmount = 0;
        while (true) {
            System.out.print("Enter your bet: ");
            betAmount = checkInput.getBetAmount("");
            if (betAmount <= 0) {
                System.out.println("Bet must be greater than 0.");
            } else if (betAmount > bettingSystem.getPlayerBalance()) {
                System.out.println("You don't have enough balance to place that bet.");
            } else {
                break;
            }
        }
        return betAmount;
    }

    private void resetGame() {
        player.clearHand();
        dealer.clearHand();
        deck.reShuffle();
    }

    private void playerTurn() {
        while (player.calculateHandScore() < 21) {
            CheckInput.Choice choice = checkInput.getChoice("Hit (H) or Stand (S)? ");
            
            if (choice == CheckInput.Choice.H) {
                player.addCard(deck.dealCard());
                System.out.println(player);
            } else {
                break;
            }
        }

        if (player.calculateHandScore() > 21) {
            System.out.println("You busted! Dealer wins.");
            bettingSystem.payout(false);  
        }
    }

    private void dealerTurn() {
        System.out.println("\nDealer reveals their hand:");
        System.out.println(dealer);

        while (dealer.calculateHandScore() < 17) {
            dealer.addCard(deck.dealCard());
            System.out.println(dealer);
        }
    }

    private void determineWinner() {
        int playerScore = player.calculateHandScore();
        int dealerScore = dealer.calculateHandScore();

        if (dealerScore > 21 || playerScore > dealerScore) {
            System.out.println("Congratulations! You win!");
            FileIO.updateStats(player.getName(), true, false);
            bettingSystem.payout(true);  
        } else if (playerScore < dealerScore) {
            System.out.println("Dealer wins. Better luck next time.");
            FileIO.updateStats(player.getName(), false, false);
            bettingSystem.payout(false);  
        } else {
            System.out.println("It's a tie!");
            FileIO.updateStats(player.getName(), false, true);
        }
    }
    
    private boolean askToPlayAgain() {
        if (bettingSystem.getPlayerBalance() > 0) {
            return checkInput.playAgainResponse("Do you want to play again? (Y/N): ");
        }

        System.out.println("You have run out of money!");
        boolean addMoreMoney = checkInput.playAgainResponse("Would you like to add more money? (Y/N): ");

        if (addMoreMoney) {
            double newAmount = checkInput.getBetAmount("Enter the new amount you want to add: ");
            bettingSystem.addFunds(newAmount);  
            return true;
        } else {
            return false; 
        }
    }
    
    private void showFinalStats(){
        
        String stats = FileIO.getPlayerStats(player.getName());
        
        if(stats != null){
            System.out.println("\nFinal Stats for " + player.getName());
            String[] parts = stats.split(" ");
            System.out.println("Wins = " + parts[1]);
            System.out.println("Losses = " + parts[2]);
            System.out.println("Ties = " + parts[3]);
        }
        
        else{
            System.out.println("\nStats were not found for " + player.getName());
        }
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CheckInput checkInput = new CheckInput();
        
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        double initialBalance = checkInput.getBetAmount("Enter the amount you have: ");

        BlackjackGame game = new BlackjackGame(playerName, initialBalance);
        game.playGame();
        scanner.close();
    }
}
