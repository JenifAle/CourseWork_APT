package com.quizsystem.util;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for getting a JDBC connection to the XAMPP MySQL database.
 * Configuration is read from .env file (or system defaults if .env is missing).
 *
 * Reference: Lecture 2 (Database and DAO with POJO).
 */
public class DbConfig {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()  // Don't fail if .env is missing; use defaults
            .load();

    // Read from .env file, or use defaults
    private static final String DB_URL = dotenv.get("DB_URL") != null
            ? dotenv.get("DB_URL")
            : "jdbc:mysql://localhost:3306/quiz_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USERNAME = dotenv.get("DB_USERNAME") != null
            ? dotenv.get("DB_USERNAME")
            : "root";
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD") != null
            ? dotenv.get("DB_PASSWORD")
            : "";

    /**
     * Returns a fresh database connection.
     * Database credentials are read from .env file (or defaults if .env is missing).
     * Caller is responsible for closing it (use try-with-resources).
     *
     * @return java.sql.Connection
     * @throws SQLException           if connection cannot be established
     * @throws ClassNotFoundException if the JDBC driver class is missing
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load the MySQL JDBC driver explicitly so the program fails fast
        // with a clear message if mysql-connector-j is not on the classpath.
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}