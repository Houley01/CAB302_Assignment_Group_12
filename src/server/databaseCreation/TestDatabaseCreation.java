package server.databaseCreation;

import org.junit.jupiter.api.Test;
import server.initialisation.ServerInit;

import java.sql.SQLException;

public class TestDatabaseCreation {

    @Test
    public void testDatabaseExists() throws SQLException {
        ServerInit.initaliseConnection();
        databaseCreation.checkDatabaseExistence();
    }
}
