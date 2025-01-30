package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Prestamo;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    private static final String TABLE_NAME = "Prestamo";

    // Método para insertar un nuevo préstamo
    public void insertarPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO " + TABLE_NAME + " (dni_alumno, codigo_libro, fecha_prestamo) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, prestamo.getDniAlumno());
            stmt.setInt(2, prestamo.getCodigoLibro());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.executeUpdate();
            System.out.println("Préstamo insertado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al insertar el préstamo.");
        }
    }

    // Método para obtener un préstamo por su ID
    public Prestamo obtenerPrestamoPorId(int idPrestamo) {
        Prestamo prestamo = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_prestamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idPrestamo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                prestamo = new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getString("dni_alumno"),
                        rs.getInt("codigo_libro"),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener el préstamo.");
        }
        return prestamo;
    }

    // Método para obtener todos los préstamos
    public List<Prestamo> obtenerTodosLosPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getString("dni_alumno"),
                        rs.getInt("codigo_libro"),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime()
                );
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener los préstamos.");
        }
        return prestamos;
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamo WHERE id_prestamo NOT IN (SELECT id_prestamo FROM Historico_prestamo)";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getString("dni_alumno"),
                        rs.getInt("codigo_libro"),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime()
                );
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener los préstamos activos.");
        }
        return prestamos;
    }

    // Método para actualizar un préstamo
    public void actualizarPrestamo(Prestamo prestamo) {
        String sql = "UPDATE " + TABLE_NAME + " SET dni_alumno = ?, codigo_libro = ?, fecha_prestamo = ? WHERE id_prestamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, prestamo.getDniAlumno());
            stmt.setInt(2, prestamo.getCodigoLibro());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.setInt(4, prestamo.getIdPrestamo());
            stmt.executeUpdate();
            System.out.println("Préstamo actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el préstamo.");
        }
    }

    // Método para eliminar un préstamo por ID
    public void eliminarPrestamo(int idPrestamo) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id_prestamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idPrestamo);
            stmt.executeUpdate();
            System.out.println("Préstamo eliminado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al eliminar el préstamo.");
        }
    }
}