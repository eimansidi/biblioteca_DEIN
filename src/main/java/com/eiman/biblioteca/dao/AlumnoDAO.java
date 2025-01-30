package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO que gestiona la conexion y operaciones CRUD para la tabla Alumno.
 * Permite insertar, obtener, actualizar y eliminar alumnos en la base de datos.
 */
public class AlumnoDAO {

    private static final String TABLE_NAME = "Alumno";

    /**
     * Inserta un nuevo alumno en la base de datos.
     * Si el alumno ya existe (mismo DNI), lanzara una excepcion.
     *
     * @param alumno El alumno a insertar.
     */
    public void insertarAlumno(Alumno alumno) {
        String sql = "INSERT INTO Alumno (dni, nombre, apellido1, apellido2) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, alumno.getDni());
            stmt.setString(2, alumno.getNombre());
            stmt.setString(3, alumno.getApellido1());
            stmt.setString(4, alumno.getApellido2());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Ya existe un alumno con el mismo DNI.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un alumno de la base de datos usando su DNI.
     *
     * @param dni El DNI del alumno.
     * @return El alumno con el DNI especificado, o null si no se encuentra.
     */
    public Alumno obtenerAlumnoPorDni(String dni) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumno;
    }

    /**
     * Obtiene todos los alumnos de la base de datos.
     *
     * @return Una lista con todos los alumnos.
     */
    public List<Alumno> obtenerTodosLosAlumnos() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumnos;
    }

    /**
     * Actualiza los datos de un alumno en la base de datos.
     *
     * @param alumno El alumno con los datos actualizados.
     */
    public void actualizarAlumno(Alumno alumno) {
        String sql = "UPDATE " + TABLE_NAME + " SET nombre = ?, apellido1 = ?, apellido2 = ? WHERE dni = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellido1());
            stmt.setString(3, alumno.getApellido2());
            stmt.setString(4, alumno.getDni());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un alumno de la base de datos usando su DNI.
     *
     * @param dni El DNI del alumno a eliminar.
     * @return true si el alumno fue eliminado exitosamente, false si no se encontro o ocurrio un error.
     */
    public boolean eliminarAlumno(String dni) {
        String sql = "DELETE FROM Alumno WHERE dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            int affectedRows = pstmt.executeUpdate(); // Retorna numero de filas afectadas
            return affectedRows > 0; // Si se elimino al menos 1 fila, devuelve true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
