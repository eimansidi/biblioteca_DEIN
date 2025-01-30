package com.eiman.biblioteca.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static Connection connection;

    static {
        // Cargar las configuraciones desde un archivo de propiedades (config.properties)
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/config.properties")) {
            if (input != null) {
                properties.load(input);
                URL = properties.getProperty("db.url");
                USER = properties.getProperty("db.user");
                PASSWORD = properties.getProperty("db.password");
            } else {
                throw new IOException("Config file not found");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar las configuraciones de la base de datos", e);
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a la base de datos.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
