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

    private static final String DB_URL = "jdbc:derby:blackjackDB;create=true"; // Derby DB
    private static final String DRIVER = "org.apache.derby.iapi.jdbc.AutoloadedDriver";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL);
    }

    public static void createPlayerStatsTable() {
        String sql = "CREATE TABLE player_stats (" +
                "player_name VARCHAR(255) PRIMARY KEY, " +
                "wins INT DEFAULT 0, " +
                "losses INT DEFAULT 0, " +
                "ties INT DEFAULT 0" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            // Derby throws an error if the table already exists, so we can ignore it
            if (!e.getSQLState().equals("X0Y32")) { // "X0Y32" = Table already exists
                e.printStackTrace();
            }
        }
    }

    public static boolean playerExists(String playerName) {
        String sql = "SELECT 1 FROM player_stats WHERE player_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            try (ResultSet rs = pstmt.executeQuery()) {
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
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, wins);
            pstmt.setInt(3, losses);
            pstmt.setInt(4, ties);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] getPlayerStats(String playerName) {
        String sql = "SELECT wins, losses, ties FROM player_stats WHERE player_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            try (ResultSet rs = pstmt.executeQuery()) {
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
        return new int[]{0, 0, 0}; // Not found
    }

    public static void updatePlayerStats(String playerName, int wins, int losses, int ties) {
        String sql = "UPDATE player_stats SET wins = ?, losses = ?, ties = ? WHERE player_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, wins);
            pstmt.setInt(2, losses);
            pstmt.setInt(3, ties);
            pstmt.setString(4, playerName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
