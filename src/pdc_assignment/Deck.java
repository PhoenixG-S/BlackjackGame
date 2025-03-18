/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

import java.util.Collections;
import java.util.Stack;

/**
 *
 * @author phoen
 */
public class Deck {
    
    private Stack<Card> cards;
    
    public Deck(){
        cards = new Stack<>();
        createDeck();
        shuffleDeck();
        
    }
    
    private void createDeck(){
        
        String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        
        for(String suit : suits){
            for(String rank : ranks){
                int value = getCardValue(rank);
                cards.push(new Card(rank, suit, value));
            }
        }
    }
    
    //netbeans improved this code
    public int getCardValue(String rank){
        
        return switch (rank) {
            case "Ace" -> 11;
            case "King", "Queen", "Jack" -> 10;
            default -> Integer.parseInt(rank);
        };
    }
    
    private void shuffleDeck(){
        Collections.shuffle(cards);
    }
    
    public void reShuffle(){
        cards.clear();
        createDeck();
        shuffleDeck();
        
    }
    
    //ChatGPT corrected this code
    public Card dealCard(){
        
        if(!cards.isEmpty()){
            return cards.pop();
        }
        return null;
    }
    
    public int cardsLeft(){
        return cards.size();
    }
    
    
    
    
}
