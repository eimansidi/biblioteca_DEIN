package com.eiman.biblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador de la ventana de ayuda que carga un archivo HTML en un WebView.
 */
public class HelpController {

    @FXML private WebView webView;
    private static final Logger logger = Logger.getLogger(HelpController.class.getName());

    /**
     * Carga el archivo HTML en el WebView.
     * @param archivoHtml Ruta del archivo HTML a cargar.
     */
    public void cargarHTML(String archivoHtml) {
        if (webView == null) {
            logger.severe("Error: webView no está inicializado.");
            return;
        }

        WebEngine webEngine = webView.getEngine();
        URL url = getClass().getResource(archivoHtml);
        if (url != null) {
            webEngine.load(url.toExternalForm());
            logger.info("Archivo HTML cargado correctamente: " + archivoHtml);
        } else {
            logger.severe("Error: No se pudo cargar el archivo HTML " + archivoHtml);
        }
    }
}