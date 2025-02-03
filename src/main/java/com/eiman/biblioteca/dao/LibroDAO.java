package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase DAO que gestiona la conexión y operaciones CRUD para la tabla Libro.
 * Permite insertar, obtener, actualizar y eliminar libros en la base de datos.
 */
public class LibroDAO {
    private static final Logger logger = Logger.getLogger(LibroDAO.class.getName());
    private static final String TABLE_NAME = "Libro";

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param libro El libro a insertar.
     */
    public void insertarLibro(Libro libro) {
        logger.info("Intentando insertar un nuevo libro: " + libro.getTitulo());
        String sql = "INSERT INTO " + TABLE_NAME + " (titulo, autor, editorial, estado, baja, portada) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getEditorial());
            stmt.setString(4, libro.getEstado());
            stmt.setInt(5, libro.getBaja());
            stmt.setBytes(6, libro.getPortada());
            stmt.executeUpdate();
            logger.info("Libro insertado exitosamente: " + libro.getTitulo());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar el libro: " + libro.getTitulo(), e);
        }
    }

    /**
     * Obtiene un libro de la base de datos utilizando su código.
     *
     * @param codigo El código del libro.
     * @return El libro con el código especificado, o null si no se encuentra.
     */
    public Libro obtenerLibroPorCodigo(int codigo) {
        logger.info("Buscando libro con código: " + codigo);
        Libro libro = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE codigo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                libro = new Libro(
                        rs.getInt("codigo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getString("estado"),
                        rs.getInt("baja"),
                        rs.getBytes("portada")
                );
                logger.info("Libro encontrado: " + libro.getTitulo());
            } else {
                logger.warning("No se encontró ningún libro con código: " + codigo);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el libro con código: " + codigo, e);
        }
        return libro;
    }

    /**
     * Obtiene todos los libros disponibles (que no están dados de baja) de la base de datos.
     *
     * @return Una lista con todos los libros disponibles.
     */
    public List<Libro> obtenerTodosLosLibros() {
        logger.info("Obteniendo lista de todos los libros disponibles.");
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE baja=0";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("codigo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getString("estado"),
                        rs.getInt("baja"),
                        rs.getBytes("portada")
                );
                libros.add(libro);
            }
            logger.info("Total de libros obtenidos: " + libros.size());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la lista de libros.", e);
        }
        return libros;
    }

    /**
     * Actualiza los datos de un libro en la base de datos.
     *
     * @param libro El libro con los datos actualizados.
     */
    public void actualizarLibro(Libro libro) {
        logger.info("Actualizando información del libro con código: " + libro.getCodigo());
        String sql = "UPDATE " + TABLE_NAME + " SET titulo = ?, autor = ?, editorial = ?, estado = ?, baja = ?, portada = ? WHERE codigo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getEditorial());
            stmt.setString(4, libro.getEstado());
            stmt.setInt(5, libro.getBaja());
            stmt.setBytes(6, libro.getPortada());
            stmt.setInt(7, libro.getCodigo());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Libro actualizado correctamente con código: " + libro.getCodigo());
            } else {
                logger.warning("No se encontró el libro con código: " + libro.getCodigo() + " para actualizar.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar el libro con código: " + libro.getCodigo(), e);
        }
    }

    /**
     * Elimina un libro de la base de datos utilizando su código.
     *
     * @param codigo El código del libro a eliminar.
     * @return true si el libro fue eliminado exitosamente, false si no se encontró o ocurrió un error.
     */
    public boolean eliminarLibro(int codigo) {
        logger.info("Intentando eliminar el libro con código: " + codigo);
        String sql = "DELETE FROM Libro WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Libro eliminado exitosamente con código: " + codigo);
                return true;
            } else {
                logger.warning("No se encontró el libro con código: " + codigo + " para eliminar.");
                return false;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el libro con código: " + codigo, e);
            return false;
        }
    }
}
