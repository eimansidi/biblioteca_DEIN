package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.utils.LanguageManager;
import javafx.collections.FXCollections;
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

/**
 * Controlador para la ventana de gestion de libros.
 * Este controlador maneja la edicion de los detalles de un libro,
 * incluyendo el titulo, autor, editorial, estado, baja y portada.
 */
public class LibroController {
    @FXML private TextField txtTitulo, txtAutor, txtEditorial;
    @FXML private CheckBox chkBaja;
    @FXML private Button btnGuardar, btnCancelar, btnSelectPortada, btnBorrarPortada;
    @FXML private ImageView imgPortada;

    private BibliotecaController bibliotecaController;

    private final LibroDAO libroDAO = new LibroDAO();
    private Libro libroActual;
    private byte[] portada;

    @FXML private ChoiceBox<String> choiceEstadoLibro;

    /**
     * Inicializa los componentes de la interfaz de usuario, configurando los tooltips de los botones
     * y cargando los valores predeterminados de los estados del libro desde el archivo de idioma.
     */
    public void initialize() {
        txtTitulo.setTooltip(new Tooltip(LanguageManager.getProperty("introduce.titulo")));
        txtAutor.setTooltip(new Tooltip(LanguageManager.getProperty("introduce.autor")));
        txtEditorial.setTooltip(new Tooltip(LanguageManager.getProperty("introduce.editorial")));
        choiceEstadoLibro.setTooltip(new Tooltip(LanguageManager.getProperty("selecciona.estado")));
        chkBaja.setTooltip(new Tooltip(LanguageManager.getProperty("baja")));
        btnSelectPortada.setTooltip(new Tooltip(LanguageManager.getProperty("selecciona.imagen")));
        btnBorrarPortada.setTooltip(new Tooltip(LanguageManager.getProperty("eliminar")));
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
     * Establece los datos del libro seleccionado en los campos del formulario.
     * Si el libro no es nulo, carga sus detalles en los campos correspondientes.
     * @param libro El libro a cargar.
     */
    public void setLibro(Libro libro) {
        this.libroActual = libro;
        if (libro != null) {
            txtTitulo.setText(libro.getTitulo());
            txtAutor.setText(libro.getAutor());
            txtEditorial.setText(libro.getEditorial());
            choiceEstadoLibro.setValue(libro.getEstado());
            chkBaja.setSelected(libro.getBaja() == 1);
            portada = libro.getPortada();

            if (portada != null) {
                imgPortada.setImage(new Image(new ByteArrayInputStream(portada)));
            }
        }
    }

    /**
     * Guarda el libro en la base de datos. Si el libro es nuevo, se inserta,
     * de lo contrario, se actualiza su informacion.
     * Si el libro pasa a baja=1, dejara de mostrarse en la tabla de libros tras recargarla.
     */
    @FXML
    private void guardarLibro() {
        if (libroActual == null) {
            // Libro nuevo
            libroActual = new Libro(
                    0, // Codigo generado por la BD si es AUTO_INCREMENT
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    txtEditorial.getText(),
                    choiceEstadoLibro.getValue(),
                    chkBaja.isSelected() ? 1 : 0,
                    portada
            );
            libroDAO.insertarLibro(libroActual);
        } else {
            // Actualizar libro existente
            libroActual.setTitulo(txtTitulo.getText());
            libroActual.setAutor(txtAutor.getText());
            libroActual.setEditorial(txtEditorial.getText());
            libroActual.setEstado(choiceEstadoLibro.getValue());
            libroActual.setBaja(chkBaja.isSelected() ? 1 : 0);
            libroActual.setPortada(portada);
            libroDAO.actualizarLibro(libroActual);
        }

        // Refrescar la tabla de la ventana principal para que el libro dado de baja desaparezca si baja=1
        if (bibliotecaController != null) {
            bibliotecaController.actualizarTablaActual();
        }

        cerrarVentana();
    }

    /**
     * Abre un selector de archivos para elegir una imagen de portada.
     * La imagen seleccionada se muestra en el campo ImageView y se guarda en la variable portada.
     */
    @FXML
    private void selectPortada() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                LanguageManager.getProperty("imagenes"), "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                portada = Files.readAllBytes(file.toPath());
                imgPortada.setImage(new Image(file.toURI().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Elimina la portada del libro, borrando la imagen y restableciendo el campo ImageView.
     */
    @FXML
    private void removePortada() {
        portada = null;
        imgPortada.setImage(null);
    }

    /**
     * Establece el controlador principal de la ventana de la biblioteca.
     * @param bibliotecaController El controlador principal de la ventana.
     */
    public void setBibliotecaController(BibliotecaController bibliotecaController) {
        this.bibliotecaController = bibliotecaController;
    }

    /**
     * Cancela la operacion y cierra la ventana sin guardar los cambios.
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
