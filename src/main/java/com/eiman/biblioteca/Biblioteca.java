package com.eiman.biblioteca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Biblioteca extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Mostrar la ventana de conexión si no se ha conectado previamente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/conexion.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Configuración de Conexión");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
