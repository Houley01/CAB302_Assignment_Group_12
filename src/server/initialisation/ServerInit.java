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
    public static void initaliseConnection() {
        String url = "";
        String username = "";
        String password = "";

        Connection conn = null;

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
            Connection con= DriverManager.getConnection(url, username, password);

            System.out.println("Connection Successful!");

        } catch (SQLException e) {
            throw new Error("Connection unsuccessful", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
