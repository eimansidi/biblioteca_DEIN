package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Libro;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    private static final String TABLE_NAME = "Libro";

    // Método para insertar un nuevo libro
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
            System.out.println("Libro insertado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al insertar el libro.");
        }
    }

    // Método para obtener un libro por su código
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
            System.err.println("Error al obtener el libro.");
        }
        return libro;
    }

    // Método para obtener todos los libros
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
            System.err.println("Error al obtener los libros.");
        }
        return libros;
    }

    // Método para actualizar los datos de un libro
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
            System.out.println("Libro actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el libro.");
        }
    }

    // Método para eliminar un libro
    public void eliminarLibro(int codigo) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE codigo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            stmt.executeUpdate();
            System.out.println("Libro eliminado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al eliminar el libro.");
        }
    }
}
