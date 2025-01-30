package com.eiman.biblioteca.dao;

import com.eiman.biblioteca.models.Alumno;
import com.eiman.biblioteca.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    private static final String TABLE_NAME = "Alumno";

    // Método para insertar un nuevo alumno
    public void insertarAlumno(Alumno alumno) {
        String sql = "INSERT INTO Alumno (dni, nombre, apellido1, apellido2) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, alumno.getDni());
            stmt.setString(2, alumno.getNombre());
            stmt.setString(3, alumno.getApellido1());
            stmt.setString(4, alumno.getApellido2());
            stmt.executeUpdate();

            System.out.println("Alumno insertado con éxito.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Error: Ya existe un alumno con el mismo DNI.");
            throw new IllegalArgumentException("Ya existe un alumno con el mismo DNI.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al insertar el alumno.");
        }
    }

    // Método para obtener un alumno por su DNI
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
            System.err.println("Error al obtener el alumno.");
        }
        return alumno;
    }

    // Método para obtener todos los alumnos
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
            System.err.println("Error al obtener los alumnos.");
        }
        return alumnos;
    }

    // Método para actualizar los datos de un alumno
    public void actualizarAlumno(Alumno alumno) {
        String sql = "UPDATE " + TABLE_NAME + " SET nombre = ?, apellido1 = ?, apellido2 = ? WHERE dni = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellido1());
            stmt.setString(3, alumno.getApellido2());
            stmt.setString(4, alumno.getDni());
            stmt.executeUpdate();
            System.out.println("Alumno actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el alumno.");
        }
    }

    // Método para eliminar un alumno
    public void eliminarAlumno(String dni) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE dni = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.executeUpdate();
            System.out.println("Alumno eliminado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al eliminar el alumno.");
        }
    }
}