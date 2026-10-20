package com.shopsphere.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Creates JDBC connections to the MySQL database.
 * Settings come from db.properties on the classpath and can be
 * overridden with system properties, e.g. -Ddb.password=secret
 */
public final class DBConnection {

    private static final String[] KEYS = {"db.driver", "db.url", "db.user", "db.password"};
    private static final Properties CONFIG = loadConfig();

    private DBConnection() {
    }

    private static Properties loadConfig() {
        Properties p = new Properties();
        p.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        p.setProperty("db.url", "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true");
        p.setProperty("db.user", "root");
        p.setProperty("db.password", "root");

        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) p.load(in);
        } catch (IOException e) {
            System.err.println("Could not read db.properties, using defaults: " + e.getMessage());
        }
        for (String key : KEYS) {
            String override = System.getProperty(key);
            if (override != null) p.setProperty(key, override);
        }
        try {
            Class.forName(p.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not found on the classpath: " + p.getProperty("db.driver"));
        }
        return p;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.user"),
                CONFIG.getProperty("db.password"));
    }
}
