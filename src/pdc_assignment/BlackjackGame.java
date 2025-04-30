/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pdc_assignment;


/**
 *
 * @author raghav
 */

import java.util.Scanner;

public class BlackjackGame {

    private Deck deck;
    private Player player;
    private Dealer dealer;
    private CheckInput checkInput;
    private BettingSystem bettingSystem;
    
    //Constructor initializes game components.
    public BlackjackGame(String playerName, double initialBalance) {
        deck = new Deck();
        player = new Player(playerName);  
        dealer = new Dealer();
        checkInput = new CheckInput();
        bettingSystem = new BettingSystem(0.0, initialBalance); 
    }
    
    //The main game loop.
    public void playGame() {
        while (true) {
            resetGame();

            System.out.println("Welcome to Blackjack, " + player.getName() + "!\n");
            System.out.println("Your current betting balance: $" + bettingSystem.getPlayerBalance());

            double betAmount = getBetAmount();
            bettingSystem.placeBet(betAmount);  
            
            //The initial deal: two cards to player and the dealer.
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());

            System.out.println(player);
            dealer.showFirstCard(); //Shows only one dealer card

            playerTurn(); //Player makes a decision.

            if (player.calculateHandScore() > 21) {
                FileIO.updatePlayerStats(player.getName(), false, false);
                bettingSystem.payout(false); 
            } else {
                dealerTurn(); //Dealer makes a decision.
                determineWinner(); //Compare the two hands and determine the outcome.
            }

            deck.reShuffle(); //Shuffle the deck for the next round

            if (!askToPlayAgain()) {
                showFinalStats();
                System.out.println("Thanks for playing! Final balance: $" + bettingSystem.getPlayerBalance());
                break;
            }
        }
    }
    
    //Gets the player to enter a valid amount to bet.
    private double getBetAmount() {
        double betAmount = 0;
        while (true) {
            System.out.print("Enter your bet: ");
            betAmount = checkInput.getBetAmount("");

            if (!bettingSystem.isValidBet(betAmount)) {
                System.out.println("Invalid bet amount. Must be between $" + bettingSystem.getMinimumBet() +
                                   " and $" + bettingSystem.getPlayerBalance());
            } else {
                break;
            }
        }
        return betAmount;
    }
    
    //Resets player and dealers hands.
    private void resetGame() {
        player.clearHand();
        dealer.clearHand();
        deck.reShuffle();
    }

    //The player makes their decision between hit, stand, and double down.
    private void playerTurn() {
        boolean doubledDown = false;

        while (player.calculateHandScore() < 21) {
            if (player.getHand().size() == 2 && bettingSystem.getPlayerBalance() >= bettingSystem.getCurrentBet()) {
                CheckInput.Choice choice = checkInput.getDoubleDownChoice("Hit (H), Stand (S), or Double Down (D)? ");
                if (choice == CheckInput.Choice.H) {
                    player.addCard(deck.dealCard());
                    System.out.println(player);
                } else if (choice == CheckInput.Choice.D) {
                    bettingSystem.doubleDown();
                    player.addCard(deck.dealCard());
                    System.out.println(player);
                    doubledDown = true;
                    break; //Player must stand after double down.
                } else {
                    break; //Stand.
                }
            } else {
                //This is just the regular hit or stand (no double down).
                CheckInput.Choice choice = checkInput.getChoice("Hit (H) or Stand (S)? ");
                if (choice == CheckInput.Choice.H) {
                    player.addCard(deck.dealCard());
                    System.out.println(player);
                } else {
                    break; //Stand.
                }
            }
        }

        if (player.calculateHandScore() > 21) {
            System.out.println("You busted! Dealer wins.");
            bettingSystem.payout(false);
        } else if (doubledDown) {
            System.out.println("You doubled down and ended your turn.");
        }
    }

    //The dealers actions are determined here. following blackjack rules.
    private void dealerTurn() {
        System.out.println("\nDealer reveals their hand:");
        System.out.println(dealer);

        while (dealer.calculateHandScore() < 17) {
            dealer.addCard(deck.dealCard());
            System.out.println(dealer);
        }
    }

    //Compares scores and determines the outcome.
    private void determineWinner() {
        int playerScore = player.calculateHandScore();
        int dealerScore = dealer.calculateHandScore();

        if (dealerScore > 21 || playerScore > dealerScore) {
            System.out.println("Congratulations! You win!");
            FileIO.updatePlayerStats(player.getName(), true, false);
            bettingSystem.payout(true);  
        } else if (playerScore < dealerScore) {
            System.out.println("Dealer wins. Better luck next time.");
            FileIO.updatePlayerStats(player.getName(), false, false);
            bettingSystem.payout(false);  
        } else {
            System.out.println("It's a tie!");
            FileIO.updatePlayerStats(player.getName(), false, true);
            bettingSystem.refundBet();
        }
    }

    //Asks the user if they want to play another round.
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

    //Dsiplays the players statistics from current and previous games played.
    private void showFinalStats(){
        String stats = FileIO.getPlayerStats(player.getName());

        if(stats != null){
            System.out.println("\nFinal Stats for " + player.getName());
            String[] parts = stats.split(" ");
            System.out.println("Wins = " + parts[1]);
            System.out.println("Losses = " + parts[2]);
            System.out.println("Ties = " + parts[3]);
        } else {
            System.out.println("\nStats were not found for " + player.getName());
        }
    }

    //Main method to start the game.
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
