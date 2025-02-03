package com.eiman.biblioteca.utils;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que gestiona la configuración de idioma de la aplicación.
 * Permite cargar, obtener, actualizar y guardar el idioma de la aplicación desde un archivo de configuración.
 */
public class LanguageManager {
    private static final Logger logger = Logger.getLogger(LanguageManager.class.getName());
    private static final String CONFIG_FILE = "/config.properties";
    private static Properties properties = new Properties();
    private static Locale currentLocale;
    private static ResourceBundle messages;

    static {
        cargarIdioma();
    }

    /**
     * Carga el idioma de la aplicación desde el archivo de configuración.
     * Si no se encuentra la configuración, establece el idioma por defecto ("es").
     */
    private static void cargarIdioma() {
        try (InputStream input = LanguageManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                logger.info("Idioma cargado desde el archivo de configuración: " + properties.getProperty("language"));
            } else {
                throw new IOException("Archivo de configuración no encontrado");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al cargar el archivo de configuración. Se usará el idioma por defecto (es).", e);
            properties.setProperty("language", "es");
        }
        actualizarLocale();
    }

    /**
     * Actualiza el locale y el ResourceBundle en base al idioma cargado desde la configuración.
     */
    private static void actualizarLocale() {
        String lang = properties.getProperty("language", "es");
        currentLocale = lang.equals("es") ? new Locale("es", "ES") : new Locale("en", "US");
        messages = ResourceBundle.getBundle("i18n.messages", currentLocale);
        logger.info("Locale actualizado a: " + currentLocale);
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
     * Obtiene el locale actual de la aplicación.
     *
     * @return El locale actual.
     */
    public static Locale getLocale() {
        return currentLocale;
    }

    /**
     * Establece el idioma de la aplicación y actualiza el locale.
     *
     * @param lang El código del idioma a establecer ("es" o "en").
     */
    public static void setLanguage(String lang) {
        logger.info("Cambiando el idioma a: " + lang);
        properties.setProperty("language", lang);
        actualizarLocale();
        guardarIdioma();
    }

    /**
     * Guarda la configuración del idioma en el archivo de configuración.
     * En caso de error, se registrará el problema.
     */
    private static void guardarIdioma() {
        logger.info("Guardando la configuración de idioma en el archivo.");
        try (OutputStream output = new FileOutputStream("config.properties")) {
            properties.store(output, "Configuración de idioma actualizada");
            logger.info("Idioma guardado en archivo de configuración: " + properties.getProperty("language"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al guardar el idioma en el archivo de configuración.", e);
        }
    }
}
