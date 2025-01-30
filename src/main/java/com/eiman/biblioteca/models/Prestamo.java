package com.eiman.biblioteca.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un préstamo de un libro a un alumno.
 */
public class Prestamo {

    private int idPrestamo;
    private String dniAlumno;
    private int codigoLibro;
    private LocalDateTime fechaPrestamo;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Constructor del préstamo.
     *
     * @param idPrestamo    Identificador del préstamo.
     * @param dniAlumno     DNI del alumno que realiza el préstamo.
     * @param codigoLibro   Código del libro prestado.
     * @param fechaPrestamo Fecha en que se realiza el préstamo.
     */
    public Prestamo(int idPrestamo, String dniAlumno, int codigoLibro, LocalDateTime fechaPrestamo) {
        if (idPrestamo < 0) { // Cambiado de <= 0 a < 0 para permitir 0
            throw new IllegalArgumentException("El ID del préstamo no puede ser negativo.");
        }
        if (codigoLibro < 0) {
            throw new IllegalArgumentException("El código del libro debe ser positivo.");
        }
        if (dniAlumno == null || dniAlumno.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI del alumno no puede estar vacío.");
        }
        this.idPrestamo = idPrestamo;
        this.dniAlumno = dniAlumno;
        this.codigoLibro = codigoLibro;
        this.fechaPrestamo = fechaPrestamo != null ? fechaPrestamo : LocalDateTime.now();
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        if (idPrestamo <= 0) {
            throw new IllegalArgumentException("El ID del préstamo debe ser positivo.");
        }
        this.idPrestamo = idPrestamo;
    }

    public String getDniAlumno() {
        return dniAlumno;
    }

    public void setDniAlumno(String dniAlumno) {
        if (dniAlumno == null || dniAlumno.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI del alumno no puede estar vacío.");
        }
        this.dniAlumno = dniAlumno;
    }

    public int getCodigoLibro() {
        return codigoLibro;
    }

    public void setCodigoLibro(int codigoLibro) {
        if (codigoLibro <= 0) {
            throw new IllegalArgumentException("El código del libro debe ser positivo.");
        }
        this.codigoLibro = codigoLibro;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    @Override
    public String toString() {
        return "Préstamo: " + idPrestamo + " - Alumno: " + dniAlumno +
                " - Libro: " + codigoLibro + " - Fecha: " + fechaPrestamo.format(FORMATTER);
    }
}
