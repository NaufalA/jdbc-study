package com.enigmacamp.shared.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DBManager {
    private static Connection conn = null;

    public static void createConnection() throws SQLException {
        Dotenv dotenv = Dotenv.configure().load();
        String dbHost = dotenv.get("DB_HOST");
        String dbPort = dotenv.get("DB_PORT");
        String dbName = dotenv.get("DB_NAME");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_USER_PASSWORD");

        String url = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
        Properties properties = new Properties();
        properties.setProperty("user", dbUser);
        properties.setProperty("password", dbPassword);
        conn = DriverManager.getConnection(url, properties);
        conn.setAutoCommit(false);
    }

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            createConnection();
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

}
