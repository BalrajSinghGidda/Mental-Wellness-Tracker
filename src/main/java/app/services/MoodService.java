package app.services;

import app.database.DatabaseManager;
import app.models.MoodEntryModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoodService {

    public boolean addMoodEntry(int userId, String mood, int stressLevel, float sleepHours, int productivity, String notes) {
        String sql = "INSERT INTO mood_entries(user_id, mood, stress_level, sleep_hours, productivity, notes) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, mood);
            pstmt.setInt(3, stressLevel);
            pstmt.setFloat(4, sleepHours);
            pstmt.setInt(5, productivity);
            pstmt.setString(6, notes);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MoodEntryModel> getMoodEntries(int userId) {
        List<MoodEntryModel> entries = new ArrayList<>();
        String sql = "SELECT * FROM mood_entries WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                entries.add(new MoodEntryModel(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("mood"),
                        rs.getInt("stress_level"),
                        rs.getFloat("sleep_hours"),
                        rs.getInt("productivity"),
                        rs.getString("notes"),
                        rs.getDate("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
}
