package voiceapp.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class responsible for handling connections with the database.
 */
public enum DBManager {
    instance;

    public static final boolean IS_DEPLOYED = false;

    private Connection conn = null;

    DBManager() {
    }

    /**
     * Gets the current active connection.
     * If no connection has been created, it initializes one.
     * @return
     */
    public Connection getConnection() {
        if (conn == null) {
            initConnection();
        }
        return conn;
    }

    /**
     * Initializes a connection with the database server.
     */
    private void initConnection() {
        Properties prop = new Properties();

        try {
            if (IS_DEPLOYED) {
                prop.load(new FileInputStream(System.getProperty("user.home") + "/ourdb.cfg"));
                Class.forName(prop.getProperty("driver"));
            } else {
                Class.forName("org.postgresql.Driver");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to connect to db: " + e);
            throw new RuntimeException(e);
        }

        try {
            if (IS_DEPLOYED) {
                String host = prop.getProperty("host");

                System.out.println("Connecting to the deployed database...");
                conn = DriverManager.getConnection(host, prop.getProperty("username"), prop.getProperty("password"));
            } else {
                // Local revival database (WSL PostgreSQL), replaces the old UT dev server.
                String devUrl = "jdbc:postgresql://127.0.0.1:5432/voice?currentSchema=voice4";

                System.out.println("Connecting to the local database...");
                conn = DriverManager.getConnection(devUrl, "voice", "voice-local");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("DB connected!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                closeDBConn();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    /**
     * Safely close the DB connection.
     * @throws SQLException
     */
    public void closeDBConn() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            System.out.println("Closing DB connection!");
            conn.close();
        }
    }
}
