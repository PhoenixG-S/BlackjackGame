/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

/**
 *
 * @author raghavbhalla
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {

    // Core game components
    private Deck deck;
    private Player player;
    private Dealer dealer;

    // UI Components
    private JTextArea playerHandArea;
    private JTextArea dealerHandArea;
    private JLabel playerScoreLabel;
    private JLabel dealerScoreLabel;
    private JButton hitButton;
    private JButton standButton;

    public GUI() {
        // Set up the main window
        setTitle("Blackjack Game");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Initialize game objects
        deck = new Deck();
        player = new Player("Player");
        dealer = new Dealer();

        startNewRound(); // Deal initial cards

        // Set up display areas
        playerHandArea = new JTextArea(5, 20);
        playerHandArea.setEditable(false);
        dealerHandArea = new JTextArea(5, 20);
        dealerHandArea.setEditable(false);

        playerScoreLabel = new JLabel();
        dealerScoreLabel = new JLabel();

        // Set up buttons
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");

        // Add button actions
        hitButton.addActionListener(e -> playerHits());
        standButton.addActionListener(e -> playerStands());

        // Organize panels
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Player panel
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        playerPanel.add(new JScrollPane(playerHandArea), BorderLayout.CENTER);
        playerPanel.add(playerScoreLabel, BorderLayout.SOUTH);

        // Dealer panel
        JPanel dealerPanel = new JPanel(new BorderLayout());
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        dealerPanel.add(new JScrollPane(dealerHandArea), BorderLayout.CENTER);
        dealerPanel.add(dealerScoreLabel, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);

        // Add panels to main frame
        mainPanel.add(dealerPanel, BorderLayout.NORTH);
        mainPanel.add(playerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        updateDisplay(); // Show current game state

        setVisible(true); // Display GUI
    }

    // Starts a new round by resetting and dealing cards
    private void startNewRound() {
        deck.reShuffle();
        player.clearHand();
        dealer.clearHand();

        // Deal two cards to both player and dealer
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
    }

    // Updates the UI to reflect current game state
    private void updateDisplay() {
        // Display player's hand and score
        playerHandArea.setText("");
        for (Card c : player.getHand()) {
            playerHandArea.append(c.toString() + "\n");
        }
        playerScoreLabel.setText("Score: " + player.calculateHandScore());

        // Display dealer's first card and hide the rest
        dealerHandArea.setText("");
        if (dealer.getHand().size() > 0) {
            dealerHandArea.append(dealer.getHand().get(0).toString() + "\n");
            for (int i = 1; i < dealer.getHand().size(); i++) {
                dealerHandArea.append("[Hidden]\n");
            }
        }
        dealerScoreLabel.setText("Score: ?");
    }

    // Called when player clicks "Hit"
    private void playerHits() {
        player.addCard(deck.dealCard());
        updateDisplay();

        // If player busts, end round
        if (player.isBusted()) {
            JOptionPane.showMessageDialog(this, "You busted! Dealer wins.");
            endRound();
        }
    }

    // Called when player clicks "Stand"
    private void playerStands() {
        dealerPlay(); // Let the dealer play
    }

    // Dealer's automatic turn after player stands
    private void dealerPlay() {
        // Reveal dealer's full hand
        dealerHandArea.setText("");
        for (Card c : dealer.getHand()) {
            dealerHandArea.append(c.toString() + "\n");
        }
        dealerScoreLabel.setText("Score: " + dealer.calculateHandScore());

        // Dealer hits until score >= 17
        while (dealer.calculateHandScore() < 17) {
            dealer.addCard(deck.dealCard());
            dealerHandArea.append(dealer.getHand().get(dealer.getHand().size() - 1).toString() + "\n");
            dealerScoreLabel.setText("Score: " + dealer.calculateHandScore());
        }

        // Determine winner
        if (dealer.isBusted()) {
            JOptionPane.showMessageDialog(this, "Dealer busted! You win!");
        } else {
            int playerScore = player.calculateHandScore();
            int dealerScore = dealer.calculateHandScore();

            if (playerScore > dealerScore) {
                JOptionPane.showMessageDialog(this, "You win!");
            } else if (playerScore < dealerScore) {
                JOptionPane.showMessageDialog(this, "Dealer wins!");
            } else {
                JOptionPane.showMessageDialog(this, "It's a tie!");
            }
        }

        endRound(); // End this round
    }

    // Ends current round and gives option to replay or exit
    private void endRound() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        int result = JOptionPane.showConfirmDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            startNewRound();
            updateDisplay();
            hitButton.setEnabled(true);
            standButton.setEnabled(true);
        } else {
            System.exit(0); // Exit the game
        }
    }

    public static void main(String[] args) {
        // Launch the GUI on the Swing thread
        SwingUtilities.invokeLater(GUI::new);
    }
}
