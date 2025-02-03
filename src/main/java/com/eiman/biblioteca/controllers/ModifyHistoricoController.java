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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para la ventana de modificación del historial de préstamos.
 * Este controlador permite modificar la fecha y hora de devolución de un libro,
 * así como actualizar el estado del libro en el historial de préstamos.
 */
public class ModifyHistoricoController {
    private static final Logger logger = Logger.getLogger(ModifyHistoricoController.class.getName());

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
        logger.info("Inicializando la ventana de modificación del historial de préstamos.");
        try {
            dateDevolucion.setTooltip(new Tooltip(LanguageManager.getProperty("fecha.devolucion")));
            spinnerHora.setTooltip(new Tooltip(LanguageManager.getProperty("hora.devolucion")));
            spinnerMinutos.setTooltip(new Tooltip(LanguageManager.getProperty("minutos.devolucion")));
            choiceEstadoLibro.setTooltip(new Tooltip(LanguageManager.getProperty("selecciona.estado")));
            btnGuardar.setTooltip(new Tooltip(LanguageManager.getProperty("guardar")));
            btnCancelar.setTooltip(new Tooltip(LanguageManager.getProperty("cancelar")));

            choiceEstadoLibro.setItems(FXCollections.observableArrayList(
                    LanguageManager.getProperty("nuevo"),
                    LanguageManager.getProperty("usado.nuevo"),
                    LanguageManager.getProperty("usado.seminuevo"),
                    LanguageManager.getProperty("usado.estropeado"),
                    LanguageManager.getProperty("restaurado")
            ));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar la ventana de modificación del historial de préstamos.", e);
        }
    }

    /**
     * Establece el historial de préstamo a modificar.
     * Carga la información del alumno, libro, fecha de devolución y estado del libro en los campos correspondientes.
     * @param historico El historial de préstamo a modificar.
     */
    public void setHistorico(HistoricoPrestamo historico) {
        logger.info("Cargando datos del historial de préstamo para modificación.");
        try {
            this.historicoActual = historico;

            Alumno alumno = alumnoDAO.obtenerAlumnoPorDni(historico.getDniAlumno());
            lblAlumno.setText((LanguageManager.getProperty("alumno")) + " " +
                    (alumno != null ? alumno.getNombre() + " " + alumno.getApellido1() : LanguageManager.getProperty("desconocido")));

            Libro libro = libroDAO.obtenerLibroPorCodigo(historico.getCodigoLibro());
            lblLibro.setText((LanguageManager.getProperty("libro")) + " " +
                    (libro != null ? libro.getTitulo() : LanguageManager.getProperty("desconocido")));

            dateDevolucion.setValue(historico.getFechaDevolucion().toLocalDate());
            spinnerHora.getValueFactory().setValue(historico.getFechaDevolucion().getHour());
            spinnerMinutos.getValueFactory().setValue(historico.getFechaDevolucion().getMinute());

            if (libro != null) {
                choiceEstadoLibro.setValue(libro.getEstado());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar los datos del historial de préstamo.", e);
        }
    }

    /**
     * Guarda los cambios realizados en el historial de préstamo.
     * Actualiza la fecha de devolución y el estado del libro en la base de datos.
     */
    @FXML
    private void guardarCambios() {
        logger.info("Intentando guardar cambios en el historial de préstamo.");
        try {
            if (dateDevolucion.getValue() == null) {
                mostrarAlerta(LanguageManager.getProperty("selecciona.fecha.devolucion"));
                return;
            }

            LocalDateTime nuevaFechaDevolucion = LocalDateTime.of(
                    dateDevolucion.getValue(),
                    LocalTime.of(spinnerHora.getValue(), spinnerMinutos.getValue())
            );
            historicoActual.setFechaDevolucion(nuevaFechaDevolucion);

            String nuevoEstado = choiceEstadoLibro.getValue();
            if (nuevoEstado != null) {
                Libro libro = libroDAO.obtenerLibroPorCodigo(historicoActual.getCodigoLibro());
                if (libro != null) {
                    libro.setEstado(nuevoEstado);
                    libroDAO.actualizarLibro(libro);
                }
            }

            historicoPrestamoDAO.actualizarHistoricoPrestamo(historicoActual);

            logger.info("Historial de préstamo actualizado correctamente.");
            cerrarVentana();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al guardar cambios en el historial de préstamo.", e);
        }
    }

    /**
     * Cancela la operación y cierra la ventana sin guardar cambios.
     */
    @FXML
    private void cancelar() {
        logger.info("Operación cancelada por el usuario.");
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        logger.info("Cerrando ventana de modificación del historial de préstamo.");
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta de advertencia con el mensaje proporcionado.
     * @param mensaje El mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        logger.warning("Mostrando alerta: " + mensaje);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(LanguageManager.getProperty("advertencia"));
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
