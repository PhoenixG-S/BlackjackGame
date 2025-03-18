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
    
}
