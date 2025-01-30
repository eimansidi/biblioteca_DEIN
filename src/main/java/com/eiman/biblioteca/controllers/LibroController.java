package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.models.Libro;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class LibroController {
    @FXML private TextField txtTitulo, txtAutor, txtEditorial;
    @FXML private ChoiceBox<String> choiceEstado;
    @FXML private CheckBox chkBaja;
    @FXML private Button btnGuardar, btnCancelar, btnSelectPortada, btnBorrarPortada;
    @FXML private ImageView imgPortada; // Agregado para mostrar la imagen seleccionada

    private BibliotecaController bibliotecaController;

    private final LibroDAO libroDAO = new LibroDAO();
    private Libro libroActual;
    private byte[] portada;

    public void setLibro(Libro libro) {
        this.libroActual = libro;
        if (libro != null) {
            txtTitulo.setText(libro.getTitulo());
            txtAutor.setText(libro.getAutor());
            txtEditorial.setText(libro.getEditorial());
            choiceEstado.setValue(libro.getEstado());
            chkBaja.setSelected(libro.getBaja() == 1);
            portada = libro.getPortada();

            if (portada != null) {
                imgPortada.setImage(new Image(new ByteArrayInputStream(portada)));
            }
        }
    }

    @FXML
    private void guardarLibro() {
        if (libroActual == null) {
            libroActual = new Libro(
                    0, // Código generado por la BD si es AUTO_INCREMENT
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    txtEditorial.getText(),
                    choiceEstado.getValue(),
                    chkBaja.isSelected() ? 1 : 0,
                    portada
            );
            libroDAO.insertarLibro(libroActual);
        } else {
            libroActual.setTitulo(txtTitulo.getText());
            libroActual.setAutor(txtAutor.getText());
            libroActual.setEditorial(txtEditorial.getText());
            libroActual.setEstado(choiceEstado.getValue());
            libroActual.setBaja(chkBaja.isSelected() ? 1 : 0);
            libroActual.setPortada(portada);
            libroDAO.actualizarLibro(libroActual);
        }

        // Actualizar la tabla después de guardar
        if (bibliotecaController != null) {
            bibliotecaController.actualizarTablaActual();
        }

        cerrarVentana();
    }

    @FXML
    private void selectPortada() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                portada = Files.readAllBytes(file.toPath());

                // Mostrar la imagen en el ImageView
                imgPortada.setImage(new Image(file.toURI().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void removePortada() {
        portada = null;
        imgPortada.setImage(null);
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
