package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Prestamo;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase DAO que gestiona la conexión y operaciones CRUD para la tabla Prestamo.
 * Permite insertar, obtener, actualizar y eliminar préstamos en la base de datos.
 */
public class PrestamoDAO {

    private static final Logger logger = Logger.getLogger(PrestamoDAO.class.getName());
    private static final String TABLE_NAME = "Prestamo";

    /**
     * Inserta un nuevo préstamo en la base de datos.
     *
     * @param prestamo El préstamo a insertar.
     */
    public void insertarPrestamo(Prestamo prestamo) {
        logger.info("Intentando insertar un nuevo préstamo para el alumno: " + prestamo.getDniAlumno());
        String sql = "INSERT INTO " + TABLE_NAME + " (dni_alumno, codigo_libro, fecha_prestamo) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, prestamo.getDniAlumno());
            stmt.setInt(2, prestamo.getCodigoLibro());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.executeUpdate();
            logger.info("Préstamo insertado con éxito para el alumno: " + prestamo.getDniAlumno());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar el préstamo.", e);
        }
    }

    /**
     * Obtiene un préstamo de la base de datos utilizando su ID.
     *
     * @param idPrestamo El ID del préstamo.
     * @return El préstamo con el ID especificado, o null si no se encuentra.
     */
    public Prestamo obtenerPrestamoPorId(int idPrestamo) {
        logger.info("Buscando préstamo con ID: " + idPrestamo);
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
                logger.info("Préstamo encontrado: " + idPrestamo);
            } else {
                logger.warning("No se encontró el préstamo con ID: " + idPrestamo);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el préstamo con ID: " + idPrestamo, e);
        }
        return prestamo;
    }

    /**
     * Obtiene todos los préstamos de la base de datos.
     *
     * @return Una lista con todos los préstamos.
     */
    public List<Prestamo> obtenerTodosLosPrestamos() {
        logger.info("Obteniendo lista de todos los préstamos.");
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
            logger.info("Total de préstamos obtenidos: " + prestamos.size());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la lista de préstamos.", e);
        }
        return prestamos;
    }

    /**
     * Obtiene todos los préstamos activos (aquellos que no han sido registrados en el histórico).
     *
     * @return Una lista con los préstamos activos.
     */
    public List<Prestamo> obtenerPrestamosActivos() {
        logger.info("Obteniendo lista de préstamos activos.");
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
            logger.info("Total de préstamos activos: " + prestamos.size());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los préstamos activos.", e);
        }
        return prestamos;
    }

    /**
     * Actualiza los datos de un préstamo en la base de datos.
     *
     * @param prestamo El préstamo con los datos actualizados.
     */
    public void actualizarPrestamo(Prestamo prestamo) {
        logger.info("Actualizando préstamo con ID: " + prestamo.getIdPrestamo());
        String sql = "UPDATE " + TABLE_NAME + " SET dni_alumno = ?, codigo_libro = ?, fecha_prestamo = ? WHERE id_prestamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, prestamo.getDniAlumno());
            stmt.setInt(2, prestamo.getCodigoLibro());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.setInt(4, prestamo.getIdPrestamo());
            stmt.executeUpdate();
            logger.info("Préstamo actualizado correctamente con ID: " + prestamo.getIdPrestamo());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar el préstamo con ID: " + prestamo.getIdPrestamo(), e);
        }
    }

    /**
     * Elimina un préstamo de la base de datos utilizando su ID.
     *
     * @param idPrestamo El ID del préstamo a eliminar.
     * @return true si el préstamo fue eliminado exitosamente, false si no se encontró o ocurrió un error.
     */
    public boolean eliminarPrestamo(int idPrestamo) {
        logger.info("Intentando eliminar préstamo con ID: " + idPrestamo);
        String sql = "DELETE FROM Prestamo WHERE id_prestamo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPrestamo);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Préstamo eliminado exitosamente con ID: " + idPrestamo);
                return true;
            } else {
                logger.warning("No se encontró el préstamo con ID: " + idPrestamo + " para eliminar.");
                return false;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el préstamo con ID: " + idPrestamo, e);
            return false;
        }
    }

    public void eliminarPorDni(String dni) {
        logger.info("Eliminando todos los préstamos del alumno con DNI: " + dni);
        String sql = "DELETE FROM Prestamo WHERE dni_alumno = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            pstmt.executeUpdate();
            logger.info("Préstamos eliminados correctamente para el alumno con DNI: " + dni);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar préstamos del alumno con DNI: " + dni, e);
        }
    }

    /**
     * Elimina todos los préstamos asociados a un libro utilizando su código.
     *
     * @param codigoLibro El código del libro cuyos préstamos serán eliminados.
     */
    public void eliminarPorCodigoLibro(int codigoLibro) {
        logger.info("Eliminando todos los préstamos asociados al libro con código: " + codigoLibro);
        String sql = "DELETE FROM Prestamo WHERE codigo_libro = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigoLibro);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Préstamos eliminados correctamente para el libro con código: " + codigoLibro);
            } else {
                logger.warning("No se encontraron préstamos para el libro con código: " + codigoLibro);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar préstamos del libro con código: " + codigoLibro, e);
        }
    }
}
