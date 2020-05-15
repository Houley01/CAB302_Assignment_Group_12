package server;

import server.databaseCreation.databaseCreation;
import server.initialisation.ServerInit;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Date;

public class Server {
    private static Boolean connectionInitiated;
    private static HashMap<String, String[]> usersAuthenticated = new HashMap<String, String[]>();

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
                // Auth token after successful login
                if (request.equals("AuthToken")) {
                    String uname = receiver.readUTF();
                    // Generate an authentication token for the user
                    send.writeObject(generateAuthToken(uname));
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

    // Returns a hash of the entered password for a new user
    private static String plaintextToHashedPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String hashedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            //Add password bytes to digest
            md.update(password.getBytes());

            //Get the hash's bytes
            byte[] bytes = md.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            hashedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        String hashedPasswordSalted = getSaltedHash(hashedPassword);

        return hashedPasswordSalted;
    }

    // Creates a salted hash of the hashed password entered from the user that can then be used to store in the database for the user
    public static String getSaltedHash(String password) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
        // store the salt with the password
        System.out.println(Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt));

        return Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
    }

    //Checks the hashed password entered by the user corresponds to a stored salted hash of the password
    public static boolean check(String password, String stored) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        String[] saltAndHash = stored.split("\\$");
        if (saltAndHash.length != 2) {
            throw new IllegalStateException(
                    "The stored password must have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.getDecoder().decode(saltAndHash[0]));
        return hashOfInput.equals(saltAndHash[1]);
    }

    // Returns a hash to check for hash comparison of the users entered password and the one stored in the database
    private static String hash(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, 20*1000, 256));
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private static String generateAuthToken(String username) {
        // Below creates the auth token for the user
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);

        // Current time in milliseconds
        long currentTime = System.currentTimeMillis();
        // Expiry time of auth token (24 hours)
        long authTokenExpiryTime = 86400000;
        // Expiry time of session token
        long expiryDate = currentTime + authTokenExpiryTime;
        // Get expiry value to string to store in hashmap
        String expiry = String.valueOf(expiryDate);

        Date dateOfExpiry = new Date(expiryDate);

        System.out.println("Username: " + username);
        System.out.println("AuthToken: " + token);
        System.out.println("Expiry: " + dateOfExpiry);

        usersAuthenticated.put(username,new String[] {token, expiry});

        return token;
    }

    // Check if the authentication token is still valid and hasn't expired or doesn't match up with what the server has stored for the user
    // This check is to be done when the user does an action that involves the server
    private static boolean checkTokenIsValid(String username, String token) {
        // Check that a user has been given an auth token
        if(usersAuthenticated.containsKey(username)) {

            // Contains the auth token and expiry date for the user
            ArrayList<String> userValues = new ArrayList<String>();
            // Store the auth token and expiry date for the user
            for(String s:usersAuthenticated.get(username)){
                userValues.add(s);
            }

            // Check that the token given to the server matches the token stored in the server
            if (token.equals(userValues.get(0))) {
                // Current time in milliseconds
                long currentTime = System.currentTimeMillis();
                // Expiry time for user
                long expiry = Long.parseLong(userValues.get(1));

                // Check that the token hasn't expired yet and is still valid
                if (expiry > currentTime) {
                    System.out.println("Not yet expired");
                    return true;
                }
                else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
