/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

import java.util.Scanner;

/**
 *
 * @author phoen
 */
public class CheckInput {
    
    public enum Choice{
        H,S;
    }
    
    private final Scanner scanner = new Scanner(System.in);
    
    public Choice getChoice(String message) {
    while (true) {
        System.out.print(message);
        
        try {
            return Choice.valueOf(scanner.next().toUpperCase());
        } 
        
        catch (IllegalArgumentException e) {
            System.out.println("Invalid input! Please enter 'H' (Hit) or 'S' (Stand).");
        }
    }
}
    
}
