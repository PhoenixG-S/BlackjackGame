/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

/**
 *
 * @author phoen
 */
public class Card {
    
    private String rank;
    private String suit;
    private int value;
    
    public Card(String rank, String suit, int value){
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }
    
    public String getRank(){
        return rank;
    }
    
    public String getSuit(){
        return suit;
    }
    
    public int getValue(){
        return value;
    }
    
    @Override
    public String toString(){
        return rank + " of " + suit;
    }
}
