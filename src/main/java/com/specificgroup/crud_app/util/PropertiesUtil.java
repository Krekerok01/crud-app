package com.specificgroup.crud_app.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Util class for searching properties in application.properties file.
 */
public class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();
    private static final String PATH_FILL = "application.properties";

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    static void loadProperties() {
        try (InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PATH_FILL)) {
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}