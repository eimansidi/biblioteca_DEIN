package com.eiman.biblioteca.models;

/**
 * Clase que representa un alumno en el sistema.
 * Un alumno tiene un DNI, nombre, primer apellido y segundo apellido.
 */
public class Alumno {
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;

    /**
     * Constructor de la clase Alumno.
     *
     * @param dni El DNI del alumno.
     * @param nombre El nombre del alumno.
     * @param apellido1 El primer apellido del alumno.
     * @param apellido2 El segundo apellido del alumno.
     */
    public Alumno(String dni, String nombre, String apellido1, String apellido2) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    /**
     * Obtiene el DNI del alumno.
     *
     * @return El DNI del alumno.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del alumno.
     *
     * @param dni El DNI del alumno.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del alumno.
     *
     * @return El nombre del alumno.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del alumno.
     *
     * @param nombre El nombre del alumno.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el primer apellido del alumno.
     *
     * @return El primer apellido del alumno.
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Establece el primer apellido del alumno.
     *
     * @param apellido1 El primer apellido del alumno.
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * Obtiene el segundo apellido del alumno.
     *
     * @return El segundo apellido del alumno.
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Establece el segundo apellido del alumno.
     *
     * @param apellido2 El segundo apellido del alumno.
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    /**
     * Devuelve una representación en cadena del nombre completo del alumno.
     *
     * @return El nombre completo del alumno, en formato "nombre apellido1 apellido2".
     */
    @Override
    public String toString() {
        return nombre + " " + apellido1 + (apellido2 != null ? " " + apellido2 : "");
    }
}
