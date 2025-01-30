package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.utils.ReportGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.util.HashMap;
import java.util.Map;

public class InformesController {
    @FXML private Button btnInformeAlumnos, btnInformeLibros, btnInformePrestamos, btnInformeHistorico;

    @FXML
    private void generarInformeAlumnos() {
        generarInforme("reporte_alumnos", new HashMap<>());
    }

    @FXML
    private void generarInformeLibros() {
        generarInforme("listado_libros", new HashMap<>());
    }

    @FXML
    private void generarInformePrestamos() {
        generarInforme("prestamo_informe", new HashMap<>());
    }

    @FXML
    private void generarInformeHistorico() {
        generarInforme("estadisticas_prestamos", new HashMap<>());
    }

    private void generarInforme(String nombreReporte, Map<String, Object> parametros) {
        ReportGenerator.generateReport(nombreReporte, null, parametros);
    }
}