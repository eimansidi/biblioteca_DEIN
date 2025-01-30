package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.AlumnoDAO;
import com.eiman.biblioteca.dao.HistoricoPrestamoDAO;
import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.models.HistoricoPrestamo;
import com.eiman.biblioteca.models.Libro;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public void setHistorico(HistoricoPrestamo historico) {
        this.historicoActual = historico;

        // Obtener el nombre del alumno
        Alumno alumno = alumnoDAO.obtenerAlumnoPorDni(historico.getDniAlumno());
        lblAlumno.setText("Alumno: " + (alumno != null ? alumno.getNombre() + " " + alumno.getApellido1() : "Desconocido"));

        // Obtener el título del libro
        Libro libro = libroDAO.obtenerLibroPorCodigo(historico.getCodigoLibro());
        lblLibro.setText("Libro: " + (libro != null ? libro.getTitulo() : "Desconocido"));

        // Cargar fecha y hora
        dateDevolucion.setValue(historico.getFechaDevolucion().toLocalDate());
        spinnerHora.getValueFactory().setValue(historico.getFechaDevolucion().getHour());
        spinnerMinutos.getValueFactory().setValue(historico.getFechaDevolucion().getMinute());

        // Cargar estado del libro
        if (libro != null) {
            choiceEstadoLibro.setValue(libro.getEstado());
        }
    }

    @FXML
    private void guardarCambios() {
        if (dateDevolucion.getValue() == null) {
            mostrarAlerta("Debe seleccionar una fecha de devolución.");
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

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
