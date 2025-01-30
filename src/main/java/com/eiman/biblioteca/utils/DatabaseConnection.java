package com.eiman.biblioteca.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que gestiona la conexion a la base de datos utilizando configuraciones cargadas desde un archivo de propiedades.
 */
public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static Connection connection;

    /**
     * Establece los datos de la conexion.
     */
    public static void setConnectionData(String url, String user, String password) {
        URL = url;
        USER = user;
        PASSWORD = password;
    }

    /**
     * Obtiene la conexion a la base de datos.
     *
     * @return La conexion a la base de datos.
     * @throws SQLException Si ocurre un error en la conexion.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
