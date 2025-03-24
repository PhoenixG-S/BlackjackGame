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
    
    public String getName(){
        return name;
    }
    
    public List<Card> getHand(){
        return hand;
    }
    
    public void addCard(Card card){
        hand.add(card);
    }
    
    public void clearHand(){
        hand.clear();
    }
    
    //ChatGPT helped with calculating the hands
    public int calculateHandScore() {
        int score = 0;
        int aceCount = 0;


        for (Card card : hand) {
            score += card.getValue();
            if (card.getRank().equals("Ace")) {
                aceCount++;
            }
        }

        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }
    
    public boolean isBusted() {
        return calculateHandScore() > 21;
    }
    
    @Override
    public String toString() {
        return name + "'s hand: " + hand.toString() + " || Total: " + calculateHandScore();
    }
    
}
