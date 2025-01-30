package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.AlumnoDAO;
import com.eiman.biblioteca.dao.HistoricoPrestamoDAO;
import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.models.HistoricoPrestamo;
import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.utils.LanguageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Controlador para la ventana de modificación del historial de préstamos.
 * Este controlador permite modificar la fecha y hora de devolución de un libro,
 * así como actualizar el estado del libro en el historial de préstamos.
 */
public class ModifyHistoricoController {

    @FXML private Label lblAlumno;
    @FXML private Label lblLibro;
    @FXML private DatePicker dateDevolucion;
    @FXML private Spinner<Integer> spinnerHora;
    @FXML private Spinner<Integer> spinnerMinutos;
    @FXML private ChoiceBox<String> choiceEstadoLibro;
    @FXML private Button btnGuardar, btnCancelar;

    private final HistoricoPrestamoDAO historicoPrestamoDAO = new HistoricoPrestamoDAO();
    private final LibroDAO libroDAO = new LibroDAO();
    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private HistoricoPrestamo historicoActual;

    /**
     * Inicializa los componentes de la interfaz de usuario, configurando los tooltips
     * de los campos y cargando los valores predeterminados desde el archivo de idioma.
     */
    public void initialize() {
        dateDevolucion.setTooltip(new Tooltip(LanguageManager.getProperty("fecha.devolucion")));
        spinnerHora.setTooltip(new Tooltip(LanguageManager.getProperty("hora.devolucion")));
        spinnerMinutos.setTooltip(new Tooltip(LanguageManager.getProperty("minutos.devolucion")));
        choiceEstadoLibro.setTooltip(new Tooltip(LanguageManager.getProperty("selecciona.estado")));
        btnGuardar.setTooltip(new Tooltip(LanguageManager.getProperty("guardar")));
        btnCancelar.setTooltip(new Tooltip(LanguageManager.getProperty("cancelar")));

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
     * Establece el historial de préstamo a modificar.
     * Carga la información del alumno, libro, fecha de devolución y estado del libro en los campos correspondientes.
     * @param historico El historial de préstamo a modificar.
     */
    public void setHistorico(HistoricoPrestamo historico) {
        this.historicoActual = historico;

        // Obtener el nombre del alumno
        Alumno alumno = alumnoDAO.obtenerAlumnoPorDni(historico.getDniAlumno());
        lblAlumno.setText((LanguageManager.getProperty("alumno")) + " " + (alumno != null ? alumno.getNombre() + " " + alumno.getApellido1() : (LanguageManager.getProperty("desconocido"))));

        // Obtener el título del libro
        Libro libro = libroDAO.obtenerLibroPorCodigo(historico.getCodigoLibro());
        lblLibro.setText((LanguageManager.getProperty("libro")) + " " + (libro != null ? libro.getTitulo() : (LanguageManager.getProperty("desconocido"))));

        // Cargar fecha y hora
        dateDevolucion.setValue(historico.getFechaDevolucion().toLocalDate());
        spinnerHora.getValueFactory().setValue(historico.getFechaDevolucion().getHour());
        spinnerMinutos.getValueFactory().setValue(historico.getFechaDevolucion().getMinute());

        // Cargar estado del libro
        if (libro != null) {
            choiceEstadoLibro.setValue(libro.getEstado());
        }
    }

    /**
     * Guarda los cambios realizados en el historial de préstamo.
     * Actualiza la fecha de devolución y el estado del libro en la base de datos.
     */
    @FXML
    private void guardarCambios() {
        if (dateDevolucion.getValue() == null) {
            mostrarAlerta(LanguageManager.getProperty("selecciona.fecha.devolucion")); // "Debe seleccionar una fecha de devolución."
            return;
        }

        // Actualizar la fecha y hora de devolución
        LocalDateTime nuevaFechaDevolucion = LocalDateTime.of(
                dateDevolucion.getValue(),
                LocalTime.of(spinnerHora.getValue(), spinnerMinutos.getValue())
        );
        historicoActual.setFechaDevolucion(nuevaFechaDevolucion);

        // Actualizar el estado del libro
        String nuevoEstado = choiceEstadoLibro.getValue();
        if (nuevoEstado != null) {
            Libro libro = libroDAO.obtenerLibroPorCodigo(historicoActual.getCodigoLibro());
            if (libro != null) {
                libro.setEstado(nuevoEstado);
                libroDAO.actualizarLibro(libro);
            }
        }

        // Guardar cambios en la base de datos
        historicoPrestamoDAO.actualizarHistoricoPrestamo(historicoActual);

        // Cerrar ventana
        cerrarVentana();
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
}
