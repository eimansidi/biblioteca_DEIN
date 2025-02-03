package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.HistoricoPrestamo;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase DAO que gestiona la conexión y operaciones CRUD para la tabla Historico_prestamo.
 * Permite insertar, obtener, actualizar y modificar registros del historial de préstamos.
 */
public class HistoricoPrestamoDAO {
    private static final Logger logger = Logger.getLogger(HistoricoPrestamoDAO.class.getName());
    private static final String TABLE_NAME = "Historico_prestamo";

    /**
     * Inserta un nuevo registro de histórico de préstamo en la base de datos.
     *
     * @param historicoPrestamo El registro de histórico de préstamo a insertar.
     */
    public void insertarHistoricoPrestamo(HistoricoPrestamo historicoPrestamo) {
        logger.info("Insertando un nuevo registro en el historial de préstamos.");
        String sql = "INSERT INTO Historico_prestamo (id_prestamo, dni_alumno, codigo_libro, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, historicoPrestamo.getIdPrestamo());
            stmt.setString(2, historicoPrestamo.getDniAlumno());
            stmt.setInt(3, historicoPrestamo.getCodigoLibro());
            stmt.setTimestamp(4, Timestamp.valueOf(historicoPrestamo.getFechaPrestamo()));
            stmt.setTimestamp(5, Timestamp.valueOf(historicoPrestamo.getFechaDevolucion()));

            stmt.executeUpdate();
            logger.info("Registro de histórico de préstamo insertado exitosamente.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar el registro de histórico de préstamo.", e);
        }
    }

    /**
     * Obtiene todos los registros del historial de préstamos de la base de datos.
     *
     * @return Una lista con todos los registros del historial de préstamos.
     */
    public List<HistoricoPrestamo> obtenerTodosLosHistoricos() {
        logger.info("Obteniendo todos los registros del historial de préstamos.");
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
            logger.info("Total de registros obtenidos: " + historicos.size());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la lista de históricos de préstamos.", e);
        }
        return historicos;
    }

    /**
     * Actualiza la fecha de devolución de un registro del historial de préstamos.
     *
     * @param historico El registro del historial de préstamo con la fecha de devolución actualizada.
     */
    public void actualizarHistoricoPrestamo(HistoricoPrestamo historico) {
        logger.info("Actualizando la fecha de devolución en el historial de préstamos para el ID: " + historico.getIdPrestamo());
        String sql = "UPDATE Historico_prestamo SET fecha_devolucion = ? WHERE id_prestamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(historico.getFechaDevolucion()));
            stmt.setInt(2, historico.getIdPrestamo());
            stmt.executeUpdate();
            logger.info("Registro actualizado correctamente.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar la fecha de devolución en el historial de préstamos.", e);
        }
    }

    /**
     * Anula el DNI de un alumno en el historial de préstamos, reemplazándolo por "ANONIMO".
     *
     * @param dni El DNI del alumno a anular en los registros del historial de préstamos.
     */
    public void anularDniAlumno(String dni) {
        logger.info("Anulando DNI de alumno en el historial de préstamos para el DNI: " + dni);
        String sql = "UPDATE Historico_prestamo SET dni_alumno = 'ANONIMO' WHERE dni_alumno = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas == 0) {
                logger.warning("No se encontraron registros en Historico_prestamo con dni_alumno: " + dni);
            } else {
                logger.info("Se actualizaron " + filasAfectadas + " registros en Historico_prestamo.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al anular el DNI del alumno en el historial de préstamos.", e);
        }
    }

    /**
     * Elimina un registro del historial de préstamos por su ID de préstamo.
     *
     * @param idPrestamo El ID del préstamo a eliminar.
     * @return true si el registro fue eliminado, false en caso contrario.
     */
    public boolean eliminarHistoricoPrestamo(int idPrestamo) {
        logger.info("Intentando eliminar un registro del historial de préstamos con ID: " + idPrestamo);
        String sql = "DELETE FROM Historico_prestamo WHERE id_prestamo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPrestamo);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Registro eliminado exitosamente con ID: " + idPrestamo);
                return true;
            } else {
                logger.warning("No se encontró el registro con ID: " + idPrestamo + " para eliminar.");
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el registro del historial de préstamos.", e);
            return false;
        }
    }

    /**
     * Elimina registros del historial de préstamos por DNI de alumno.
     *
     * @param dni El DNI del alumno cuyos registros deben ser eliminados.
     */
    public void eliminarPorDni(String dni) {
        logger.info("Eliminando registros del historial de préstamos para el DNI: " + dni);
        String sql = "DELETE FROM Historico_prestamo WHERE dni_alumno = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            int filasAfectadas = pstmt.executeUpdate();
            logger.info("Registros eliminados: " + filasAfectadas);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar registros del historial de préstamos por DNI.", e);
        }
    }

    /**
     * Elimina registros del historial de préstamos por código de libro.
     *
     * @param codigoLibro El código del libro cuyos registros deben ser eliminados.
     */
    public void eliminarPorCodigoLibro(int codigoLibro) {
        logger.info("Eliminando registros del historial de préstamos para el código de libro: " + codigoLibro);
        String sql = "DELETE FROM Historico_prestamo WHERE codigo_libro = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigoLibro);
            int filasAfectadas = pstmt.executeUpdate();
            logger.info("Registros eliminados: " + filasAfectadas);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar registros del historial de préstamos por código de libro.", e);
        }
    }
}