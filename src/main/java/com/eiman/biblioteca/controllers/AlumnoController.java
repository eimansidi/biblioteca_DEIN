package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.AlumnoDAO;
import com.eiman.biblioteca.models.Alumno;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AlumnoController {
    @FXML private TextField txtDni, txtNombre, txtApellido1, txtApellido2;
    @FXML private Button btnGuardar, btnCancelar;

    private BibliotecaController bibliotecaController;

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private Alumno alumnoActual;

    public void setAlumno(Alumno alumno) {
        this.alumnoActual = alumno;
        if (alumno != null) {
            txtDni.setText(alumno.getDni());
            txtNombre.setText(alumno.getNombre());
            txtApellido1.setText(alumno.getApellido1());
            txtApellido2.setText(alumno.getApellido2());
            txtDni.setDisable(true);
        }
    }

    @FXML
    private void guardarAlumno() {
        if (alumnoActual == null) {
            alumnoActual = new Alumno(txtDni.getText(), txtNombre.getText(), txtApellido1.getText(), txtApellido2.getText());
            alumnoDAO.insertarAlumno(alumnoActual);
        } else {
            alumnoActual.setNombre(txtNombre.getText());
            alumnoActual.setApellido1(txtApellido1.getText());
            alumnoActual.setApellido2(txtApellido2.getText());
            alumnoDAO.actualizarAlumno(alumnoActual);
        }

        // Actualizar la tabla después de guardar
        if (bibliotecaController != null) {
            bibliotecaController.actualizarTablaActual();
        }

        cerrarVentana();
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