package com.eiman.biblioteca.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa el historico de un prestamo de libros.
 * Esta clase almacena la informacion de un prestamo, incluyendo la fecha de prestamo y de devolucion.
 */
public class HistoricoPrestamo {

    private int idPrestamo;
    private String dniAlumno;
    private int codigoLibro;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;

    /**
     * Constructor de la clase HistoricoPrestamo.
     *
     * @param idPrestamo Identificador del prestamo.
     * @param dniAlumno DNI del alumno que solicita el prestamo.
     * @param codigoLibro Codigo del libro prestado.
     * @param fechaPrestamo Fecha en la que se realizo el prestamo.
     * @param fechaDevolucion Fecha en la que se devolvio el libro.
     */
    public HistoricoPrestamo(int idPrestamo, String dniAlumno, int codigoLibro,
                             LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion) {
        this.idPrestamo = idPrestamo;
        this.dniAlumno = dniAlumno;
        this.codigoLibro = codigoLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * Obtiene el id del prestamo.
     *
     * @return El id del prestamo.
     */
    public int getIdPrestamo() {
        return idPrestamo;
    }

    /**
     * Establece el id del prestamo.
     *
     * @param idPrestamo El id del prestamo.
     */
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    /**
     * Obtiene el DNI del alumno.
     *
     * @return El DNI del alumno.
     */
    public String getDniAlumno() {
        return dniAlumno;
    }

    /**
     * Establece el DNI del alumno.
     *
     * @param dniAlumno El DNI del alumno.
     */
    public void setDniAlumno(String dniAlumno) {
        this.dniAlumno = dniAlumno;
    }

    /**
     * Obtiene el codigo del libro prestado.
     *
     * @return El codigo del libro.
     */
    public int getCodigoLibro() {
        return codigoLibro;
    }

    /**
     * Establece el codigo del libro prestado.
     *
     * @param codigoLibro El codigo del libro.
     */
    public void setCodigoLibro(int codigoLibro) {
        this.codigoLibro = codigoLibro;
    }

    /**
     * Obtiene la fecha de prestamo.
     *
     * @return La fecha del prestamo.
     */
    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    /**
     * Establece la fecha de prestamo.
     *
     * @param fechaPrestamo La fecha de prestamo.
     */
    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    /**
     * Obtiene la fecha de devolucion.
     *
     * @return La fecha de devolucion.
     */
    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    /**
     * Establece la fecha de devolucion.
     * Si la fecha de devolucion es anterior a la fecha de prestamo, lanza una excepcion.
     *
     * @param fechaDevolucion La fecha de devolucion.
     * @throws IllegalArgumentException Si la fecha de devolucion es anterior a la fecha de prestamo.
     */
    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        if (fechaDevolucion.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException("La fecha de devolucion no puede ser anterior a la fecha del prestamo.");
        }
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * Devuelve una representacion en cadena del historico de prestamo.
     *
     * @return La representacion en cadena del historico de prestamo con todos los detalles.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return "Historico Prestamo: " + idPrestamo +
                " - Alumno: " + dniAlumno +
                " - Libro: " + codigoLibro +
                " - Fecha Prestamo: " + fechaPrestamo.format(formatter) +
                " - Fecha Devolucion: " + (fechaDevolucion != null ? fechaDevolucion.format(formatter) : "No devuelto");
    }
}
