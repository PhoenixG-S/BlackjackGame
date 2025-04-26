/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phoen
 */
public class Player {
    
    private String name;
    private List<Card> hand;
    
    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
    }
    
    //Getter methods.
    public String getName(){
        return name;
    }
    
    public List<Card> getHand(){
        return hand;
    }
    
    //Add a card to the players hand.
    public void addCard(Card card){
        hand.add(card);
    }
    
    //Clear the players hand 
    public void clearHand(){
        hand.clear();
    }
    
    //Calculate the total score of the players hand.
    //Aces are counted as 11 but can also be a 1 to avoid the player busting.
    public int calculateHandScore() {
        int score = 0;
        int aceCount = 0;


        for (Card card : hand) {
            score += card.getValue();
            if (card.getRank().equals("Ace")) {
                aceCount++;
            }
        }
        
        //Adjusts whether player will bust or not.
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }
    
    //Check if player has busted.
    public boolean isBusted() {
        return calculateHandScore() > 21;
    }
    
    @Override
    public String toString() {
        return name + "'s hand: " + hand.toString() + " || Total: " + calculateHandScore();
    }
    
}
