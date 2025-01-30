package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.utils.LanguageManager;
import com.eiman.biblioteca.utils.ReportGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la ventana de informes.
 * Este controlador maneja la generación de los diferentes informes de la biblioteca
 * como los informes de alumnos, libros, préstamos y el histórico de préstamos.
 */
public class InformesController {
    @FXML private Button btnInformeAlumnos, btnInformeLibros, btnInformePrestamos, btnInformeHistorico;

    /**
     * Inicializa la ventana de informes configurando los tooltips de los botones.
     * Los tooltips se asignan dinámicamente desde el archivo de idioma.
     */
    @FXML
    private void initialize() {
        // Asignar tooltips manualmente desde el archivo de idioma
        btnInformeAlumnos.setTooltip(new Tooltip(LanguageManager.getProperty("button.informeAlumnos")));
        btnInformeLibros.setTooltip(new Tooltip(LanguageManager.getProperty("button.informeLibros")));
        btnInformePrestamos.setTooltip(new Tooltip(LanguageManager.getProperty("button.informePrestamos")));
        btnInformeHistorico.setTooltip(new Tooltip(LanguageManager.getProperty("button.informeHistorico")));
    }

    /**
     * Genera el informe de alumnos utilizando el generador de informes.
     */
    @FXML
    private void generarInformeAlumnos() {
        generarInforme("reporte_alumnos", new HashMap<>());
    }

    /**
     * Genera el informe de libros utilizando el generador de informes.
     */
    @FXML
    private void generarInformeLibros() {
        generarInforme("listado_libros", new HashMap<>());
    }

    /**
     * Genera el informe de préstamos utilizando el generador de informes.
     */
    @FXML
    private void generarInformePrestamos() {
        generarInforme("prestamo_informe", new HashMap<>());
    }

    /**
     * Genera el informe del histórico de préstamos utilizando el generador de informes.
     */
    @FXML
    private void generarInformeHistorico() {
        generarInforme("estadisticas_prestamos", new HashMap<>());
    }

    /**
     * Llama al generador de informes para generar un informe específico.
     * @param nombreReporte El nombre del reporte que se generará.
     * @param parametros Parámetros adicionales para el informe.
     */
    private void generarInforme(String nombreReporte, Map<String, Object> parametros) {
        // Genera el informe con el nombre y parámetros especificados
        ReportGenerator.generateReport(nombreReporte, null, parametros);
    }
}
