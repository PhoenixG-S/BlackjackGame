/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

import java.util.Scanner;

/**
 *
 * @author phoen
 */

public class BlackjackGame {

    private Deck deck;
    private Player player;
    private Dealer dealer;
    private CheckInput checkInput;

    public BlackjackGame(String playerName) {
        deck = new Deck();  
        player = new Player(playerName);  
        dealer = new Dealer();  
        checkInput = new CheckInput();  
    }

    public void startGame() {

        while (true) {
            System.out.println("Welcome to Blackjack, " + player.getName() + "!\n");

            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());

            System.out.println(player);
            dealer.showFirstCard();

            playerTurn();

            if (player.calculateHandScore() > 21) {
                System.out.println("You busted! Dealer wins.");
            } else {

                dealerTurn();
                determineWinner();
            }

            deck.reShuffle();

            if (!askToPlayAgain()) {
                System.out.println("Thanks for playing!");
                break;
            }
        }
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
        } else if (playerScore < dealerScore) {
            System.out.println("Dealer wins. Better luck next time.");
        } else {
            System.out.println("It's a tie!");
        }
    }

    private boolean askToPlayAgain() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to play again? (Y/N): ");
        String response = scanner.next().toUpperCase();
        return response.equals("Y");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        BlackjackGame game = new BlackjackGame(playerName);
        game.startGame();
    }
}
