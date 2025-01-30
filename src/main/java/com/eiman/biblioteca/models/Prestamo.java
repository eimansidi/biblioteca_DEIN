package com.eiman.biblioteca.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un prestamo de un libro a un alumno.
 * Esta clase almacena la informacion del prestamo, incluyendo el id, el DNI del alumno, el codigo del libro
 * y la fecha en que se realiza el prestamo.
 */
public class Prestamo {

    private int idPrestamo;
    private String dniAlumno;
    private int codigoLibro;
    private LocalDateTime fechaPrestamo;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Constructor del prestamo.
     *
     * @param idPrestamo Identificador del prestamo.
     * @param dniAlumno DNI del alumno que realiza el prestamo.
     * @param codigoLibro Codigo del libro prestado.
     * @param fechaPrestamo Fecha en que se realiza el prestamo.
     * @throws IllegalArgumentException Si el idPrestamo es negativo, el codigoLibro es negativo o el dniAlumno esta vacio.
     */
    public Prestamo(int idPrestamo, String dniAlumno, int codigoLibro, LocalDateTime fechaPrestamo) {
        if (idPrestamo < 0) { // Cambiado de <= 0 a < 0 para permitir 0
            throw new IllegalArgumentException("El ID del prestamo no puede ser negativo.");
        }
        if (codigoLibro < 0) {
            throw new IllegalArgumentException("El codigo del libro debe ser positivo.");
        }
        if (dniAlumno == null || dniAlumno.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI del alumno no puede estar vacio.");
        }
        this.idPrestamo = idPrestamo;
        this.dniAlumno = dniAlumno;
        this.codigoLibro = codigoLibro;
        this.fechaPrestamo = fechaPrestamo != null ? fechaPrestamo : LocalDateTime.now();
    }

    /**
     * Obtiene el ID del prestamo.
     *
     * @return El ID del prestamo.
     */
    public int getIdPrestamo() {
        return idPrestamo;
    }

    /**
     * Establece el ID del prestamo.
     *
     * @param idPrestamo El ID del prestamo.
     * @throws IllegalArgumentException Si el idPrestamo es negativo o cero.
     */
    public void setIdPrestamo(int idPrestamo) {
        if (idPrestamo <= 0) {
            throw new IllegalArgumentException("El ID del prestamo debe ser positivo.");
        }
        this.idPrestamo = idPrestamo;
    }

    /**
     * Obtiene el DNI del alumno que realizo el prestamo.
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
     * @throws IllegalArgumentException Si el DNI esta vacio.
     */
    public void setDniAlumno(String dniAlumno) {
        if (dniAlumno == null || dniAlumno.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI del alumno no puede estar vacio.");
        }
        this.dniAlumno = dniAlumno;
    }

    /**
     * Obtiene el codigo del libro prestado.
     *
     * @return El codigo del libro prestado.
     */
    public int getCodigoLibro() {
        return codigoLibro;
    }

    /**
     * Establece el codigo del libro prestado.
     *
     * @param codigoLibro El codigo del libro.
     * @throws IllegalArgumentException Si el codigo del libro es negativo o cero.
     */
    public void setCodigoLibro(int codigoLibro) {
        if (codigoLibro <= 0) {
            throw new IllegalArgumentException("El codigo del libro debe ser positivo.");
        }
        this.codigoLibro = codigoLibro;
    }

    /**
     * Obtiene la fecha en que se realizo el prestamo.
     *
     * @return La fecha del prestamo.
     */
    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    /**
     * Establece la fecha en que se realizo el prestamo.
     *
     * @param fechaPrestamo La fecha del prestamo.
     */
    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    /**
     * Devuelve una representacion en cadena del prestamo.
     *
     * @return La representacion en cadena del prestamo, con el ID, el DNI del alumno, el codigo del libro y la fecha del prestamo.
     */
    @Override
    public String toString() {
        return "Prestamo: " + idPrestamo + " - Alumno: " + dniAlumno +
                " - Libro: " + codigoLibro + " - Fecha: " + fechaPrestamo.format(FORMATTER);
    }
}
