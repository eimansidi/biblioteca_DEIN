package com.eiman.biblioteca.models;

import java.util.Base64;

/**
 * Clase que representa un libro dentro de la biblioteca.
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
     * @param codigo    Código identificador del libro.
     * @param titulo    Título del libro.
     * @param autor     Autor del libro.
     * @param editorial Editorial del libro.
     * @param estado    Estado del libro (Ej. "Disponible", "Prestado").
     * @param baja      Indica si el libro está dado de baja (0: no, 1: sí).
     * @param portada   Imagen de la portada del libro en formato byte[].
     */
    public Libro(int codigo, String titulo, String autor, String editorial, String estado, int baja, byte[] portada) {
        if (codigo < 0) { // Cambiar de <= 0 a < 0 para permitir 0 cuando sea AUTO_INCREMENT
            throw new IllegalArgumentException("El código del libro no puede ser negativo.");
        }
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.estado = estado != null ? estado : "Desconocido";
        this.baja = baja;
        this.portada = portada;
    }


    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código del libro debe ser positivo.");
        }
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getBaja() {
        return baja;
    }

    public void setBaja(int baja) {
        this.baja = baja;
    }

    public byte[] getPortada() {
        return portada;
    }

    public void setPortada(byte[] portada) {
        this.portada = portada;
    }

    /**
     * Devuelve la portada codificada en Base64 para facilitar su visualización.
     *
     * @return Cadena Base64 de la imagen de la portada.
     */
    public String getPortadaBase64() {
        return portada != null ? Base64.getEncoder().encodeToString(portada) : null;
    }

    @Override
    public String toString() {
        return titulo + " - " + autor + " (" + estado + ")";
    }
}
