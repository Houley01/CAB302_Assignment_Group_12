package server;

import server.databaseCreation.databaseCreation;
import server.initialisation.ServerInit;

import java.io.IOException;
import java.sql.SQLException;

public class Server {
    private static Boolean connectionInitiated;

    public static void main(String[] args) throws IOException, SQLException {
        connectionInitiated = ServerInit.initaliseConnection();

        if (connectionInitiated) {
            databaseCreation.checkDatabaseExistence();
        }
    }
}
