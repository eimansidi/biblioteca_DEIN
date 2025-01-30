package com.eiman.biblioteca.utils;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Clase que gestiona la configuracion de idioma de la aplicacion.
 * Permite cargar, obtener, actualizar y guardar el idioma de la aplicacion desde un archivo de configuracion.
 */
public class LanguageManager {
    private static final String CONFIG_FILE = "/config.properties";
    private static Properties properties = new Properties();
    private static Locale currentLocale;
    private static ResourceBundle messages;

    static {
        cargarIdioma();
    }

    /**
     * Carga el idioma de la aplicacion desde el archivo de configuracion.
     * Si no se encuentra la configuracion, establece el idioma por defecto ("es").
     */
    private static void cargarIdioma() {
        try (InputStream input = LanguageManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new IOException("Archivo de configuracion no encontrado");
            }
        } catch (IOException e) {
            e.printStackTrace();
            properties.setProperty("language", "es");
        }
        actualizarLocale();
    }

    /**
     * Actualiza el locale y el ResourceBundle en base al idioma cargado desde la configuracion.
     */
    private static void actualizarLocale() {
        String lang = properties.getProperty("language", "es");
        currentLocale = lang.equals("es") ? new Locale("es", "ES") : new Locale("en", "US");
        messages = ResourceBundle.getBundle("i18n.messages", currentLocale);
    }

    /**
     * Obtiene el valor asociado a una clave en el archivo de recursos del idioma.
     * Si no se encuentra la clave, devuelve una cadena con el formato "??? key ???".
     *
     * @param key La clave del mensaje a obtener.
     * @return El valor asociado a la clave o "??? key ???" si no se encuentra.
     */
    public static String getProperty(String key) {
        return messages.containsKey(key) ? messages.getString(key) : "??? " + key + " ???";
    }

    /**
     * Obtiene el locale actual.
     *
     * @return El locale actual.
     */
    public static Locale getLocale() {
        return currentLocale;
    }

    /**
     * Establece el idioma de la aplicacion y actualiza el locale.
     *
     * @param lang El codigo del idioma a establecer ("es" o "en").
     */
    public static void setLanguage(String lang) {
        properties.setProperty("language", lang);
        actualizarLocale();
        guardarIdioma();
    }

    /**
     * Guarda la configuracion del idioma en el archivo de configuracion.
     */
    private static void guardarIdioma() {
        try (OutputStream output = new FileOutputStream("config.properties")) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
