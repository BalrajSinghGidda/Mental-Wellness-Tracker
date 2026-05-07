package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:mindmosaic.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String schema = new String(Files.readAllBytes(Paths.get("database/schema.sql")));
            stmt.executeUpdate(schema);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
