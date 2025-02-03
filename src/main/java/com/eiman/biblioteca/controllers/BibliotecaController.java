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
import javafx.scene.Parent;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador principal de la ventana de la biblioteca.
 * Este controlador gestiona la navegacion entre las diferentes vistas (Alumnos, Libros, Prestamos, Historicos e Informes).
 * Tambien maneja la carga y actualizacion de las tablas correspondientes, asi como las operaciones de adicion, modificacion y eliminacion.
 */
public class BibliotecaController {
    private static final Logger logger = Logger.getLogger(BibliotecaController.class.getName());

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
     * Inicializa la interfaz de la biblioteca, configurando las tooltips para cada boton y configurando la tabla de alumnos.
     * Tambien deshabilita los botones de modificacion y eliminacion por defecto.
     */
    @FXML
    private void initialize() {
        logger.info("Inicializando BibliotecaController.");
        try {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error durante la inicialización de BibliotecaController.", e);
        }
    }

    /**
     * Carga la tabla de alumnos en la vista principal.
     * Asigna las columnas de la tabla con la informacion de los alumnos y actualiza la vista.
     */
    @FXML
    private void openAlumnosTable() {
        logger.info("Cargando tabla de alumnos.");
        try {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar la tabla de alumnos.", e);
        }
    }

    /**
     * Carga la tabla de libros en la vista principal.
     * Asigna las columnas de la tabla con la informacion de los libros y actualiza la vista.
     * Se cargan solo los libros con baja=0, para que los dados de baja no aparezcan.
     */
    @FXML
    private void openLibrosTable() {
        logger.info("Cargando tabla de libros.");
        try {
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

            tableView.getColumns().addAll(colCodigo, colTitulo, colAutor, colEditorial, colEstado);
            tableView.setItems(FXCollections.observableArrayList(libroDAO.obtenerTodosLosLibros()));

            btnAñadir.setDisable(false);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar la tabla de libros.", e);
        }
    }

    /**
     * Carga la tabla de prestamos en la vista principal.
     * Asigna las columnas de la tabla con la informacion de los prestamos y actualiza la vista.
     */
    @FXML
    private void openPrestamosTable() {
        logger.info("Cargando tabla de préstamos.");
        try {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar la tabla de préstamos.", e);
        }
    }

    /**
     * Carga la tabla del historial de prestamos en la vista principal.
     * Asigna las columnas de la tabla con la informacion de los prestamos historicos y actualiza la vista.
     */
    @FXML
    private void openHistoricoPrestamosTable() {
        logger.info("Cargando tabla de histórico de préstamos.");
        try {
            vistaActual = "historico_prestamos";
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
            TableColumn<Object, String> colFechaDevolucion = new TableColumn<>(LanguageManager.getProperty("fecha.devolucion"));
            colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

            tableView.getColumns().setAll(colId, colDniAlumno, colCodigoLibro, colFechaPrestamo, colFechaDevolucion);
            tableView.setItems(FXCollections.observableArrayList(historicoPrestamoDAO.obtenerTodosLosHistoricos()));

            btnAñadir.setDisable(false);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar la tabla de histórico de préstamos.", e);
        }
    }

    /**
     * Abre la ventana de informes para generar y ver los informes disponibles.
     */
    @FXML
    private void openInformesWindow() {
        logger.info("Abriendo ventana de informes.");
        abrirVentana("/fxml/informes.fxml", (LanguageManager.getProperty("informes")));
    }

    /**
     * Actualiza la tabla actual basada en la vista seleccionada.
     * Si estamos en "libros", se recarga la lista de libros con baja=0.
     */
    public void actualizarTablaActual() {
        logger.info("Actualizando la tabla actual: " + vistaActual);
        switch (vistaActual) {
            case "alumnos" -> openAlumnosTable();
            case "libros" -> openLibrosTable();
            case "prestamos" -> openPrestamosTable();
            case "historico_prestamos" -> openHistoricoPrestamosTable();
            default -> logger.warning("Vista desconocida: " + vistaActual);
        }
    }

    /**
     * Abre la ventana para añadir un nuevo elemento dependiendo de la vista actual (Alumno, Libro, Prestamo o Historico).
     */
    @FXML
    private void openAddWindow() {
        logger.info("Abriendo ventana para añadir nuevo elemento. Vista actual: " + vistaActual);
        switch (vistaActual) {
            case "alumnos" -> abrirVentana("/fxml/alumnos.fxml", (LanguageManager.getProperty("añadir.alumno")));
            case "libros" -> abrirVentana("/fxml/libros.fxml", (LanguageManager.getProperty("añadir.libro")));
            case "prestamos" -> abrirVentana("/fxml/prestamos.fxml", (LanguageManager.getProperty("añadir.prestamo")));
            case "historico_prestamos" -> abrirVentana("/fxml/historicos.fxml", (LanguageManager.getProperty("devolver.libro")));
            default -> logger.warning("Vista desconocida para añadir: " + vistaActual);
        }
    }

    /**
     * Abre la ventana de modificacion del elemento seleccionado en la tabla actual.
     * Permite modificar alumnos, libros, prestamos o historicos.
     */
    @FXML
    private void openModifyWindow() {
        logger.info("Abriendo ventana de modificación de elemento.");
        Object seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            logger.info("No hay elemento seleccionado para modificar.");
            return;
        }

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
            } else {
                logger.warning("No se pudo cargar la ventana de modificación (loader es null).");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al abrir la ventana de modificación.", e);
        }
    }

    /**
     * Elimina el elemento seleccionado de la tabla actual despues de confirmacion.
     */
    @FXML
    private void deleteItem() {
        logger.info("Intentando eliminar elemento de la tabla actual: " + vistaActual);
        Object seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(LanguageManager.getProperty("confirmacion"));
            alert.setHeaderText(LanguageManager.getProperty("eliminar.elemento"));
            alert.setContentText(LanguageManager.getProperty("seguro.eliminar"));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean eliminado = false;

                try {
                    switch (vistaActual) {
                        case "alumnos" -> {
                            String dni = ((Alumno) seleccionado).getDni();

                            // PASO 1: Eliminar registros en Historico_prestamo
                            historicoPrestamoDAO.eliminarPorDni(dni);

                            // PASO 2: Eliminar registros en Prestamo
                            prestamoDAO.eliminarPorDni(dni);

                            // PASO 3: Intentar eliminar el alumno
                            eliminado = alumnoDAO.eliminarAlumno(dni);
                        }
                        case "libros" -> {
                            int codigo = ((Libro) seleccionado).getCodigo();
                            // PASO 1: Eliminar registros en Historico_prestamo relacionados con este libro
                            historicoPrestamoDAO.eliminarPorCodigoLibro(codigo);

                            // PASO 2: Eliminar registros en Prestamo relacionados con este libro
                            prestamoDAO.eliminarPorCodigoLibro(codigo);

                            // PASO 3: Eliminar el libro
                            eliminado = libroDAO.eliminarLibro(codigo);
                        }
                        case "prestamos" -> {
                            int idPrestamo = ((Prestamo) seleccionado).getIdPrestamo();
                            eliminado = prestamoDAO.eliminarPrestamo(idPrestamo);
                        }
                        case "historico_prestamos" -> {
                            int idPrestamo = ((HistoricoPrestamo) seleccionado).getIdPrestamo();
                            eliminado = historicoPrestamoDAO.eliminarHistoricoPrestamo(idPrestamo);
                        }
                        default -> logger.warning("Vista desconocida al eliminar: " + vistaActual);
                    }

                    if (eliminado) {
                        tableView.getItems().remove(seleccionado);
                    } else {
                        mostrarAlertaError(LanguageManager.getProperty("error.eliminar.detalle"));
                    }

                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error al eliminar el elemento de la base de datos.", e);
                    mostrarAlertaError(LanguageManager.getProperty("error.eliminar.detalle"));
                }
            }
        } else {
            logger.info("No hay elemento seleccionado para eliminar.");
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
     * Abre una nueva ventana con el FXML y titulo especificados.
     * @param fxmlPath La ruta del archivo FXML a cargar.
     * @param titulo El titulo que tendra la ventana.
     */
    private void abrirVentana(String fxmlPath, String titulo) {
        logger.info("Abriendo ventana: " + fxmlPath + " con título: " + titulo);
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
            } else if (controller instanceof HistoricoController historicoController) {
                historicoController.setBibliotecaController(this);
            }

            // Al cerrar la ventana, se actualiza la tabla actual
            stage.setOnHidden(event -> actualizarTablaActual());
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al abrir la ventana: " + fxmlPath, e);
        }
    }

    /**
     * Abre la ventana de ayuda con la guia rapida de la aplicacion.
     */
    @FXML
    private void openHelp() {
        logger.info("Abriendo ventana de ayuda. Vista actual: " + vistaActual);
        String archivoHtml = switch (vistaActual) {
            case "alumnos" -> "/help/guia_alumnos.html";
            case "libros" -> "/help/guia_libros.html";
            case "prestamos" -> "/help/guia_prestamos.html";
            case "historico_prestamos" -> "/help/guia_historico.html";
            default -> "/help/guia.html";
        };

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/help.fxml"));
            Parent root = loader.load();
            HelpController helpController = loader.getController();

            helpController.cargarHTML(archivoHtml);

            Stage stage = new Stage();
            stage.setTitle("GUIA");
            stage.setScene(new Scene(root, 850, 750));
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al abrir la ventana de ayuda.", e);
        }
    }

    /**
     * Cambia el idioma de la aplicacion a espanol.
     */
    @FXML
    private void setIdiomaEs() {
        logger.info("Cambiando idioma de la aplicación a Español (es).");
        cambiarIdioma("es");
    }

    /**
     * Cambia el idioma de la aplicacion a ingles.
     */
    @FXML
    private void setIdiomaEn() {
        logger.info("Cambiando idioma de la aplicación a Inglés (en).");
        cambiarIdioma("en");
    }

    /**
     * Cambia el idioma de la aplicacion.
     * @param lang El codigo del idioma (es/en).
     */
    private void cambiarIdioma(String lang) {
        logger.info("Cambiando idioma a: " + lang);
        LanguageManager.setLanguage(lang);
        actualizarInterfaz();
    }

    /**
     * Actualiza la interfaz para reflejar el cambio de idioma.
     */
    private void actualizarInterfaz() {
        logger.info("Actualizando interfaz para reflejar el cambio de idioma.");
        Stage stage = (Stage) menuBar.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/biblioteca.fxml"),
                    ResourceBundle.getBundle("i18n/messages", LanguageManager.getLocale()));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(loader.getResources().getString("title.biblioteca"));
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al actualizar la interfaz para reflejar el cambio de idioma.", e);
        }
    }
}
