package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.AlumnoDAO;
import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.utils.LanguageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para la gestión de los alumnos.
 * Este controlador maneja la interfaz de usuario relacionada con la adición y modificación de datos de alumnos.
 * Permite insertar nuevos alumnos o actualizar los existentes.
 */
public class AlumnoController {
    private static final Logger logger = Logger.getLogger(AlumnoController.class.getName());

    @FXML private TextField txtDni, txtNombre, txtApellido1, txtApellido2;
    @FXML private Button btnGuardar, btnCancelar;

    private BibliotecaController bibliotecaController;

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private Alumno alumnoActual;

    /**
     * Inicializa los tooltips de los campos de entrada y botones.
     * Asigna los textos de los tooltips desde el archivo de configuración de idioma.
     */
    @FXML
    private void initialize() {
        logger.info("Inicializando tooltips en AlumnoController");
        try {
            txtDni.setTooltip(new Tooltip(LanguageManager.getProperty("introduce.dni")));
            txtNombre.setTooltip(new Tooltip(LanguageManager.getProperty("introduce.nombre")));
            txtApellido1.setTooltip(new Tooltip(LanguageManager.getProperty("introduce.apellido1")));
            txtApellido2.setTooltip(new Tooltip(LanguageManager.getProperty("introduce.apellido2")));

            btnGuardar.setTooltip(new Tooltip(LanguageManager.getProperty("guardar")));
            btnCancelar.setTooltip(new Tooltip(LanguageManager.getProperty("cancelar")));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar los tooltips en AlumnoController", e);
        }
    }

    /**
     * Establece los datos de un alumno en los campos del formulario.
     * Si el alumno no es nulo, se cargan sus datos en los campos de texto.
     * @param alumno El alumno a cargar.
     */
    public void setAlumno(Alumno alumno) {
        logger.info("Cargando datos del alumno en el formulario");
        this.alumnoActual = alumno;
        if (alumno != null) {
            txtDni.setText(alumno.getDni());
            txtNombre.setText(alumno.getNombre());
            txtApellido1.setText(alumno.getApellido1());
            txtApellido2.setText(alumno.getApellido2());
            txtDni.setDisable(true);  // Desactiva el campo DNI para evitar su modificación
        }
    }

    /**
     * Guarda los datos del alumno. Si el alumno no existe, se crea uno nuevo,
     * de lo contrario, se actualiza el existente.
     * Actualiza la tabla después de guardar los cambios y cierra la ventana.
     */
    @FXML
    private void guardarAlumno() {
        try {
            if (alumnoActual == null) {
                alumnoActual = new Alumno(txtDni.getText(), txtNombre.getText(), txtApellido1.getText(), txtApellido2.getText());
                logger.info("Insertando nuevo alumno: " + alumnoActual);
                alumnoDAO.insertarAlumno(alumnoActual);
            } else {
                alumnoActual.setNombre(txtNombre.getText());
                alumnoActual.setApellido1(txtApellido1.getText());
                alumnoActual.setApellido2(txtApellido2.getText());
                logger.info("Actualizando alumno existente: " + alumnoActual);
                alumnoDAO.actualizarAlumno(alumnoActual);
            }

            // Actualizar la tabla después de guardar
            if (bibliotecaController != null) {
                bibliotecaController.actualizarTablaActual();
                logger.info("Tabla de alumnos actualizada en BibliotecaController");
            }

            cerrarVentana();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al guardar los datos del alumno", e);
        }
    }

    /**
     * Establece el controlador principal de la ventana de la biblioteca.
     * @param bibliotecaController El controlador principal de la ventana.
     */
    public void setBibliotecaController(BibliotecaController bibliotecaController) {
        logger.info("Estableciendo referencia a BibliotecaController");
        this.bibliotecaController = bibliotecaController;
    }

    /**
     * Cancela la operación y cierra la ventana sin guardar los cambios.
     */
    @FXML
    private void cancelar() {
        logger.info("Operación cancelada por el usuario");
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        logger.info("Cerrando ventana del formulario de Alumno");
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
