package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.HistoricoPrestamoDAO;
import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.dao.PrestamoDAO;
import com.eiman.biblioteca.models.HistoricoPrestamo;
import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.models.Prestamo;
import com.eiman.biblioteca.utils.LanguageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Controlador para la ventana del historial de préstamos.
 * Este controlador permite gestionar la devolución de libros,
 * mostrando los préstamos activos y gestionando la actualización de
 * su estado y su registro en el historial de préstamos.
 */
public class HistoricoController {
    @FXML private TableView<Prestamo> tablePrestamos;
    @FXML private TableColumn<Prestamo, Integer> colIdPrestamo;
    @FXML private TableColumn<Prestamo, String> colDniAlumno;
    @FXML private TableColumn<Prestamo, Integer> colCodigoLibro;
    @FXML private TableColumn<Prestamo, String> colFechaPrestamo;
    @FXML private ChoiceBox<String> choiceEstadoLibro;
    @FXML private DatePicker dateDevolucion;
    @FXML private Spinner<Integer> spinnerHora;
    @FXML private Spinner<Integer> spinnerMinutos;
    @FXML private Button btnDevolver;

    private BibliotecaController bibliotecaController;
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final HistoricoPrestamoDAO historicoPrestamoDAO = new HistoricoPrestamoDAO();
    private final LibroDAO libroDAO = new LibroDAO();

    /**
     * Inicializa la vista, configura las columnas de la tabla de préstamos
     * activos y carga los datos de los préstamos activos. También establece
     * los valores predeterminados para la fecha y hora de devolución.
     */
    @FXML
    private void initialize() {
        configurarColumnas();
        cargarPrestamosActivos();

        dateDevolucion.setValue(LocalDate.now());
        spinnerHora.getValueFactory().setValue(LocalTime.now().getHour());
        spinnerMinutos.getValueFactory().setValue(LocalTime.now().getMinute());

        tablePrestamos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarEstadoLibro(newSelection.getCodigoLibro());
            }
        });

        dateDevolucion.setTooltip(new Tooltip(LanguageManager.getProperty("fecha.devolucion")));
        spinnerHora.setTooltip(new Tooltip(LanguageManager.getProperty("hora.devolucion")));
        spinnerMinutos.setTooltip(new Tooltip(LanguageManager.getProperty("minutos.devolucion")));
        choiceEstadoLibro.setTooltip(new Tooltip(LanguageManager.getProperty("selecciona.estado")));
        btnDevolver.setTooltip(new Tooltip(LanguageManager.getProperty("devolver")));

        // Cargar valores desde el archivo de idioma para los estados de los libros
        choiceEstadoLibro.setItems(FXCollections.observableArrayList(
                LanguageManager.getProperty("nuevo"),
                LanguageManager.getProperty("usado.nuevo"),
                LanguageManager.getProperty("usado.seminuevo"),
                LanguageManager.getProperty("usado.estropeado"),
                LanguageManager.getProperty("restaurado")
        ));
    }

    /**
     * Configura las columnas de la tabla de préstamos, vinculando cada
     * columna con un atributo del objeto `Prestamo`.
     */
    private void configurarColumnas() {
        colIdPrestamo.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
        colDniAlumno.setCellValueFactory(new PropertyValueFactory<>("dniAlumno"));
        colCodigoLibro.setCellValueFactory(new PropertyValueFactory<>("codigoLibro"));
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
    }

    /**
     * Carga los préstamos activos en la tabla.
     * Los préstamos activos se obtienen desde la base de datos.
     */
    private void cargarPrestamosActivos() {
        List<Prestamo> prestamosActivos = prestamoDAO.obtenerPrestamosActivos();
        tablePrestamos.setItems(FXCollections.observableArrayList(prestamosActivos));
        tablePrestamos.refresh();
    }

    /**
     * Carga el estado del libro basado en el código del libro del préstamo seleccionado.
     * Actualiza el campo `choiceEstadoLibro` con el estado actual del libro.
     * @param codigoLibro El código del libro cuyo estado se desea cargar.
     */
    private void cargarEstadoLibro(int codigoLibro) {
        Libro libro = libroDAO.obtenerLibroPorCodigo(codigoLibro);
        if (libro != null) {
            choiceEstadoLibro.setValue(libro.getEstado());
        }
    }

    /**
     * Maneja la devolución de un libro, registrando el préstamo en el historial,
     * eliminando el préstamo activo y actualizando el estado del libro en la base de datos.
     */
    @FXML
    private void devolverLibro() {
        Prestamo prestamoSeleccionado = tablePrestamos.getSelectionModel().getSelectedItem();
        if (prestamoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un préstamo para devolver.");
            return;
        }

        String nuevoEstado = choiceEstadoLibro.getValue();
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            mostrarAlerta("Debe seleccionar el estado del libro.");
            return;
        }

        LocalDate fechaSeleccionada = dateDevolucion.getValue();
        if (fechaSeleccionada == null) {
            mostrarAlerta("Debe seleccionar una fecha de devolución.");
            return;
        }

        int horaSeleccionada = spinnerHora.getValue();
        int minutosSeleccionados = spinnerMinutos.getValue();
        LocalTime horaDevolucion = LocalTime.of(horaSeleccionada, minutosSeleccionados);

        // Crear un objeto HistoricoPrestamo con la fecha y hora de devolución seleccionadas
        HistoricoPrestamo historico = new HistoricoPrestamo(
                prestamoSeleccionado.getIdPrestamo(),
                prestamoSeleccionado.getDniAlumno(),
                prestamoSeleccionado.getCodigoLibro(),
                prestamoSeleccionado.getFechaPrestamo(),
                LocalDateTime.of(fechaSeleccionada, horaDevolucion)
        );

        // Insertar el historial de préstamo
        historicoPrestamoDAO.insertarHistoricoPrestamo(historico);

        // Eliminar el préstamo activo
        prestamoDAO.eliminarPrestamo(prestamoSeleccionado.getIdPrestamo());

        // Actualizar el estado del libro en la base de datos
        Libro libro = libroDAO.obtenerLibroPorCodigo(prestamoSeleccionado.getCodigoLibro());
        if (libro != null) {
            libro.setEstado(nuevoEstado);
            libroDAO.actualizarLibro(libro);
        }

        // Recargar la tabla de préstamos activos y la tabla de históricos
        cargarPrestamosActivos();
        if (bibliotecaController != null) {
            bibliotecaController.actualizarTablaActual();
        }
    }

    /**
     * Establece el controlador principal de la ventana de la biblioteca.
     * @param bibliotecaController El controlador principal de la ventana.
     */
    public void setBibliotecaController(BibliotecaController bibliotecaController) {
        this.bibliotecaController = bibliotecaController;
    }

    /**
     * Muestra una alerta de advertencia con el mensaje proporcionado.
     * @param mensaje El mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
