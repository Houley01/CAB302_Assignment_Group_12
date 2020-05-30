package server.initialisation;
import java.io.IOException;
import java.io.InputStream;
// represents the connection to the database.
import java.sql.Connection;
// obtains the connection to the database.
import java.sql.DriverManager;
// handles SQL errors between the Java application and the database.
import java.sql.SQLException;
import java.util.Properties;

// Class initiates connection to the database with a db.props file
public class ServerInit {
    public static Connection conn;
    private static String url;
    private static String username;
    private static String password;

    /**
     * Sets the database connection via props file.
     * Reads url and places itself as a root user.
     * Note: No connection is made in this method,
     * it's just initialization.
     *
     * @see initaliseConnection
     * @throws IOException
     */
    public static void setupDatabaseConnectionInfo() throws IOException {
        // Attempt to open file 'db.props' and retrieve DB connection information
        try {
            InputStream in = ServerInit.class.getResourceAsStream("../../resources/db.props");

            Properties prop = new Properties();
            prop.load(in);

            url = prop.getProperty("jdbc.url");
            username = prop.getProperty("jdbc.username");
            password = prop.getProperty("jdbc.password");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Uses the information from setupDatabaseConnectionInfo
     * and connects to the database.
     *
     * @return bool         Return true if connection was successful.
     * @throws SQLException
     */
    public static Boolean initaliseConnection() throws SQLException {
        // Attempt to make the connection to the DB
        try {
            setupDatabaseConnectionInfo();

            conn = DriverManager.getConnection(url, username, password);

            System.out.println("Connection Successful!");

            return true;
        } catch (SQLException | IOException e) {
            throw new Error("Connection unsuccessful", e);
        }
    }
}
