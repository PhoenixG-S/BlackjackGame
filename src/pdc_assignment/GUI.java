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

    private Deck deck;
    private Player player;
    private Dealer dealer;

    private JTextArea playerHandArea;
    private JTextArea dealerHandArea;
    private JLabel playerScoreLabel;
    private JLabel dealerScoreLabel;
    private JButton hitButton;
    private JButton standButton;
    private JButton doubleDownButton;

    private JTextField betField;
    private JButton placeBetButton;
    private JButton addMoneyButton;
    private JLabel moneyLabel;
    private JLabel statsLabel;

    private int playerMoney;
    private int currentBet;

    private String playerName;
    private int totalWins;
    private int totalLosses;
    private int totalTies;

    public GUI() {
        playerName = askPlayerName();
        playerMoney = askStartingMoney();
        currentBet = 0;

        DatabaseManager.createPlayerStatsTable();

        if (!DatabaseManager.playerExists(playerName)) {
            DatabaseManager.insertPlayer(playerName, 0, 0, 0);
        }

        int[] stats = DatabaseManager.getPlayerStats(playerName);
        totalWins = stats[0];
        totalLosses = stats[1];
        totalTies = stats[2];

        setTitle("Blackjack Game - Player: " + playerName);
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
        addMoneyButton = new JButton("Add Money");
        addMoneyButton.addActionListener(e -> addMoney());
        moneyLabel = new JLabel("Money: $" + playerMoney);
        statsLabel = new JLabel();
        statsLabel.setBorder(null);
        statsLabel.setVisible(false); // Hidden until game ends

        bettingPanel.add(betField);
        bettingPanel.add(placeBetButton);
        bettingPanel.add(addMoneyButton);
        bettingPanel.add(moneyLabel);
        bettingPanel.add(statsLabel);

        JPanel dealerPanel = new JPanel(new BorderLayout());
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        dealerPanel.add(new JScrollPane(dealerHandArea), BorderLayout.CENTER);
        dealerPanel.add(dealerScoreLabel, BorderLayout.SOUTH);

        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        playerPanel.add(new JScrollPane(playerHandArea), BorderLayout.CENTER);
        playerPanel.add(playerScoreLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(doubleDownButton);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(dealerPanel);
        mainPanel.add(playerPanel);

        setLayout(new BorderLayout());
        add(bettingPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private String askPlayerName() {
        while (true) {
            String name = JOptionPane.showInputDialog(this, "Enter your name:", "Player Name", JOptionPane.QUESTION_MESSAGE);
            if (name == null) System.exit(0);
            name = name.trim();
            if (!name.isEmpty()) return name;
            JOptionPane.showMessageDialog(this, "Please enter a valid name.");
        }
    }

    private int askStartingMoney() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter your starting money:", "Starting Money", JOptionPane.QUESTION_MESSAGE);
            if (input == null) System.exit(0);
            try {
                int money = Integer.parseInt(input);
                if (money > 0) return money;
                else JOptionPane.showMessageDialog(this, "Please enter a positive number.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            }
        }
    }

    private void addMoney() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter amount to add:", "Add Money", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return; // Cancel pressed, do nothing
            try {
                int amount = Integer.parseInt(input);
                if (amount > 0) {
                    playerMoney += amount;
                    moneyLabel.setText("Money: $" + playerMoney);
                    betField.setToolTipText("Enter amount to bet (max $" + playerMoney + ")");
                    // Re-enable betting if previously disabled due to no funds
                    if (!placeBetButton.isEnabled()) {
                        placeBetButton.setEnabled(true);
                        betField.setEditable(true);
                    }
                    JOptionPane.showMessageDialog(this, "Added $" + amount + " to your balance.");
                    return;
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
                int response = JOptionPane.showConfirmDialog(this,
                    "You don't have enough money for that bet. Would you like to add more money?",
                    "Insufficient Funds", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    addMoney();
                }
                return;
            }

            currentBet = bet;
            playerMoney -= currentBet;
            moneyLabel.setText("Money: $" + playerMoney);

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

    private void startNewRound() {
        deck.reShuffle();
        player.clearHand();
        dealer.clearHand();

        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
    }

    private void updateDisplay() {
        playerHandArea.setText("");
        for (Card c : player.getHand()) {
            playerHandArea.append(c.toString() + "\n");
        }
        playerScoreLabel.setText("Score: " + player.calculateHandScore());

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
            int response = JOptionPane.showConfirmDialog(this,
                "Not enough money to double down. Would you like to add more money?",
                "Insufficient Funds", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                addMoney();
            }
            return;
        }

        playerMoney -= currentBet;
        currentBet *= 2;
        moneyLabel.setText("Money: $" + playerMoney);

        player.addCard(deck.dealCard());
        updateDisplay();

        if (player.isBusted()) {
            revealDealerHand();
            JOptionPane.showMessageDialog(this, "You busted after doubling down! Dealer wins.");
            endRound(false);
            return;
        }

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

    private void endRound(boolean playerWon) {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleDownButton.setEnabled(false);

        if (playerWon) {
            playerMoney += currentBet * 2;
            totalWins++;
        } else {
            totalLosses++;
        }

        moneyLabel.setText("Money: $" + playerMoney);
        DatabaseManager.updatePlayerStats(playerName, totalWins, totalLosses, totalTies);
        askReplay();
    }

    private void endRoundNeutral() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleDownButton.setEnabled(false);

        playerMoney += currentBet;
        moneyLabel.setText("Money: $" + playerMoney);
        totalTies++;
        DatabaseManager.updatePlayerStats(playerName, totalWins, totalLosses, totalTies);
        askReplay();
    }

    private void askReplay() {
        if (playerMoney <= 0) {
            int response = JOptionPane.showConfirmDialog(this,
                "You have no money left. Would you like to add more money to continue playing?",
                "Game Over", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                addMoney();
            } else {
                updateStatsLabel();
                statsLabel.setVisible(true);
                hitButton.setEnabled(false);
                standButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                placeBetButton.setEnabled(false);
                betField.setEditable(false);
                return;
            }
        }

        int result = JOptionPane.showConfirmDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            currentBet = 0;
            betField.setText("");
            betField.setEditable(true);
            betField.setToolTipText("Enter amount to bet (max $" + playerMoney + ")");
            placeBetButton.setEnabled(true);

            playerHandArea.setText("");
            dealerHandArea.setText("");
            playerScoreLabel.setText("");
            dealerScoreLabel.setText("");
            statsLabel.setVisible(false);
        } else {
            updateStatsLabel();
            statsLabel.setVisible(true);

            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            doubleDownButton.setEnabled(false);
            placeBetButton.setEnabled(false);
            betField.setEditable(false);
        }
    }

    private void updateStatsLabel() {
        statsLabel.setText("<html>Wins: " + totalWins + "<br>" +
                           "Losses: " + totalLosses + "<br>" +
                           "Ties: " + totalTies + "</html>");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
