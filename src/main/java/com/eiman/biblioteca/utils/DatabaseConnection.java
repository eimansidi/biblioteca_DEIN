package com.eiman.biblioteca.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que gestiona la conexión a la base de datos utilizando configuraciones cargadas desde un archivo de propiedades.
 */
public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static Connection connection;

    /**
     * Establece los datos de la conexión.
     *
     * @param url      URL de la base de datos.
     * @param user     Usuario de la base de datos.
     * @param password Contraseña de la base de datos.
     */
    public static void setConnectionData(String url, String user, String password) {
        URL = url;
        USER = user;
        PASSWORD = password;
        logger.info("Datos de conexión establecidos correctamente.");
    }

    /**
     * Obtiene la conexión a la base de datos.
     *
     * @return La conexión a la base de datos.
     * @throws SQLException Si ocurre un error en la conexión.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.info("Estableciendo nueva conexión a la base de datos.");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Conexión a la base de datos establecida con éxito.");
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Conexión a la base de datos cerrada correctamente.");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error al cerrar la conexión a la base de datos.", e);
            }
        }
    }
}
