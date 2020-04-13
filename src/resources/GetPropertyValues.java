package resources;// Read information from server properties
import server.initialisation.ServerInit;

import java.io.*;
import java.util.*;

public class GetPropertyValues {
    public static String serverName;
    public static int port;
    static InputStream inputStream;
    public static void readPropertyFile() throws IOException {


        try {
            Properties prop = new Properties();
            String propFileName = "resources\\server.properties";

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
            System.out.println("Exception: " + e);
        }
        inputStream.close();

    }
}
