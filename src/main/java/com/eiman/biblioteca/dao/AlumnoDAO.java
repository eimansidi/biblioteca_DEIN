package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase DAO que gestiona la conexion y operaciones CRUD para la tabla Alumno.
 * Permite insertar, obtener, actualizar y eliminar alumnos en la base de datos.
 */
public class AlumnoDAO {
    private static final Logger logger = Logger.getLogger(AlumnoDAO.class.getName());
    private static final String TABLE_NAME = "Alumno";

    /**
     * Inserta un nuevo alumno en la base de datos.
     * Si el alumno ya existe (mismo DNI), lanzará una excepción.
     *
     * @param alumno El alumno a insertar.
     */
    public void insertarAlumno(Alumno alumno) {
        logger.info("Intentando insertar un nuevo alumno con DNI: " + alumno.getDni());
        String sql = "INSERT INTO Alumno (dni, nombre, apellido1, apellido2) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, alumno.getDni());
            stmt.setString(2, alumno.getNombre());
            stmt.setString(3, alumno.getApellido1());
            stmt.setString(4, alumno.getApellido2());
            stmt.executeUpdate();
            logger.info("Alumno insertado exitosamente: " + alumno.getDni());

        } catch (SQLIntegrityConstraintViolationException e) {
            logger.log(Level.WARNING, "Intento de insertar un alumno con DNI duplicado: " + alumno.getDni(), e);
            throw new IllegalArgumentException("Ya existe un alumno con el mismo DNI.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar el alumno con DNI: " + alumno.getDni(), e);
        }
    }

    /**
     * Obtiene un alumno de la base de datos usando su DNI.
     *
     * @param dni El DNI del alumno.
     * @return El alumno con el DNI especificado, o null si no se encuentra.
     */
    public Alumno obtenerAlumnoPorDni(String dni) {
        logger.info("Buscando alumno con DNI: " + dni);
        Alumno alumno = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE dni = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                alumno = new Alumno(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2")
                );
                logger.info("Alumno encontrado: " + alumno.getDni());
            } else {
                logger.info("No se encontró ningún alumno con DNI: " + dni);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el alumno con DNI: " + dni, e);
        }
        return alumno;
    }

    /**
     * Obtiene todos los alumnos de la base de datos.
     *
     * @return Una lista con todos los alumnos.
     */
    public List<Alumno> obtenerTodosLosAlumnos() {
        logger.info("Obteniendo lista de todos los alumnos.");
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2")
                );
                alumnos.add(alumno);
            }
            logger.info("Total de alumnos obtenidos: " + alumnos.size());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la lista de alumnos.", e);
        }
        return alumnos;
    }

    /**
     * Actualiza los datos de un alumno en la base de datos.
     *
     * @param alumno El alumno con los datos actualizados.
     */
    public void actualizarAlumno(Alumno alumno) {
        logger.info("Intentando actualizar datos del alumno con DNI: " + alumno.getDni());
        String sql = "UPDATE " + TABLE_NAME + " SET nombre = ?, apellido1 = ?, apellido2 = ? WHERE dni = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellido1());
            stmt.setString(3, alumno.getApellido2());
            stmt.setString(4, alumno.getDni());
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                logger.info("Alumno actualizado exitosamente: " + alumno.getDni());
            } else {
                logger.warning("No se encontró el alumno con DNI: " + alumno.getDni() + " para actualizar.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar el alumno con DNI: " + alumno.getDni(), e);
        }
    }

    /**
     * Elimina un alumno de la base de datos usando su DNI.
     *
     * @param dni El DNI del alumno a eliminar.
     * @return true si el alumno fue eliminado exitosamente, false si no se encontró o ocurrió un error.
     */
    public boolean eliminarAlumno(String dni) {
        logger.info("Intentando eliminar alumno con DNI: " + dni);
        String sql = "DELETE FROM Alumno WHERE dni = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Alumno eliminado exitosamente: " + dni);
                return true;
            } else {
                logger.warning("No se encontró el alumno con DNI: " + dni + " para eliminar.");
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el alumno con DNI: " + dni, e);
            return false;
        }
    }
}
