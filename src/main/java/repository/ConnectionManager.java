package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
    public static final String URL      = "jdbc:postgresql://localhost:5432/db";
    public static final String USER     = "postgres";
    public static final String PASSWORD = "12345678";

    static {
        try {
            // Загрузить драйвер (JDBC 4+ умеет делать это сам, но на всякий случай)
            Class.forName("org.postgresql.Driver");

            // При старте — создать таблицу, если её нет
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                String ddl = """
                    CREATE TABLE IF NOT EXISTS books (
                      id SERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      isbn VARCHAR(50) UNIQUE NOT NULL
                    );
                    """;
                stmt.execute(ddl);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database schema", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
