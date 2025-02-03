package com.eiman.biblioteca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase principal de la aplicación Biblioteca.
 * Gestiona la inicialización de la interfaz gráfica con JavaFX.
 */
public class Biblioteca extends Application {
    private static final Logger logger = Logger.getLogger(Biblioteca.class.getName());

    /**
     * Metodo de inicio de la aplicación JavaFX.
     * Carga y muestra la ventana de conexión inicial.
     *
     * @param primaryStage El escenario principal de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Cargando la ventana de configuración de conexión.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/conexion.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Configuración de Conexión");
            primaryStage.setScene(scene);
            primaryStage.show();
            logger.info("Ventana de configuración de conexión mostrada correctamente.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar la ventana de configuración de conexión.", e);
        }
    }

    /**
     * Metodo principal que inicia la aplicación.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        logger.info("Iniciando la aplicación Biblioteca.");
        launch(args);
    }
}
