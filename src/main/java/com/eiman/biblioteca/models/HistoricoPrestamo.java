package com.eiman.biblioteca.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa el histórico de un préstamo de libros.
 */
public class HistoricoPrestamo {

    private int idPrestamo;
    private String dniAlumno;
    private int codigoLibro;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;

    /**
     * Constructor de la clase HistoricoPrestamo.
     * @param idPrestamo Identificador del préstamo.
     * @param dniAlumno DNI del alumno que solicita el préstamo.
     * @param codigoLibro Código del libro prestado.
     * @param fechaPrestamo Fecha en la que se realizó el préstamo.
     * @param fechaDevolucion Fecha en la que se devolvió el libro.
     */
    public HistoricoPrestamo(int idPrestamo, String dniAlumno, int codigoLibro,
                             LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion) {
        this.idPrestamo = idPrestamo;
        this.dniAlumno = dniAlumno;
        this.codigoLibro = codigoLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getDniAlumno() {
        return dniAlumno;
    }

    public void setDniAlumno(String dniAlumno) {
        this.dniAlumno = dniAlumno;
    }

    public int getCodigoLibro() {
        return codigoLibro;
    }

    public void setCodigoLibro(int codigoLibro) {
        this.codigoLibro = codigoLibro;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        if (fechaDevolucion.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior a la fecha del préstamo.");
        }
        this.fechaDevolucion = fechaDevolucion;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return "Histórico Préstamo: " + idPrestamo +
                " - Alumno: " + dniAlumno +
                " - Libro: " + codigoLibro +
                " - Fecha Préstamo: " + fechaPrestamo.format(formatter) +
                " - Fecha Devolución: " + (fechaDevolucion != null ? fechaDevolucion.format(formatter) : "No devuelto");
    }
}
