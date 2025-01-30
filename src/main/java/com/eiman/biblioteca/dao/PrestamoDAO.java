package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Prestamo;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO que gestiona la conexion y operaciones CRUD para la tabla Prestamo.
 * Permite insertar, obtener, actualizar y eliminar prestamos en la base de datos.
 */
public class PrestamoDAO {

    private static final String TABLE_NAME = "Prestamo";

    /**
     * Inserta un nuevo prestamo en la base de datos.
     *
     * @param prestamo El prestamo a insertar.
     */
    public void insertarPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO " + TABLE_NAME + " (dni_alumno, codigo_libro, fecha_prestamo) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, prestamo.getDniAlumno());
            stmt.setInt(2, prestamo.getCodigoLibro());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un prestamo de la base de datos utilizando su ID.
     *
     * @param idPrestamo El ID del prestamo.
     * @return El prestamo con el ID especificado, o null si no se encuentra.
     */
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
        }
        return prestamo;
    }

    /**
     * Obtiene todos los prestamos de la base de datos.
     *
     * @return Una lista con todos los prestamos.
     */
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
        }
        return prestamos;
    }

    /**
     * Obtiene todos los prestamos activos (es decir, aquellos no registrados en el historico).
     *
     * @return Una lista con los prestamos activos.
     */
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
        }
        return prestamos;
    }

    /**
     * Actualiza los datos de un prestamo en la base de datos.
     *
     * @param prestamo El prestamo con los datos actualizados.
     */
    public void actualizarPrestamo(Prestamo prestamo) {
        String sql = "UPDATE " + TABLE_NAME + " SET dni_alumno = ?, codigo_libro = ?, fecha_prestamo = ? WHERE id_prestamo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, prestamo.getDniAlumno());
            stmt.setInt(2, prestamo.getCodigoLibro());
            stmt.setTimestamp(3, Timestamp.valueOf(prestamo.getFechaPrestamo()));
            stmt.setInt(4, prestamo.getIdPrestamo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un prestamo de la base de datos utilizando su ID.
     *
     * @param idPrestamo El ID del prestamo a eliminar.
     * @return true si el prestamo fue eliminado exitosamente, false si no se encontro o ocurrio un error.
     */
    public boolean eliminarPrestamo(int idPrestamo) {
        String sql = "DELETE FROM Prestamo WHERE id_prestamo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPrestamo);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
