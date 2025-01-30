package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO que gestiona la conexion y operaciones CRUD para la tabla Libro.
 * Permite insertar, obtener, actualizar y eliminar libros en la base de datos.
 */
public class LibroDAO {

    private static final String TABLE_NAME = "Libro";

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param libro El libro a insertar.
     */
    public void insertarLibro(Libro libro) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un libro de la base de datos utilizando su código.
     *
     * @param codigo El código del libro.
     * @return El libro con el código especificado, o null si no se encuentra.
     */
    public Libro obtenerLibroPorCodigo(int codigo) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libro;
    }

    /**
     * Obtiene todos los libros de la base de datos.
     *
     * @return Una lista con todos los libros.
     */
    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    /**
     * Actualiza los datos de un libro en la base de datos.
     *
     * @param libro El libro con los datos actualizados.
     */
    public void actualizarLibro(Libro libro) {
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
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un libro de la base de datos utilizando su código.
     *
     * @param codigo El código del libro a eliminar.
     * @return true si el libro fue eliminado exitosamente, false si no se encontro o ocurrio un error.
     */
    public boolean eliminarLibro(int codigo) {
        String sql = "DELETE FROM Libro WHERE codigo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
