package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.AlumnoDAO;
import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.dao.PrestamoDAO;
import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.models.Prestamo;
import com.eiman.biblioteca.utils.LanguageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la ventana de gestión de préstamos.
 * Este controlador maneja la creación y edición de préstamos de libros a los alumnos,
 * así como la selección de los libros disponibles y la configuración de la fecha y hora de préstamo.
 */
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

    /**
     * Inicializa los componentes de la interfaz de usuario, configurando los spinners para hora y minutos,
     * y cargando los alumnos y libros disponibles para préstamo.
     */
    @FXML
    private void initialize() {
        cargarAlumnos();
        cargarLibrosDisponibles();

        // Configurar los spinners de hora y minutos con los valores actuales
        datePrestamo.setValue(LocalDate.now());
        spinnerHora.getValueFactory().setValue(LocalTime.now().getHour());
        spinnerMinutos.getValueFactory().setValue(LocalTime.now().getMinute());

        // Asignar tooltips manualmente desde el archivo de idioma
        comboAlumnos.setTooltip(new Tooltip(LanguageManager.getProperty("selecciona.alumno")));
        comboLibros.setTooltip(new Tooltip(LanguageManager.getProperty("selecciona.libro")));
        datePrestamo.setTooltip(new Tooltip(LanguageManager.getProperty("fecha.prestamo")));
        spinnerHora.setTooltip(new Tooltip(LanguageManager.getProperty("hora.devolucion")));
        spinnerMinutos.setTooltip(new Tooltip(LanguageManager.getProperty("minutos.devolucion")));
        btnGuardar.setTooltip(new Tooltip(LanguageManager.getProperty("guardar")));
        btnCancelar.setTooltip(new Tooltip(LanguageManager.getProperty("cancelar")));
    }

    /**
     * Establece el préstamo que se está editando, cargando sus valores en los campos del formulario.
     * @param prestamo El préstamo a editar.
     */
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

    /**
     * Carga todos los alumnos disponibles en el ComboBox de alumnos.
     */
    private void cargarAlumnos() {
        List<Alumno> alumnos = alumnoDAO.obtenerTodosLosAlumnos();
        comboAlumnos.setItems(FXCollections.observableArrayList(alumnos));
    }

    /**
     * Carga los libros disponibles para préstamo, excluyendo los que están prestados o dados de baja.
     */
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

    /**
     * Guarda el préstamo, ya sea nuevo o editado, en la base de datos.
     * Si es un nuevo préstamo, se inserta, de lo contrario, se actualiza.
     */
    @FXML
    private void guardarPrestamo() {
        Alumno alumnoSeleccionado = comboAlumnos.getValue();
        Libro libroSeleccionado = comboLibros.getValue();

        if (alumnoSeleccionado == null || libroSeleccionado == null) {
            mostrarAlerta(LanguageManager.getProperty("selecciona.alumno.libro")); // "Debe seleccionar un alumno y un libro."
            return;
        }

        LocalDate fechaSeleccionada = datePrestamo.getValue();
        if (fechaSeleccionada == null) {
            mostrarAlerta(LanguageManager.getProperty("selecciona.fecha.prestamo")); // "Debe seleccionar una fecha de préstamo."
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

    /**
     * Muestra una alerta de advertencia con el mensaje proporcionado.
     * @param mensaje El mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(LanguageManager.getProperty("advertencia"));
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Establece el controlador principal de la ventana de la biblioteca.
     * @param bibliotecaController El controlador principal de la ventana.
     */
    public void setBibliotecaController(BibliotecaController bibliotecaController) {
        this.bibliotecaController = bibliotecaController;
    }

    /**
     * Cancela la operación y cierra la ventana sin guardar cambios.
     */
    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
