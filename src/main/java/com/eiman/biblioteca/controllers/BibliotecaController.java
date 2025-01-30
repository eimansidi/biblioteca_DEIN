package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.AlumnoDAO;
import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.dao.PrestamoDAO;
import com.eiman.biblioteca.dao.HistoricoPrestamoDAO;
import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.models.Prestamo;
import com.eiman.biblioteca.models.HistoricoPrestamo;
import com.eiman.biblioteca.utils.LanguageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador principal de la ventana de la biblioteca.
 * Este controlador gestiona la navegación entre las diferentes vistas (Alumnos, Libros, Préstamos, Históricos y Informes).
 * También maneja la carga y actualización de las tablas correspondientes, así como las operaciones de adición, modificación y eliminación.
 */
public class BibliotecaController {
    @FXML private Button btnAlumnos, btnLibros, btnPrestamos, btnHistoricoPrestamos, btnInformes;
    @FXML private TableView<Object> tableView;
    @FXML private MenuBar menuBar;
    @FXML private Button btnAñadir, btnModificar, btnEliminar;

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private final LibroDAO libroDAO = new LibroDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final HistoricoPrestamoDAO historicoPrestamoDAO = new HistoricoPrestamoDAO();

    private String vistaActual = "alumnos";

    /**
     * Inicializa la interfaz de la biblioteca, configurando las tooltips para cada botón y configurando la tabla de alumnos.
     * También deshabilita los botones de modificación y eliminación por defecto.
     */
    @FXML
    private void initialize() {
        openAlumnosTable();
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean seleccion = newSelection != null;
            btnModificar.setDisable(!seleccion);
            btnEliminar.setDisable(!seleccion);
        });

        btnAlumnos.setTooltip(new Tooltip(LanguageManager.getProperty("cargar.tabla.alumnos")));
        btnLibros.setTooltip(new Tooltip(LanguageManager.getProperty("cargar.tabla.libros")));
        btnPrestamos.setTooltip(new Tooltip(LanguageManager.getProperty("cargar.tabla.prestamos")));
        btnHistoricoPrestamos.setTooltip(new Tooltip(LanguageManager.getProperty("cargar.tabla.historicos")));
        btnInformes.setTooltip(new Tooltip(LanguageManager.getProperty("cargar.informes")));

        btnAñadir.setTooltip(new Tooltip(LanguageManager.getProperty("añadir")));
        btnModificar.setTooltip(new Tooltip(LanguageManager.getProperty("modificar")));
        btnEliminar.setTooltip(new Tooltip(LanguageManager.getProperty("eliminar")));
    }

    /**
     * Carga la tabla de alumnos en la vista principal.
     * Asigna las columnas de la tabla con la información de los alumnos y actualiza la vista.
     */
    @FXML
    private void openAlumnosTable() {
        vistaActual = "alumnos";
        tableView.getColumns().clear();
        tableView.setItems(FXCollections.observableArrayList());

        TableColumn<Object, String> colDni = new TableColumn<>(LanguageManager.getProperty("dni"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        TableColumn<Object, String> colNombre = new TableColumn<>(LanguageManager.getProperty("nombre"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Object, String> colApellido1 = new TableColumn<>(LanguageManager.getProperty("apellido1"));
        colApellido1.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        TableColumn<Object, String> colApellido2 = new TableColumn<>(LanguageManager.getProperty("apellido2"));
        colApellido2.setCellValueFactory(new PropertyValueFactory<>("apellido2"));

        tableView.getColumns().addAll(colDni, colNombre, colApellido1, colApellido2);
        tableView.setItems(FXCollections.observableArrayList(alumnoDAO.obtenerTodosLosAlumnos()));

        btnAñadir.setDisable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    /**
     * Carga la tabla de libros en la vista principal.
     * Asigna las columnas de la tabla con la información de los libros y actualiza la vista.
     */
    @FXML
    private void openLibrosTable() {
        vistaActual = "libros";
        tableView.getColumns().clear();
        tableView.setItems(FXCollections.observableArrayList());

        TableColumn<Object, Integer> colCodigo = new TableColumn<>(LanguageManager.getProperty("codigo"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<Object, String> colTitulo = new TableColumn<>(LanguageManager.getProperty("titulo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        TableColumn<Object, String> colAutor = new TableColumn<>(LanguageManager.getProperty("autor"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        TableColumn<Object, String> colEditorial = new TableColumn<>(LanguageManager.getProperty("editorial"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        TableColumn<Object, String> colEstado = new TableColumn<>(LanguageManager.getProperty("estado"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        TableColumn<Object, String> colBaja = new TableColumn<>(LanguageManager.getProperty("baja"));
        colBaja.setCellValueFactory(new PropertyValueFactory<>("baja"));

        tableView.getColumns().addAll(colCodigo, colTitulo, colAutor, colEditorial, colEstado, colBaja);
        tableView.setItems(FXCollections.observableArrayList(libroDAO.obtenerTodosLosLibros()));

        btnAñadir.setDisable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    /**
     * Carga la tabla de préstamos en la vista principal.
     * Asigna las columnas de la tabla con la información de los préstamos y actualiza la vista.
     */
    @FXML
    private void openPrestamosTable() {
        vistaActual = "prestamos";
        tableView.getColumns().clear();
        tableView.setItems(FXCollections.observableArrayList());

        TableColumn<Object, Integer> colId = new TableColumn<>(LanguageManager.getProperty("id.prestamo"));
        colId.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
        TableColumn<Object, String> colDniAlumno = new TableColumn<>(LanguageManager.getProperty("dni.alumno"));
        colDniAlumno.setCellValueFactory(new PropertyValueFactory<>("dniAlumno"));
        TableColumn<Object, Integer> colCodigoLibro = new TableColumn<>(LanguageManager.getProperty("codigo.libro"));
        colCodigoLibro.setCellValueFactory(new PropertyValueFactory<>("codigoLibro"));
        TableColumn<Object, String> colFechaPrestamo = new TableColumn<>(LanguageManager.getProperty("fecha.prestamo"));
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));

        tableView.getColumns().setAll(colId, colDniAlumno, colCodigoLibro, colFechaPrestamo);
        tableView.setItems(FXCollections.observableArrayList(prestamoDAO.obtenerTodosLosPrestamos()));

        btnAñadir.setDisable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    /**
     * Carga la tabla del historial de préstamos en la vista principal.
     * Asigna las columnas de la tabla con la información de los préstamos históricos y actualiza la vista.
     */
    @FXML
    private void openHistoricoPrestamosTable() {
        vistaActual = "historico_prestamos";
        tableView.getColumns().clear();
        tableView.setItems(FXCollections.observableArrayList());

        TableColumn<Object, Integer> colId = new TableColumn<>(LanguageManager.getProperty("id.prestamo"));
        colId.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
        TableColumn<Object, String> colDniAlumno = new TableColumn<>(LanguageManager.getProperty("id.alumno"));
        colDniAlumno.setCellValueFactory(new PropertyValueFactory<>("dniAlumno"));
        TableColumn<Object, Integer> colCodigoLibro = new TableColumn<>(LanguageManager.getProperty("codigo.libro"));
        colCodigoLibro.setCellValueFactory(new PropertyValueFactory<>("codigoLibro"));
        TableColumn<Object, String> colFechaPrestamo = new TableColumn<>(LanguageManager.getProperty("fecha.prestamo"));
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        TableColumn<Object, String> colFechaDevolucion = new TableColumn<>(LanguageManager.getProperty("fecha.devolucion"));
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

        tableView.getColumns().setAll(colId, colDniAlumno, colCodigoLibro, colFechaPrestamo, colFechaDevolucion);
        tableView.setItems(FXCollections.observableArrayList(historicoPrestamoDAO.obtenerTodosLosHistoricos()));

        btnAñadir.setDisable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    /**
     * Abre la ventana de informes para generar y ver los informes disponibles.
     */
    @FXML
    private void openInformesWindow() {
        abrirVentana("/fxml/informes.fxml", (LanguageManager.getProperty("informes")));
    }

    /**
     * Actualiza la tabla actual basada en la vista seleccionada.
     */
    public void actualizarTablaActual() {
        switch (vistaActual) {
            case "alumnos" -> openAlumnosTable();
            case "libros" -> openLibrosTable();
            case "prestamos" -> openPrestamosTable();
            case "historico_prestamos" -> openHistoricoPrestamosTable();
        }
    }

    /**
     * Abre la ventana para añadir un nuevo elemento dependiendo de la vista actual (Alumno, Libro, Préstamo o Histórico).
     */
    @FXML
    private void openAddWindow() {
        switch (vistaActual) {
            case "alumnos" -> abrirVentana("/fxml/alumnos.fxml", (LanguageManager.getProperty("añadir.alumno")));
            case "libros" -> abrirVentana("/fxml/libros.fxml", (LanguageManager.getProperty("añadir.libro")));
            case "prestamos" -> abrirVentana("/fxml/prestamos.fxml", (LanguageManager.getProperty("añadir.prestamo")));
            case "historico_prestamos" -> abrirVentana("/fxml/historicos.fxml", (LanguageManager.getProperty("devolver.libro")));
        }
    }

    /**
     * Abre la ventana de modificación del elemento seleccionado en la tabla actual.
     * Permite modificar alumnos, libros, préstamos o históricos.
     */
    @FXML
    private void openModifyWindow() {
        Object seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        try {
            FXMLLoader loader = null;
            Stage stage = new Stage();

            if (seleccionado instanceof Alumno alumno) {
                loader = new FXMLLoader(getClass().getResource("/fxml/alumnos.fxml"),
                        ResourceBundle.getBundle("i18n/messages", LanguageManager.getLocale()));
                stage.setTitle(LanguageManager.getProperty("modificar.alumno"));
            } else if (seleccionado instanceof Libro libro) {
                loader = new FXMLLoader(getClass().getResource("/fxml/libros.fxml"),
                        ResourceBundle.getBundle("i18n/messages", LanguageManager.getLocale()));
                stage.setTitle(LanguageManager.getProperty("modificar.libro"));
            } else if (seleccionado instanceof Prestamo prestamo) {
                loader = new FXMLLoader(getClass().getResource("/fxml/prestamos.fxml"),
                        ResourceBundle.getBundle("i18n/messages", LanguageManager.getLocale()));
                stage.setTitle(LanguageManager.getProperty("modificar.prestamo"));
            } else if (seleccionado instanceof HistoricoPrestamo historico) {
                loader = new FXMLLoader(getClass().getResource("/fxml/modify_historicos.fxml"),
                        ResourceBundle.getBundle("i18n/messages", LanguageManager.getLocale()));
                stage.setTitle(LanguageManager.getProperty("modificar.devolucion"));
            }

            if (loader != null) {
                stage.setScene(new Scene(loader.load()));
                Object controller = loader.getController();

                if (controller instanceof AlumnoController alumnoController) {
                    alumnoController.setAlumno((Alumno) seleccionado);
                    alumnoController.setBibliotecaController(this);
                } else if (controller instanceof LibroController libroController) {
                    libroController.setLibro((Libro) seleccionado);
                } else if (controller instanceof PrestamoController prestamoController) {
                    prestamoController.setPrestamo((Prestamo) seleccionado);
                } else if (controller instanceof ModifyHistoricoController historicoController) {
                    historicoController.setHistorico((HistoricoPrestamo) seleccionado);
                }

                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina el elemento seleccionado de la tabla actual después de confirmación.
     */
    @FXML
    private void deleteItem() {
        Object seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(LanguageManager.getProperty("confirmacion"));
            alert.setHeaderText(LanguageManager.getProperty("eliminar.elemento"));
            alert.setContentText(LanguageManager.getProperty("seguro.eliminar")); // ¿Estás seguro de que quieres eliminar este registro?

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean eliminado = false;

                try {
                    switch (vistaActual) {
                        case "alumnos" -> {
                            String dni = ((Alumno) seleccionado).getDni();

                            // PASO 1: Actualizar registros en Historico_prestamo
                            historicoPrestamoDAO.anularDniAlumno(dni);

                            // PASO 2: Intentar eliminar el alumno
                            eliminado = alumnoDAO.eliminarAlumno(dni);
                        }
                        case "libros" -> {
                            int codigo = ((Libro) seleccionado).getCodigo();
                            eliminado = libroDAO.eliminarLibro(codigo);
                        }
                        case "prestamos" -> {
                            int idPrestamo = ((Prestamo) seleccionado).getIdPrestamo();
                            eliminado = prestamoDAO.eliminarPrestamo(idPrestamo);
                        }
                    }

                    if (eliminado) {
                        tableView.getItems().remove(seleccionado);
                    } else {
                        mostrarAlertaError(LanguageManager.getProperty("error.eliminar.detalle"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlertaError(LanguageManager.getProperty("error.eliminar.detalle"));
                }
            }
        }
    }

    /**
     * Muestra una alerta de error con el mensaje proporcionado.
     * @param mensaje El mensaje a mostrar en la alerta.
     */
    private void mostrarAlertaError(String mensaje) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(LanguageManager.getProperty("error"));
        errorAlert.setHeaderText(LanguageManager.getProperty("error.eliminar"));
        errorAlert.setContentText(mensaje);
        errorAlert.showAndWait();
    }

    /**
     * Abre una nueva ventana con el FXML y título especificados.
     * @param fxmlPath La ruta del archivo FXML a cargar.
     * @param titulo El título que tendrá la ventana.
     */
    private void abrirVentana(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxmlPath),
                    ResourceBundle.getBundle("i18n/messages", LanguageManager.getLocale()));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(titulo);

            Object controller = loader.getController();
            if (controller instanceof AlumnoController alumnoController) {
                alumnoController.setBibliotecaController(this);
            } else if (controller instanceof LibroController libroController) {
                libroController.setBibliotecaController(this);
            } else if (controller instanceof PrestamoController prestamoController) {
                prestamoController.setBibliotecaController(this);
            } else if (controller instanceof  HistoricoController historicoController) {
                historicoController.setBibliotecaController(this);
            }

            stage.setOnHidden(event -> actualizarTablaActual());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de ayuda con la guía rápida de la aplicación.
     */
    @FXML
    private void openHelp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/help.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Guía Rápida");
            stage.setScene(new Scene(loader.load(), 1000, 800));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cambia el idioma de la aplicación a español.
     */
    @FXML
    private void setIdiomaEs() {
        cambiarIdioma("es");
    }

    /**
     * Cambia el idioma de la aplicación a inglés.
     */
    @FXML
    private void setIdiomaEn() {
        cambiarIdioma("en");
    }

    /**
     * Cambia el idioma de la aplicación.
     * @param lang El código del idioma (es/en).
     */
    private void cambiarIdioma(String lang) {
        LanguageManager.setLanguage(lang);
        actualizarInterfaz();
    }

    /**
     * Actualiza la interfaz para reflejar el cambio de idioma.
     */
    private void actualizarInterfaz() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biblioteca.fxml"),
                    ResourceBundle.getBundle("i18n/messages", LanguageManager.getLocale()));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(loader.getResources().getString("title.biblioteca"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
