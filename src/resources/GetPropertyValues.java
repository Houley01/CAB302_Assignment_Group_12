package resources;// Read information from server properties

import java.io.*;
import java.util.*;

/**
 * Gets the property values from the server property file and
 * allocates those values into variables for other sections of
 * code to read from.
 */
public class GetPropertyValues {
    public static String serverName;
    public static int port;
    static InputStream inputStream;


    /**
     * Streams the server property file and sets the class variables to the values supplied
     * in the properties file.
     * @see serverName
     * @see port
     * @see inputStream
     *
     *
     * @throws IOException
     * @throws FileNotFoundException        If the server properties file cannot be found or read
     */
    public static void ReadPropertyFile() throws IOException {


        try {
            Properties prop = new Properties();
            String propFileName = "resources/server.properties";

            inputStream = GetPropertyValues.class.getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }


            // get the property value and print it out
            serverName = prop.getProperty("serverLocation");
            port = Integer.parseInt(prop.getProperty("port"));

//            String result = "Sever Location: " + serverName + "\nPort Number: " + port;
//            System.out.println(result);

        } catch (Exception e) {
//            System.out.println("Exception: " + e);
        }
        inputStream.close();

    }
}
