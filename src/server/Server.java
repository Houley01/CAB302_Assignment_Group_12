package server;

import server.databaseCreation.databaseCreation;
import server.initialisation.ServerInit;

import java.sql.SQLException;

public class Server {
    public static void main(String[] args) throws SQLException {
        ServerInit.initaliseConnection();

        databaseCreation.checkDB();
    }
}
