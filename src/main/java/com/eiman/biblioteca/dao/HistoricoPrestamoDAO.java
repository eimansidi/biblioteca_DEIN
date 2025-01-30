package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.HistoricoPrestamo;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO que gestiona la conexion y operaciones CRUD para la tabla Historico_prestamo.
 * Permite insertar, obtener, actualizar y modificar registros del historial de prestamos.
 */
public class HistoricoPrestamoDAO {

    private static final String TABLE_NAME = "Historico_prestamo";

    /**
     * Inserta un nuevo registro de historico de prestamo en la base de datos.
     *
     * @param historicoPrestamo El registro de historico de prestamo a insertar.
     */
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todos los registros de historicos de prestamos de la base de datos.
     *
     * @return Una lista con todos los registros de historicos de prestamos.
     */
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
        }
        return historicos;
    }

    /**
     * Actualiza la fecha de devolucion de un registro de historico de prestamo.
     *
     * @param historico El registro de historico de prestamo con la fecha de devolucion actualizada.
     */
    public void actualizarHistoricoPrestamo(HistoricoPrestamo historico) {
        String sql = "UPDATE Historico_prestamo SET fecha_devolucion = ? WHERE id_prestamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(historico.getFechaDevolucion()));
            stmt.setInt(2, historico.getIdPrestamo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Anula el dni de un alumno en el historial de prestamos, reemplazandolo por "ANONIMO".
     *
     * @param dni El dni del alumno a anular en los registros de historico de prestamo.
     */
    public void anularDniAlumno(String dni) {
        String sql = "UPDATE Historico_prestamo SET dni_alumno = 'ANONIMO' WHERE dni_alumno = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("No se encontraron registros en Historico_prestamo con dni_alumno: " + dni);
            } else {
                System.out.println("Se actualizaron " + filasAfectadas + " registros en Historico_prestamo.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
