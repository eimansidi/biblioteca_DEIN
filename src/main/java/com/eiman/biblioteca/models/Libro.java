package com.eiman.biblioteca.models;

import java.util.Base64;

/**
 * Clase que representa un libro dentro de la biblioteca.
 * Un libro tiene un codigo, titulo, autor, editorial, estado, baja y portada.
 */
public class Libro {

    private int codigo;
    private String titulo;
    private String autor;
    private String editorial;
    private String estado;
    private int baja;
    private byte[] portada;

    /**
     * Constructor de la clase Libro.
     *
     * @param codigo    Codigo identificador del libro.
     * @param titulo    Titulo del libro.
     * @param autor     Autor del libro.
     * @param editorial Editorial del libro.
     * @param estado    Estado del libro (Ej. "Disponible", "Prestado").
     * @param baja      Indica si el libro esta dado de baja (0: no, 1: si).
     * @param portada   Imagen de la portada del libro en formato byte[].
     */
    public Libro(int codigo, String titulo, String autor, String editorial, String estado, int baja, byte[] portada) {
        if (codigo < 0) { // Cambiar de <= 0 a < 0 para permitir 0 cuando sea AUTO_INCREMENT
            throw new IllegalArgumentException("El codigo del libro no puede ser negativo.");
        }
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado != null ? estado : "Desconocido";
        this.baja = baja;
        this.portada = portada;
    }

    /**
     * Obtiene el codigo del libro.
     *
     * @return El codigo del libro.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el codigo del libro.
     *
     * @param codigo El codigo del libro.
     * @throws IllegalArgumentException Si el codigo es menor o igual a 0.
     */
    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El codigo del libro debe ser positivo.");
        }
        this.codigo = codigo;
    }

    /**
     * Obtiene el titulo del libro.
     *
     * @return El titulo del libro.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el titulo del libro.
     *
     * @param titulo El titulo del libro.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el autor del libro.
     *
     * @return El autor del libro.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Establece el autor del libro.
     *
     * @param autor El autor del libro.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtiene la editorial del libro.
     *
     * @return La editorial del libro.
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Establece la editorial del libro.
     *
     * @param editorial La editorial del libro.
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * Obtiene el estado del libro (Ej. "Disponible", "Prestado").
     *
     * @return El estado del libro.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del libro.
     *
     * @param estado El estado del libro.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el valor de baja del libro (0: no, 1: si).
     *
     * @return El valor de baja del libro.
     */
    public int getBaja() {
        return baja;
    }

    /**
     * Establece el valor de baja del libro.
     *
     * @param baja El valor de baja del libro.
     */
    public void setBaja(int baja) {
        this.baja = baja;
    }

    /**
     * Obtiene la portada del libro como un arreglo de bytes.
     *
     * @return La portada en formato byte[].
     */
    public byte[] getPortada() {
        return portada;
    }

    /**
     * Establece la portada del libro.
     *
     * @param portada La portada del libro en formato byte[].
     */
    public void setPortada(byte[] portada) {
        this.portada = portada;
    }

    /**
     * Devuelve la portada codificada en Base64 para facilitar su visualizacion.
     *
     * @return Cadena Base64 de la imagen de la portada.
     */
    public String getPortadaBase64() {
        return portada != null ? Base64.getEncoder().encodeToString(portada) : null;
    }

    /**
     * Devuelve una representacion en cadena del libro, mostrando el titulo, autor y estado.
     *
     * @return La representacion en cadena del libro.
     */
    @Override
    public String toString() {
        return titulo + " - " + autor + " (" + estado + ")";
    }
}
