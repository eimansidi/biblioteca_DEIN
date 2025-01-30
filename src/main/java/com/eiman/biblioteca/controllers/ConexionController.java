package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.utils.DatabaseConnection;
import com.eiman.biblioteca.utils.LanguageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionController {

    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private TextField dbNameField;
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;
    @FXML private Button testButton;
    @FXML private Button connectButton;
    @FXML private Label statusLabel;

    private static final Logger logger = Logger.getLogger(ConexionController.class.getName());

    /**
     * Intenta probar la conexion con los datos ingresados por el usuario.
     * Habilita el boton de conexion si la conexion de prueba es exitosa.
     */
    @FXML
    private void onTestConnection() {
        String host = hostField.getText();
        String port = portField.getText();
        String dbName = dbNameField.getText();
        String user = userField.getText();
        String password = passwordField.getText();

        if (host.isEmpty() || port.isEmpty() || dbName.isEmpty() || user.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Todos los campos son obligatorios.");
            connectButton.setDisable(true); // Deshabilitar el botón de conexión hasta que la prueba sea exitosa
            return;
        }

        String url = "jdbc:mariadb://" + host + ":" + port + "/" + dbName;

        try {
            DatabaseConnection.setConnectionData(url, user, password);
            DatabaseConnection.getConnection();
            statusLabel.setText("Conexión de prueba exitosa.");
            logger.info("Conexión exitosa.");
            connectButton.setDisable(false); // Habilitar el botón de conexión si la prueba fue exitosa
        } catch (Exception e) {
            statusLabel.setText("Error al probar la conexión.");
            connectButton.setDisable(true); // Deshabilitar el botón de conexión si la prueba falla
            logger.log(Level.SEVERE, "Error de conexión.", e);
        }
    }

    /**
     * Guarda las credenciales y la URL de conexión si la prueba fue exitosa.
     */
    @FXML
    private void onConnect() {
        String host = hostField.getText();
        String port = portField.getText();
        String dbName = dbNameField.getText();
        String user = userField.getText();
        String password = passwordField.getText();

        // Crear la URL completa de conexión
        String url = "jdbc:mariadb://" + host + ":" + port + "/" + dbName;

        // Guardar la configuración de conexión en el archivo config.properties
        saveConnectionConfig(url, user, password);

        // Intentar conectar a la base de datos con la configuración guardada
        try {
            DatabaseConnection.setConnectionData(url, user, password);
            if (DatabaseConnection.getConnection() != null) {
                logger.info("Conexión exitosa a la base de datos.");
                statusLabel.setText("Conexión exitosa.");
                loadMainWindow();
            }
        } catch (Exception e) {
            statusLabel.setText("Error al conectar a la base de datos.");
            logger.log(Level.SEVERE, "Error al conectar a la base de datos.", e);
        }
    }

    /**
     * Guarda las credenciales y la URL de conexión en el archivo config.properties.
     */
    private void saveConnectionConfig(String url, String user, String password) {
        Properties properties = new Properties();
        properties.setProperty("db.url", url);
        properties.setProperty("db.user", user);
        properties.setProperty("db.password", password);

        try (OutputStream output = new FileOutputStream("config.properties")) {
            properties.store(output, null);
            logger.info("Credenciales guardadas correctamente en config.properties.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al guardar las credenciales en config.properties.", e);
        }
    }

    /**
     * Carga la ventana principal si la conexion es exitosa.
     */
    private void loadMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biblioteca.fxml"),
                    ResourceBundle.getBundle("i18n.messages", LanguageManager.getLocale()));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al cargar la ventana principal.", e);
        }
    }
}
