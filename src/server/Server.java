package server;

import server.databaseCreation.databaseCreation;
import server.initialisation.ServerInit;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Base64;

public class Server {
    private static Boolean connectionInitiated;

    public static void main(String[] args) throws IOException, SQLException, InvalidKeySpecException, NoSuchAlgorithmException {

        // Gathers the information from server.config file
        resources.GetPropertyValues properties = new resources.GetPropertyValues();
        properties.readPropertyFile();

        connectionInitiated = ServerInit.initaliseConnection();

        if (connectionInitiated) {
            databaseCreation.checkDatabaseExistence();

            // Reads the port number from the server.properties file
            ServerSocket serverSocket =  new ServerSocket(properties.port);

            while(true) {
                Socket server = serverSocket.accept();
                System.out.println("Connected to " + server.getInetAddress());

                ObjectInputStream receiver = new ObjectInputStream(server.getInputStream());
                ObjectOutputStream send = new ObjectOutputStream(server.getOutputStream());


                String request = receiver.readUTF();

                // Username and password
                if (request.equals("Login")) {
                    String uname = receiver.readUTF();
                    String pass = receiver.readUTF();
                    // func check username and database
                    send.writeBoolean(checkUserDetails(uname, pass));
                    send.flush();
                }
                // Create Billboard Information
                if (request.equals( "CreateBillboards")) {

                }
                // Schedule Billboards
                if (request.equals("ScheduleBillboards")) {

                }
                // List Billboards
                if (request.equals("ListBillboards")) {

                }
                // List Billboards
                if (request.equals("EditUser")) {

                }
                // Viewer Request Billboards
                if (request.equals("ViewerRequest")) {

                }


                // End connections
                receiver.close();
                send.close();
                server.close();
            }
        }
    }

    static boolean checkUserDetails(String uName, String pass) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String query = "SELECT * FROM `users` WHERE `user` = \""+ uName + "\";";

        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);
        // If no user name is found
        if (!rs.isBeforeFirst()) {
            System.out.println("No MATCH");
            return false;
        } else {
            rs.next();

            // Get the salt+hash for the user from the database
           String storedPass = rs.getString("pass");

           // Compare the entered password by the user with the salt+hash for the user and check if it matches
            if (check(pass, storedPass)) {
                return true;
            }
            else return false;
        }
//          DEBUGGING USE \/
//            while (rs.next()) {
//                int id = rs.getInt("idUsers");
//                String username = rs.getString("user");
//                String password = rs.getString("pass");
//                int idPermissions = rs.getInt("idPermissions");
//                String firstName = rs.getString("fName");
//                String lastName = rs.getString("lName");
//
//                // print the results
//                System.out.format("%s, %s, %s, %s, %s, %s\n",
//                        id, username, password, idPermissions, firstName, lastName);
//            }
//              DEBUGGING USE /\
    }

    /** Computes a salted PBKDF2 hash of given plaintext password
     suitable for storing in a database.*/
    public static String getSaltedHash(String password) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
        // store the salt with the password
        System.out.println(Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt));

        return Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
    }

    /** Checks whether given plaintext password corresponds
     to a stored salted hash of the password. */
    public static boolean check(String password, String stored) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        String[] saltAndHash = stored.split("\\$");
        if (saltAndHash.length != 2) {
            throw new IllegalStateException(
                    "The stored password must have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.getDecoder().decode(saltAndHash[0]));
        return hashOfInput.equals(saltAndHash[1]);
    }

    private static String hash(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, 20*1000, 256));
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
