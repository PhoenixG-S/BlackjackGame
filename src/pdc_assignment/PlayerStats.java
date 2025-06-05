/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

/**
 *
 * @author phoen
 */
public class PlayerStats {
    
    private String playerName;
    private int wins;
    private int losses;
    private int ties;

    public PlayerStats(String playerName, int wins, int losses, int ties) {
        this.playerName = playerName;
        this.wins = wins;
        this.losses = losses;
        this.ties = ties;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTies() {
        return ties;
    }

    @Override
    public String toString() {
        return playerName + " Wins: " + wins + ", Losses: " + losses + ", Ties: " + ties;
    }
}
