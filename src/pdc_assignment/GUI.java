/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    private JButton doubleDownButton;

    private JTextField betField;
    private JButton placeBetButton;
    private JLabel moneyLabel;

    // Game money management
    private int playerMoney;
    private int currentBet;

    public GUI() {
        playerMoney = askStartingMoney();
        currentBet = 0;

        setTitle("Blackjack Game");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        deck = new Deck();
        player = new Player("Player");
        dealer = new Dealer();

        playerHandArea = new JTextArea(5, 20);
        playerHandArea.setEditable(false);
        dealerHandArea = new JTextArea(5, 20);
        dealerHandArea.setEditable(false);

        playerScoreLabel = new JLabel();
        dealerScoreLabel = new JLabel();

        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        doubleDownButton = new JButton("Double Down");

        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleDownButton.setEnabled(false);

        hitButton.addActionListener(e -> playerHits());
        standButton.addActionListener(e -> playerStands());
        doubleDownButton.addActionListener(e -> playerDoubleDown());

        JPanel bettingPanel = new JPanel();
        bettingPanel.setBorder(BorderFactory.createTitledBorder("Place Your Bet"));
        bettingPanel.add(new JLabel("Bet Amount ($):"));
        betField = new JTextField(6);
        betField.setToolTipText("Enter amount to bet (max $" + playerMoney + ")");
        placeBetButton = new JButton("Place Bet");
        placeBetButton.addActionListener(e -> placeBet());
        moneyLabel = new JLabel("Money: $" + playerMoney);
        bettingPanel.add(betField);
        bettingPanel.add(placeBetButton);
        bettingPanel.add(moneyLabel);

        //Dealer panel on the left.
        JPanel dealerPanel = new JPanel(new BorderLayout());
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        dealerPanel.add(new JScrollPane(dealerHandArea), BorderLayout.CENTER);
        dealerPanel.add(dealerScoreLabel, BorderLayout.SOUTH);

        //Player panel on the right.
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        playerPanel.add(new JScrollPane(playerHandArea), BorderLayout.CENTER);
        playerPanel.add(playerScoreLabel, BorderLayout.SOUTH);

        //Button panel below the dealer and player panel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(doubleDownButton);

        //Main panel with GridLayout(1 row, 2 columns).
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(dealerPanel);
        mainPanel.add(playerPanel);

        //Frame layout.
        setLayout(new BorderLayout());
        add(bettingPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);  //Dealer on left, Player on right.
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    //Ask player starting money through input.
    private int askStartingMoney() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter your starting money:", "Starting Money", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                //Player cancelled, will exit.
                System.exit(0);
            }
            try {
                int money = Integer.parseInt(input);
                if (money > 0) {
                    return money;
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            }
        }
    }

    private void placeBet() {
        try {
            int bet = Integer.parseInt(betField.getText());
            if (bet <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a bet greater than zero.");
                return;
            }
            if (bet > playerMoney) {
                JOptionPane.showMessageDialog(this, "You don't have enough money for that bet.");
                return;
            }

            currentBet = bet;
            playerMoney -= currentBet;
            moneyLabel.setText("Money: $" + playerMoney);

            //Disable betting inputs.
            placeBetButton.setEnabled(false);
            betField.setEditable(false);
            betField.setToolTipText(null);

            startNewRound();
            updateDisplay();

            hitButton.setEnabled(true);
            standButton.setEnabled(true);
            doubleDownButton.setEnabled(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for your bet.");
        }
    }

    //Starts a new round by resetting and dealing cards.
    private void startNewRound() {
        deck.reShuffle();
        player.clearHand();
        dealer.clearHand();

        //Deal two cards to both player and dealer.
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
    }

    //Updates the UI to reflect current game state.
    private void updateDisplay() {
        // Player hand
        playerHandArea.setText("");
        for (Card c : player.getHand()) {
            playerHandArea.append(c.toString() + "\n");
        }
        playerScoreLabel.setText("Score: " + player.calculateHandScore());
        
        playerHandArea.revalidate();
        playerHandArea.repaint();
        //Dealer shows only first card until round ends.
        dealerHandArea.setText("");
        if (dealer.getHand().size() > 0) {
            dealerHandArea.append(dealer.getHand().get(0).toString() + "\n");
            for (int i = 1; i < dealer.getHand().size(); i++) {
                dealerHandArea.append("[Hidden]\n");
            }
        }
        dealerScoreLabel.setText("Score: ?");
    }

    private void playerHits() {
        player.addCard(deck.dealCard());
        updateDisplay();

        if (player.isBusted()) {
            revealDealerHand();
            JOptionPane.showMessageDialog(this, "You busted! Dealer wins.");
            endRound(false);
        }
    }

    private void playerStands() {
        dealerPlay();
    }

    private void playerDoubleDown() {
        if (playerMoney < currentBet) {
            JOptionPane.showMessageDialog(this, "Not enough money to double down.");
            return;
        }

        //Double the bet.
        playerMoney -= currentBet;
        currentBet *= 2;
        moneyLabel.setText("Money: $" + playerMoney);

        //Player draws one card only.
        player.addCard(deck.dealCard());
        updateDisplay();

        if (player.isBusted()) {
            revealDealerHand();
            JOptionPane.showMessageDialog(this, "You busted after doubling down! Dealer wins.");
            endRound(false);
            return;
        }

        //After double down player stands automatically.
        dealerPlay();
    }

    private void dealerPlay() {
        revealDealerHand();

        while (dealer.calculateHandScore() < 17) {
            dealer.addCard(deck.dealCard());
            dealerHandArea.append(dealer.getHand().get(dealer.getHand().size() - 1).toString() + "\n");
            dealerScoreLabel.setText("Score: " + dealer.calculateHandScore());
        }

        int playerScore = player.calculateHandScore();
        int dealerScore = dealer.calculateHandScore();

        if (dealer.isBusted()) {
            JOptionPane.showMessageDialog(this, "Dealer busted! You win!");
            endRound(true);
        } else if (playerScore > dealerScore) {
            JOptionPane.showMessageDialog(this, "You win!");
            endRound(true);
        } else if (playerScore < dealerScore) {
            JOptionPane.showMessageDialog(this, "Dealer wins!");
            endRound(false);
        } else {
            JOptionPane.showMessageDialog(this, "It's a tie!");
            //Tie returns bet, so treat as win in terms of money (no loss).
            endRoundNeutral();
        }
    }

    private void revealDealerHand() {
        dealerHandArea.setText("");
        for (Card c : dealer.getHand()) {
            dealerHandArea.append(c.toString() + "\n");
        }
        dealerScoreLabel.setText("Score: " + dealer.calculateHandScore());
    }

    //End round with player win (payout).
    private void endRound(boolean playerWon) {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleDownButton.setEnabled(false);

        if (playerWon) {
            playerMoney += currentBet * 2;  //player gets double the bet.
        }
        moneyLabel.setText("Money: $" + playerMoney);

        askReplay();
    }

    //End round for tie (return bet).
    private void endRoundNeutral() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleDownButton.setEnabled(false);

        playerMoney += currentBet;  //bet returned.
        moneyLabel.setText("Money: $" + playerMoney);

        askReplay();
    }

    private void askReplay() {
        int result = JOptionPane.showConfirmDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            currentBet = 0;
            betField.setText("");
            betField.setEditable(true);
            betField.setToolTipText("Enter amount to bet (max $" + playerMoney + ")");
            placeBetButton.setEnabled(true);

            //Clear hands and UI.
            playerHandArea.setText("");
            dealerHandArea.setText("");
            playerScoreLabel.setText("");
            dealerScoreLabel.setText("");

        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
