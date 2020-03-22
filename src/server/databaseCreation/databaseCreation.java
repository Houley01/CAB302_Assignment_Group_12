package server.databaseCreation;

import server.initialisation.ServerInit;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class databaseCreation {
    // Check if the database has tables
    public static void checkDB() throws SQLException {
        System.out.println("Checking if DB exists...");

        DatabaseMetaData dbm = ServerInit.conn.getMetaData();

        ResultSet resultSet = ServerInit.conn.getMetaData().getCatalogs();

        Boolean databaseExists = false;

        //iterate each catalog in the ResultSet
        while (resultSet.next()) {
            // Get the database name
            String databaseName = resultSet.getString(1);

            if (databaseName.equals("cab302")) {
                System.out.println(databaseName + " exists!");
                databaseExists = true;
            }
        }

        // check for database existing and requiring to create the database
        if (!databaseExists) {
            System.out.println("Creating DB: cab302...");
            createDB();
        }
    }

    // Create the database and tables if they don't exist
    public static void createDB() throws SQLException {
        Statement stmt = ServerInit.conn.createStatement();

        try {
            File myObj = new File("src/server/resources/cab302_db_queries.sql");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                stmt.execute(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
            e.printStackTrace();
        }
    }
}
