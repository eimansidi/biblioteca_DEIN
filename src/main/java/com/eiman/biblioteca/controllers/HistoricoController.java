package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.HistoricoPrestamoDAO;
import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.dao.PrestamoDAO;
import com.eiman.biblioteca.models.HistoricoPrestamo;
import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.models.Prestamo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    }

    private void configurarColumnas() {
        colIdPrestamo.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
        colDniAlumno.setCellValueFactory(new PropertyValueFactory<>("dniAlumno"));
        colCodigoLibro.setCellValueFactory(new PropertyValueFactory<>("codigoLibro"));
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
    }

    private void cargarPrestamosActivos() {
        List<Prestamo> prestamosActivos = prestamoDAO.obtenerPrestamosActivos();
        tablePrestamos.setItems(FXCollections.observableArrayList(prestamosActivos));
        tablePrestamos.refresh();
    }

    private void cargarEstadoLibro(int codigoLibro) {
        Libro libro = libroDAO.obtenerLibroPorCodigo(codigoLibro);
        if (libro != null) {
            choiceEstadoLibro.setValue(libro.getEstado());
        }
    }

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

        // Insertar en el historial con la fecha y hora de devolución seleccionadas
        HistoricoPrestamo historico = new HistoricoPrestamo(
                prestamoSeleccionado.getIdPrestamo(),
                prestamoSeleccionado.getDniAlumno(),
                prestamoSeleccionado.getCodigoLibro(),
                prestamoSeleccionado.getFechaPrestamo(),
                LocalDateTime.of(fechaSeleccionada, horaDevolucion)
        );

        historicoPrestamoDAO.insertarHistoricoPrestamo(historico);

        // Eliminar el préstamo de la tabla de préstamos activos
        prestamoDAO.eliminarPrestamo(prestamoSeleccionado.getIdPrestamo());

        // Actualizar el estado del libro en la base de datos
        Libro libro = libroDAO.obtenerLibroPorCodigo(prestamoSeleccionado.getCodigoLibro());
        if (libro != null) {
            libro.setEstado(nuevoEstado);
            libroDAO.actualizarLibro(libro);
        }

        // Recargar la tabla de préstamos activos y la de históricos
        cargarPrestamosActivos();
        if (bibliotecaController != null) {
            bibliotecaController.actualizarTablaActual();
        }
    }

    public void setBibliotecaController(BibliotecaController bibliotecaController) {
        this.bibliotecaController = bibliotecaController;
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
