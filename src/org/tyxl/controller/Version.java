package org.tyxl.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Version {
    private static String VERSION = "0.9 [BETA] ";
    private static String TIMESTAMP = "";

    private static boolean initialized = false;
    
    static String getVersion() {
        return VERSION;
    }
    
    synchronized static String getTimestamp() {
        if (!initialized) {
            initialize();
        }
        return TIMESTAMP;
    }
    
    private static void initialize() {
        String timestamp = "15/1/2015";
        try {
            Class clazz = Version.class;
            String className = clazz.getSimpleName() + ".class";
            String classPath = clazz.getResource(className).toString();

            if (classPath.startsWith("jar")) {
                InputStream is = clazz.getResourceAsStream("/resources/build.properties");
                Properties props = new Properties();
                props.load(is);
                is.close();
                timestamp = props.getProperty("Build-Date");
            }
        } catch (IOException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        TIMESTAMP = timestamp;
    }
}
