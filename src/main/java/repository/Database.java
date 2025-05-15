package repository;

import config.Config;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Хранит DataSource и выдаёт новое соединение по требованию. */
public final class Database {

    private final DataSource ds;

    private Database(DataSource ds) { this.ds = ds; }

    public static Database newDatabase(Config cfg) {

        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUser        (cfg.getUser());
        ds.setPassword    (cfg.getPassword());
        ds.setServerNames (new String[]{cfg.getHost()});
        ds.setPortNumbers (new int[]   {cfg.getPort()});
        ds.setDatabaseName(cfg.getDatabase());

        /* ping + DDL один раз */
        try (Connection c = ds.getConnection()) {
            if (!c.isValid(2)) throw new SQLException("Ping failed");

            try (Statement st = c.createStatement()) {
                st.execute("""
                    CREATE TABLE IF NOT EXISTS books (
                      id SERIAL PRIMARY KEY,
                      title   VARCHAR(255) NOT NULL,
                      author  VARCHAR(255) NOT NULL,
                      isbn    VARCHAR(50)  UNIQUE NOT NULL
                    );
                """);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Database init failed", e);
        }

        return new Database(ds);
    }

    /** Каждому вызову — своё соединение (закрывайте в try-with-resources). */
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
