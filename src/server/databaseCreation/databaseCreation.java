package server.databaseCreation;

import server.initialisation.ServerInit;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        String[] dbCreationQueries = {"CREATE DATABASE IF NOT EXISTS cab302", "USE cab302", "CREATE TABLE IF NOT EXISTS `billboards` (\n" +
                "  `idbillboards` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`idbillboards`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1", "CREATE TABLE IF NOT EXISTS `schedules` (\n" +
                "  `idschedules` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`idschedules`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1", "CREATE TABLE IF NOT EXISTS `users` (\n" +
                "  `idusers` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`idusers`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;"};

        Statement stmt = ServerInit.conn.createStatement();
        ResultSet rs = null;

        for (String query : dbCreationQueries) {
            rs = stmt.executeQuery(query);
            rs.next();
        }
    }
}
