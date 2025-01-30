package com.eiman.biblioteca.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class LanguageManager {
    private static final String CONFIG_FILE = "/config.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream input = LanguageManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new IOException("Archivo de configuración no encontrado");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // En caso de error al cargar el archivo, podemos usar idioma por defecto
            properties.setProperty("language", "es");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key, "");
    }

    public static Locale getLocale() {
        String lang = getProperty("language");
        if (lang == null || (!lang.equals("es") && !lang.equals("en"))) {
            lang = "es"; // Idioma por defecto
        }
        return lang.equals("es") ? new Locale("es", "ES") : new Locale("en", "US");
    }
}
