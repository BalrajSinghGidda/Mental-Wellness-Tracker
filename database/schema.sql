-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Mood Entries Table
CREATE TABLE IF NOT EXISTS mood_entries (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    mood VARCHAR(50) NOT NULL,
    stress_level INTEGER NOT NULL,
    sleep_hours FLOAT NOT NULL,
    productivity INTEGER NOT NULL,
    notes TEXT,
    created_at DATE DEFAULT (date('now')),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Wellness Tips Table
CREATE TABLE IF NOT EXISTS wellness_tips (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mood_type VARCHAR(50) NOT NULL,
    tip_text TEXT NOT NULL
);

-- Insert some default wellness tips
INSERT INTO wellness_tips (mood_type, tip_text) VALUES
('High Stress', 'Try a 5-minute breathing exercise. Inhale for 4 seconds, hold for 7, and exhale for 8.'),
('Low Sleep', 'Avoid screens for an hour before bed. The blue light can disrupt your natural sleep cycle.'),
('Productive', 'Great job! Keep the momentum going, but remember to take short breaks to stay fresh.'),
('Happy', 'Share your positivity! Call a friend or family member and tell them something you appreciate about them.'),
('Sad', 'It''s okay to feel down. Try listening to some uplifting music or watching a favorite comfort movie.'),
('Angry', 'Channel that energy into something productive. Go for a run, do some push-ups, or write down your feelings.');
