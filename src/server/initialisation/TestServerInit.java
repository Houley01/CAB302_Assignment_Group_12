package server.initialisation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestServerInit {

    @Test
    public void testDBPropsCredentials() throws IOException {
        ServerInit.setupDatabaseConnectionInfo();
    }

    @Test
    public void testDatabaseConnection() throws IOException, SQLException {
            ServerInit.initaliseConnection();
    }
}
