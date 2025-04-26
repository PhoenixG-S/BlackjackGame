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
    
    //Stack to hold the cards. Used a stack to simulate a real deck of cards.
    private Stack<Card> cards;
    
    public Deck(){
        cards = new Stack<>();
        createDeck();
        shuffleDeck();
        
    }
    
    //Fills the deck with the standard 52 playing cards.
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
    
    //Gives the cards value in blackjack game form.
    //netbeans improved this code
    public int getCardValue(String rank){
        
        return switch (rank) {
            case "Ace" -> 11;
            case "King", "Queen", "Jack" -> 10;
            default -> Integer.parseInt(rank);
        };
    }
    
    //Shuffles the deck.
    private void shuffleDeck(){
        Collections.shuffle(cards);
    }
    
    //Clears the old deck, creates a new one and shuffles that one.
    public void reShuffle(){
        cards.clear();
        createDeck();
        shuffleDeck();
        
    }
    
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
