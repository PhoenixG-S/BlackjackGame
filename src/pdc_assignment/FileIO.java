/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

import java.io.*;
import java.util.*;

/**
 *
 * @author phoen
 */
public class FileIO {
    
    //The file where all the stats are saved.
    private static final String FILE_NAME = "playerStats.txt";
    
    //Gets the stats line for a given player from the file.
    public static String getPlayerStats(String playerName) {
        
        File file = new File(FILE_NAME);
        
        //If the file doesnt exist, returns null.
        if (!file.exists()){
            return null;
        }
        
        //Read each line from the file and try find the right player name.
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equalsIgnoreCase(playerName)) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; //The player is not found.
    }
    
    //Update the players stats in the file. If its a new player, add them to the file.
    public static void updatePlayerStats(String playerName, boolean win, boolean tie) {
        
        File file = new File(FILE_NAME);
        List<String> fullStats = new ArrayList<>();
        
        boolean found = false;

        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    
                    //Update the win/loss/tie count.
                    String[] parts = line.split(" ");
                    if (parts[0].equalsIgnoreCase(playerName)) {
                        int wins = Integer.parseInt(parts[1]);
                        int losses = Integer.parseInt(parts[2]);
                        int ties = Integer.parseInt(parts[3]);

                        if (win) {
                            wins++;
                        } else if (tie) {
                            ties++;
                        } else {
                            losses++;
                        }

                        line = playerName + " " + wins + " " + losses + " " + ties;
                        found = true;
                    }
                    fullStats.add(line);
                }

                reader.close();
            }
            
            //If a player was not found, add a new player in.
            if (!found) {
                int wins = 0;
                int losses = 0;
                int ties = 0;

                if (win) {
                    wins = 1;
                } else if (tie) {
                    ties = 1;
                } else {
                    losses = 1;
                }

                fullStats.add(playerName + " " + wins + " " + losses + " " + ties);
            }
            
            //Write the updated stats to the file.
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String stat : fullStats) {
                writer.write(stat);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
}
