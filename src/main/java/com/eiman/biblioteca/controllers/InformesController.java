package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.utils.LanguageManager;
import com.eiman.biblioteca.utils.ReportGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para la ventana de informes.
 * Este controlador maneja la generación de los diferentes informes de la biblioteca
 * como los informes de alumnos, libros, préstamos y el histórico de préstamos.
 */
public class InformesController {
    private static final Logger logger = Logger.getLogger(InformesController.class.getName());

    @FXML private Button btnInformeAlumnos, btnInformeLibros, btnInformePrestamos, btnInformeHistorico;

    /**
     * Inicializa la ventana de informes configurando los tooltips de los botones.
     * Los tooltips se asignan dinámicamente desde el archivo de idioma.
     */
    @FXML
    private void initialize() {
        logger.info("Inicializando la ventana de informes.");
        try {
            btnInformeAlumnos.setTooltip(new Tooltip(LanguageManager.getProperty("button.informeAlumnos")));
            btnInformeLibros.setTooltip(new Tooltip(LanguageManager.getProperty("button.informeLibros")));
            btnInformePrestamos.setTooltip(new Tooltip(LanguageManager.getProperty("button.informePrestamos")));
            btnInformeHistorico.setTooltip(new Tooltip(LanguageManager.getProperty("button.informeHistorico")));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar la ventana de informes.", e);
        }
    }

    /**
     * Genera el informe de alumnos utilizando el generador de informes.
     */
    @FXML
    private void generarInformeAlumnos() {
        logger.info("Generando informe de alumnos.");
        generarInforme("reporte_alumnos", new HashMap<>());
    }

    /**
     * Genera el informe de libros utilizando el generador de informes.
     */
    @FXML
    private void generarInformeLibros() {
        logger.info("Generando informe de libros.");
        generarInforme("listado_libros", new HashMap<>());
    }

    /**
     * Genera el informe de préstamos utilizando el generador de informes.
     */
    @FXML
    private void generarInformePrestamos() {
        logger.info("Generando informe de préstamos.");
        generarInforme("prestamo_informe", new HashMap<>());
    }

    /**
     * Genera el informe del histórico de préstamos utilizando el generador de informes.
     */
    @FXML
    private void generarInformeHistorico() {
        logger.info("Generando informe del histórico de préstamos.");
        generarInforme("estadisticas_prestamos", new HashMap<>());
    }

    /**
     * Llama al generador de informes para generar un informe específico.
     * @param nombreReporte El nombre del reporte que se generará.
     * @param parametros Parámetros adicionales para el informe.
     */
    private void generarInforme(String nombreReporte, Map<String, Object> parametros) {
        try {
            logger.info("Generando informe: " + nombreReporte);
            ReportGenerator.generateReport(nombreReporte, null, parametros);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al generar el informe: " + nombreReporte, e);
        }
    }
}
