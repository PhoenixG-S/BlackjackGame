/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

/**
 *
 * @author phoen
 */
public class Dealer extends Player{
    
    public Dealer(){
        super("Dealer");
    }
    
    public void showFirstCard(){
        System.out.println(getName() + "'s First card: " + getHand().get(0));
    }
    
    public void playTurn(Deck deck) {
  
    while (calculateHandScore() < 17) {
        System.out.println(getName() + " has a score of " + calculateHandScore() + " and must hit.");
        addCard(deck.dealCard());
        System.out.println(getName() + " draws a card: " + getHand().get(getHand().size() - 1));
    }

    System.out.println(getName() + "'s full hand: " + getHand() + " || Total: " + calculateHandScore());
    }
}
