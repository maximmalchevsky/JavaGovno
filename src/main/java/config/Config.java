package config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;

    private Config(String host, int port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public static Config load() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().filename(".env").load();
        String host = dotenv.get("POSTGRES_HOST", "localhost");
        String port = dotenv.get("POSTGRES_PORT", "5432");
        String user = dotenv.get("POSTGRES_USER", "postgres");
        String password = dotenv.get("POSTGRES_PASSWORD", "12345678");
        String database = dotenv.get("POSTGRES_DB", "db");

        return new Config(host, Integer.parseInt(port), user, password, database);
    }


    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDatabase() {
        return this.database;
    }


}
