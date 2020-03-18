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

    public static void initaliseConnection() {
        String url = "";
        String username = "";
        String password = "";

        conn = null;

        // Attempt to open file 'db.props' and retrieve DB connection information
        try {
            InputStream in = ServerInit.class.getResourceAsStream("db.props");

            Properties prop = new Properties();
            prop.load(in);

            url = prop.getProperty("jdbc.url");
            username = prop.getProperty("jdbc.username");
            password = prop.getProperty("jdbc.password");
        }catch (IOException e) {
            e.printStackTrace();
        }
        // Attempt to make the connection to the DB
        try {
            conn = DriverManager.getConnection(url, username, password);

            System.out.println("Connection Successful!");

        } catch (SQLException e) {
            throw new Error("Connection unsuccessful", e);
        }
    }
}
