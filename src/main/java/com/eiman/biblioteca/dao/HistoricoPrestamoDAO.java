package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.HistoricoPrestamo;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoPrestamoDAO {

    private static final String TABLE_NAME = "Historico_prestamo";

    // Método para insertar un nuevo registro de histórico de préstamo
    public void insertarHistoricoPrestamo(HistoricoPrestamo historicoPrestamo) {
        String sql = "INSERT INTO Historico_prestamo (id_prestamo, dni_alumno, codigo_libro, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, historicoPrestamo.getIdPrestamo());
            stmt.setString(2, historicoPrestamo.getDniAlumno());
            stmt.setInt(3, historicoPrestamo.getCodigoLibro());
            stmt.setTimestamp(4, Timestamp.valueOf(historicoPrestamo.getFechaPrestamo()));
            stmt.setTimestamp(5, Timestamp.valueOf(historicoPrestamo.getFechaDevolucion()));

            stmt.executeUpdate();
            System.out.println("Histórico de préstamo insertado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al insertar el histórico de préstamo.");
        }
    }

    // Método para obtener todos los históricos de préstamos
    public List<HistoricoPrestamo> obtenerTodosLosHistoricos() {
        List<HistoricoPrestamo> historicos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HistoricoPrestamo historicoPrestamo = new HistoricoPrestamo(
                        rs.getInt("id_prestamo"),
                        rs.getString("dni_alumno"),
                        rs.getInt("codigo_libro"),
                        rs.getTimestamp("fecha_prestamo").toLocalDateTime(),
                        rs.getTimestamp("fecha_devolucion") != null ? rs.getTimestamp("fecha_devolucion").toLocalDateTime() : null
                );
                historicos.add(historicoPrestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener los históricos de préstamos.");
        }
        return historicos;
    }

    public void actualizarHistoricoPrestamo(HistoricoPrestamo historico) {
        String sql = "UPDATE Historico_prestamo SET fecha_devolucion = ? WHERE id_prestamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(historico.getFechaDevolucion()));
            stmt.setInt(2, historico.getIdPrestamo());
            stmt.executeUpdate();
            System.out.println("Histórico de préstamo actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el histórico de préstamo.");
        }
    }

}
