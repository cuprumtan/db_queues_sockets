package edu.psu.receiver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    public static String LoadProperty(String propertyName) {

        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("conf.properties");
            property.load(fis);

        } catch (IOException e) {
            System.err.println("ERROR: No properties file!");
        }

        return property.getProperty(propertyName);
    }

}
