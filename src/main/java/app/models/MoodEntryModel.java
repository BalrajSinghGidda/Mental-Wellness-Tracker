package app.models;

import java.sql.Date;

public class MoodEntryModel {
    private int id;
    private int userId;
    private String mood;
    private int stressLevel;
    private float sleepHours;
    private int productivity;
    private String notes;
    private Date createdAt;

    public MoodEntryModel(int id, int userId, String mood, int stressLevel, float sleepHours, int productivity, String notes, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.mood = mood;
        this.stressLevel = stressLevel;
        this.sleepHours = sleepHours;
        this.productivity = productivity;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getMood() { return mood; }
    public int getStressLevel() { return stressLevel; }
    public float getSleepHours() { return sleepHours; }
    public int getProductivity() { return productivity; }
    public String getNotes() { return notes; }
    public Date getCreatedAt() { return createdAt; }
}
