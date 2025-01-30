package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.AlumnoDAO;
import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.dao.PrestamoDAO;
import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.models.Prestamo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class PrestamoController {
    @FXML private ComboBox<Alumno> comboAlumnos;
    @FXML private ComboBox<Libro> comboLibros;
    @FXML private DatePicker datePrestamo;
    @FXML private Spinner<Integer> spinnerHora;
    @FXML private Spinner<Integer> spinnerMinutos;
    @FXML private Button btnGuardar, btnCancelar;

    private BibliotecaController bibliotecaController;
    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private final LibroDAO libroDAO = new LibroDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private Prestamo prestamoActual;

    @FXML
    private void initialize() {
        cargarAlumnos();
        cargarLibrosDisponibles();

        // Configurar los spinners de hora y minutos
        datePrestamo.setValue(LocalDate.now());
        spinnerHora.getValueFactory().setValue(LocalTime.now().getHour());
        spinnerMinutos.getValueFactory().setValue(LocalTime.now().getMinute());
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamoActual = prestamo;
        if (prestamo != null) {
            comboAlumnos.setValue(alumnoDAO.obtenerAlumnoPorDni(prestamo.getDniAlumno()));
            comboLibros.setValue(libroDAO.obtenerLibroPorCodigo(prestamo.getCodigoLibro()));
            datePrestamo.setValue(prestamo.getFechaPrestamo().toLocalDate());
            spinnerHora.getValueFactory().setValue(prestamo.getFechaPrestamo().getHour());
            spinnerMinutos.getValueFactory().setValue(prestamo.getFechaPrestamo().getMinute());
        }
    }

    private void cargarAlumnos() {
        List<Alumno> alumnos = alumnoDAO.obtenerTodosLosAlumnos();
        comboAlumnos.setItems(FXCollections.observableArrayList(alumnos));
    }

    private void cargarLibrosDisponibles() {
        List<Libro> libros = libroDAO.obtenerTodosLosLibros();
        List<Integer> librosPrestados = prestamoDAO.obtenerTodosLosPrestamos().stream()
                .map(Prestamo::getCodigoLibro)
                .collect(Collectors.toList());

        comboLibros.setItems(FXCollections.observableArrayList(
                libros.stream()
                        .filter(libro -> libro.getBaja() == 0 && !librosPrestados.contains(libro.getCodigo()))
                        .collect(Collectors.toList())
        ));
    }

    @FXML
    private void guardarPrestamo() {
        Alumno alumnoSeleccionado = comboAlumnos.getValue();
        Libro libroSeleccionado = comboLibros.getValue();

        if (alumnoSeleccionado == null || libroSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un alumno y un libro.");
            return;
        }

        LocalDate fechaSeleccionada = datePrestamo.getValue();
        if (fechaSeleccionada == null) {
            mostrarAlerta("Debe seleccionar una fecha de préstamo.");
            return;
        }

        int horaSeleccionada = spinnerHora.getValue();
        int minutosSeleccionados = spinnerMinutos.getValue();
        LocalTime horaPrestamo = LocalTime.of(horaSeleccionada, minutosSeleccionados);

        LocalDateTime fechaHoraPrestamo = LocalDateTime.of(fechaSeleccionada, horaPrestamo);

        if (prestamoActual == null) {
            prestamoActual = new Prestamo(0, alumnoSeleccionado.getDni(), libroSeleccionado.getCodigo(), fechaHoraPrestamo);
            prestamoDAO.insertarPrestamo(prestamoActual);
        } else {
            prestamoActual.setDniAlumno(alumnoSeleccionado.getDni());
            prestamoActual.setCodigoLibro(libroSeleccionado.getCodigo());
            prestamoActual.setFechaPrestamo(fechaHoraPrestamo);
            prestamoDAO.actualizarPrestamo(prestamoActual);
        }

        // Actualizar la tabla después de guardar
        if (bibliotecaController != null) {
            bibliotecaController.actualizarTablaActual();
        }

        cerrarVentana();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setBibliotecaController(BibliotecaController bibliotecaController) {
        this.bibliotecaController = bibliotecaController;
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
