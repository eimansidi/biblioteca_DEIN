package com.eiman.biblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.File;

/**
 * Controlador para la ventana de ayuda.
 * Este controlador maneja la carga del archivo HTML que contiene la guía rápida de la aplicación,
 * mostrando su contenido en un componente WebView.
 */
public class HelpController {

    @FXML
    private WebView webViewHelp;

    /**
     * Inicializa la vista de la ayuda cargando el archivo HTML correspondiente en el WebView.
     * Utiliza el motor WebEngine para cargar y mostrar el archivo HTML de la guía rápida.
     */
    @FXML
    public void initialize() {
        WebEngine webEngine = webViewHelp.getEngine();
        // Ruta al archivo de ayuda (guía rápida en formato HTML)
        File file = new File("src/main/resources/help/guia.html");
        // Cargar el archivo HTML en el WebView
        webEngine.load(file.toURI().toString());
    }
}
