package me.orbitium.oFHeroesLobby.mysql;

import me.orbitium.oFHeroesLobby.OFHeroesLobby;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.logging.Level;

public class MySQL {
    private static Connection connection;

    public static void connect(boolean force) throws SQLException {
        if ((connection == null || connection.isClosed()) || force) {
            FileConfiguration config = OFHeroesLobby.instance.getConfig();
            String hostname = config.getString("database.hostname");
            String port = config.getString("database.port");
            String database = config.getString("database.database");
            String username = config.getString("database.username");
            String password = config.getString("database.password");

            String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database +
                    "?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";

            connection = DriverManager.getConnection(url, username, password);

            checkAndCreateTable();
        }
    }

    private static final int MAX_RETRIES = 5; // Number of retry attempts
    private static final int RETRY_DELAY = 100; // Delay between retries (in milliseconds)

    public static Connection getConnection() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT 1");
            if (rs.next())
                return connection;

        } catch (SQLException queryException) {
        }

        int retries = 0;

        // Loop to retry the connection
        while (retries < MAX_RETRIES) {
            try {
                connect(true);
                if (connection != null) {
                    OFHeroesLobby.instance.getLogger().log(Level.INFO, "Connected to the database successfully on attempt " + (retries + 1));
                    return connection;
                }
            } catch (SQLException e) {
                retries++;
                OFHeroesLobby.instance.getLogger().log(Level.INFO, "Failed to connect. Retrying attempt " + retries + " of " + MAX_RETRIES);
                if (retries == MAX_RETRIES) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(RETRY_DELAY); // Wait before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    ie.printStackTrace();
                }
            }
        }
        return null;
    }

//    public Connection getConnection() {
//        return getConnection();
////        try {
////            if (connection == null || connection.isClosed())
////                connect();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        return connection;
//    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private static void checkAndCreateTable() {
        try {
            // Get database metadata to check if the table exists
            DatabaseMetaData meta = getConnection().getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "players", null);

            if (!resultSet.next()) {
                // Table does not exist, so create it
                try (Statement statement = connection.createStatement()) {
                    String createTableSQL = "CREATE TABLE players (" +
                            "uuid VARCHAR(36) NOT NULL PRIMARY KEY," +   // Player UUID
                            "selected_class VARCHAR(255) NOT NULL" +     // Player's selected class
                            ")";
                    statement.executeUpdate(createTableSQL);
                    System.out.println("Table 'selectedClasses' created successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to set the player's selected class using UUID
    public static void setSelectedClass(String uuid, String selectedClass) {
        String updateSQL = "INSERT INTO players (uuid, selected_class) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE selected_class = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(updateSQL)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, selectedClass);
            preparedStatement.setString(3, selectedClass);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Player's selected class updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Function to get the player's selected class using UUID
    public static String getSelectedClass(String uuid) {
        String querySQL = "SELECT selected_class FROM players WHERE uuid = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(querySQL)) {
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("selected_class");
            } else {
                System.out.println("Player with UUID " + uuid + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
