package com.eiman.biblioteca.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase que gestiona la conexion a la base de datos utilizando configuraciones cargadas desde un archivo de propiedades.
 * Permite obtener una conexion a la base de datos y cerrarla.
 */
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

    /**
     * Obtiene una conexion a la base de datos.
     * Si no hay una conexion activa o si esta cerrada, se crea una nueva conexion.
     *
     * @return La conexion activa a la base de datos.
     * @throws RuntimeException Si ocurre un error al conectar con la base de datos.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
        return connection;
    }

    /**
     * Cierra la conexion a la base de datos si esta abierta.
     *
     * @throws RuntimeException Si ocurre un error al cerrar la conexion.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }
}
