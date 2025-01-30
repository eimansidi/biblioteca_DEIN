package com.eiman.biblioteca;

import com.eiman.biblioteca.utils.LanguageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Clase principal de la aplicacion que lanza la interfaz grafica.
 * Esta clase se encarga de cargar el archivo FXML de la ventana principal y mostrarla.
 */
public class Biblioteca extends Application {

    /**
     * Metodo de inicio de la aplicacion.
     * Carga el archivo FXML de la interfaz principal y lo muestra en una ventana.
     *
     * @param primaryStage El escenario principal donde se muestra la aplicacion.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML de la ventana principal y el archivo de recursos para el idioma
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biblioteca.fxml"),
                    ResourceBundle.getBundle("i18n.messages", LanguageManager.getLocale()));
            Scene scene = new Scene(loader.load());

            // Establecer el titulo de la ventana
            primaryStage.setTitle("Gestion de Biblioteca");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo principal para iniciar la aplicacion JavaFX.
     *
     * @param args Los argumentos que se pasan a la aplicacion.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
