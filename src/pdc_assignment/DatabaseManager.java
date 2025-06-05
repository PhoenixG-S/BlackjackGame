/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author phoen
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:blackjack.db"; // SQLite DB file

    public static Connection getConnection() throws SQLException {
        
        return DriverManager.getConnection(DB_URL);
    }

    public static void createPlayerStatsTable() {
        
        String sql = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "player_name TEXT PRIMARY KEY," +
                "wins INTEGER DEFAULT 0," +
                "losses INTEGER DEFAULT 0," +
                "ties INTEGER DEFAULT 0" +
                ");";

        try (Connection conn = getConnection();
             Statement state = conn.createStatement()) {
            state.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean playerExists(String playerName) {
        
        String sql = "SELECT 1 FROM player_stats WHERE player_name = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement prepState = conn.prepareStatement(sql)) {
            prepState.setString(1, playerName);
            try (ResultSet rs = prepState.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertPlayer(String playerName, int wins, int losses, int ties) {
        
        String sql = "INSERT INTO player_stats (player_name, wins, losses, ties) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement prepState = conn.prepareStatement(sql)) {
            prepState.setString(1, playerName);
            prepState.setInt(2, wins);
            prepState.setInt(3, losses);
            prepState.setInt(4, ties);
            prepState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] getPlayerStats(String playerName) {
        
        String sql = "SELECT wins, losses, ties FROM player_stats WHERE player_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement prepState = conn.prepareStatement(sql)) {
            prepState.setString(1, playerName);
            try (ResultSet rs = prepState.executeQuery()) {
                if (rs.next()) {
                    return new int[]{
                            rs.getInt("wins"),
                            rs.getInt("losses"),
                            rs.getInt("ties")
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // If not found, return zeros
        return new int[]{0, 0, 0};
    }

    public static void updatePlayerStats(String playerName, int wins, int losses, int ties) {
        
        String sql = "UPDATE player_stats SET wins = ?, losses = ?, ties = ? WHERE player_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement prepState = conn.prepareStatement(sql)) {
            prepState.setInt(1, wins);
            prepState.setInt(2, losses);
            prepState.setInt(3, ties);
            prepState.setString(4, playerName);
            prepState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
