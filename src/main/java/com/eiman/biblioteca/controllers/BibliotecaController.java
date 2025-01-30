package com.eiman.biblioteca.controllers;

import com.eiman.biblioteca.dao.AlumnoDAO;
import com.eiman.biblioteca.dao.LibroDAO;
import com.eiman.biblioteca.dao.PrestamoDAO;
import com.eiman.biblioteca.dao.HistoricoPrestamoDAO;
import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.models.Prestamo;
import com.eiman.biblioteca.models.HistoricoPrestamo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.Optional;

public class BibliotecaController {
    @FXML private Button btnAlumnos, btnLibros, btnPrestamos, btnHistoricoPrestamos, btnInformes;
    @FXML private TableView<Object> tableView;
    @FXML private Button btnAñadir, btnModificar, btnEliminar;

    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private final LibroDAO libroDAO = new LibroDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final HistoricoPrestamoDAO historicoPrestamoDAO = new HistoricoPrestamoDAO();

    private String vistaActual = "alumnos";

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
    }

    @FXML
    private void openAlumnosTable() {
        vistaActual = "alumnos";
        tableView.getColumns().clear();
        tableView.setItems(FXCollections.observableArrayList());

        TableColumn<Object, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        TableColumn<Object, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Object, String> colApellido1 = new TableColumn<>("Primer Apellido");
        colApellido1.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        TableColumn<Object, String> colApellido2 = new TableColumn<>("Segundo Apellido");
        colApellido2.setCellValueFactory(new PropertyValueFactory<>("apellido2"));

        tableView.getColumns().addAll(colDni, colNombre, colApellido1, colApellido2);
        tableView.setItems(FXCollections.observableArrayList(alumnoDAO.obtenerTodosLosAlumnos()));

        btnAñadir.setDisable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void openLibrosTable() {
        vistaActual = "libros";
        tableView.getColumns().clear();
        tableView.setItems(FXCollections.observableArrayList());

        TableColumn<Object, Integer> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<Object, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        TableColumn<Object, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        TableColumn<Object, String> colEditorial = new TableColumn<>("Editorial");
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        TableColumn<Object, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        TableColumn<Object, String> colBaja = new TableColumn<>("Baja");
        colBaja.setCellValueFactory(new PropertyValueFactory<>("baja"));

        tableView.getColumns().addAll(colCodigo, colTitulo, colAutor, colEditorial, colEstado, colBaja);
        tableView.setItems(FXCollections.observableArrayList(libroDAO.obtenerTodosLosLibros()));

        btnAñadir.setDisable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void openPrestamosTable() {
        vistaActual = "prestamos";
        tableView.getColumns().clear();
        tableView.setItems(FXCollections.observableArrayList());

        TableColumn<Object, Integer> colId = new TableColumn<>("ID Préstamo");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
        TableColumn<Object, String> colDniAlumno = new TableColumn<>("DNI Alumno");
        colDniAlumno.setCellValueFactory(new PropertyValueFactory<>("dniAlumno"));
        TableColumn<Object, Integer> colCodigoLibro = new TableColumn<>("Código Libro");
        colCodigoLibro.setCellValueFactory(new PropertyValueFactory<>("codigoLibro"));
        TableColumn<Object, String> colFechaPrestamo = new TableColumn<>("Fecha Préstamo");
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));

        tableView.getColumns().setAll(colId, colDniAlumno, colCodigoLibro, colFechaPrestamo);
        tableView.setItems(FXCollections.observableArrayList(prestamoDAO.obtenerTodosLosPrestamos()));

        btnAñadir.setDisable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void openHistoricoPrestamosTable() {
        vistaActual = "historico_prestamos";
        tableView.getColumns().clear();
        tableView.setItems(FXCollections.observableArrayList());

        TableColumn<Object, Integer> colId = new TableColumn<>("ID Préstamo");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
        TableColumn<Object, String> colDniAlumno = new TableColumn<>("DNI Alumno");
        colDniAlumno.setCellValueFactory(new PropertyValueFactory<>("dniAlumno"));
        TableColumn<Object, Integer> colCodigoLibro = new TableColumn<>("Código Libro");
        colCodigoLibro.setCellValueFactory(new PropertyValueFactory<>("codigoLibro"));
        TableColumn<Object, String> colFechaPrestamo = new TableColumn<>("Fecha Préstamo");
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        TableColumn<Object, String> colFechaDevolucion = new TableColumn<>("Fecha Devolución");
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

        tableView.getColumns().setAll(colId, colDniAlumno, colCodigoLibro, colFechaPrestamo, colFechaDevolucion);
        tableView.setItems(FXCollections.observableArrayList(historicoPrestamoDAO.obtenerTodosLosHistoricos()));

        btnAñadir.setDisable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void openInformesWindow() {
        abrirVentana("/fxml/informes.fxml", "Informes");
    }

    public void actualizarTablaActual() {
        switch (vistaActual) {
            case "alumnos" -> openAlumnosTable();
            case "libros" -> openLibrosTable();
            case "prestamos" -> openPrestamosTable();
            case "historico_prestamos" -> openHistoricoPrestamosTable();
        }
    }

    @FXML
    private void openAddWindow() {
        switch (vistaActual) {
            case "alumnos" -> abrirVentana("/fxml/alumnos.fxml", "Añadir Alumno");
            case "libros" -> abrirVentana("/fxml/libros.fxml", "Añadir Libro");
            case "prestamos" -> abrirVentana("/fxml/prestamos.fxml", "Añadir Préstamo");
            case "historico_prestamos" -> abrirVentana("/fxml/historicos.fxml", "Devolver Libro");
        }
    }

    @FXML
    private void openModifyWindow() {
        Object seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        try {
            FXMLLoader loader = null;
            Stage stage = new Stage();

            if (seleccionado instanceof Alumno alumno) {
                loader = new FXMLLoader(getClass().getResource("/fxml/alumnos.fxml"));
                stage.setTitle("Modificar Alumno");
                stage.setScene(new Scene(loader.load()));
                AlumnoController controller = loader.getController();
                controller.setAlumno(alumno);
                controller.setBibliotecaController(this);
            } else if (seleccionado instanceof Libro libro) {
                loader = new FXMLLoader(getClass().getResource("/fxml/libros.fxml"));
                stage.setTitle("Modificar Libro");
                stage.setScene(new Scene(loader.load()));
                LibroController controller = loader.getController();
                controller.setLibro(libro);
            } else if (seleccionado instanceof Prestamo prestamo) {
                loader = new FXMLLoader(getClass().getResource("/fxml/prestamos.fxml"));
                stage.setTitle("Modificar Préstamo");
                stage.setScene(new Scene(loader.load()));
                PrestamoController controller = loader.getController();
                controller.setPrestamo(prestamo);
            } else if (seleccionado instanceof HistoricoPrestamo historico) {
                loader = new FXMLLoader(getClass().getResource("/fxml/modify_historicos.fxml"));
                stage.setTitle("Modificar Devolución");
                stage.setScene(new Scene(loader.load()));
                ModifyHistoricoController controller = loader.getController();
                controller.setHistorico(historico);
            }

            if (loader != null) {
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteItem() {
        Object seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Eliminar elemento");
            alert.setContentText("¿Estás seguro de que quieres eliminar este registro?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                switch (vistaActual) {
                    case "alumnos" -> alumnoDAO.eliminarAlumno(((Alumno) seleccionado).getDni());
                    case "libros" -> libroDAO.eliminarLibro(((Libro) seleccionado).getCodigo());
                    case "prestamos" -> prestamoDAO.eliminarPrestamo(((Prestamo) seleccionado).getIdPrestamo());
                }
                tableView.getItems().remove(seleccionado);
            }
        }
    }

    private void abrirVentana(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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

}