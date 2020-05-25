package server;

import resources.Billboard;
import resources.CustomXMFile;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Date;

public class Server {
    private static Boolean connectionInitiated;
    static HashMap<String, String[]> usersAuthenticated = new HashMap<String, String[]>();

    public static void main(String[] args) throws IOException, SQLException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException, ParseException {

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
                if (request.equals("CreateBillboard")) {
//                    String[] billboardData = (String[]) receiver.readObject();
//                    CreateBillboard(billboardData);
                    CreateBillboard(receiver, send);
                }
                // Schedule Billboards
                if (request.equals("ScheduleBillboards")) {

                }

                if (request.equals("RequestScheduleBillboards")) {
                    System.out.println("Requested schedule billboards");
                    String username = receiver.readUTF();
                    String token = receiver.readUTF();
                    send.writeObject(RequestScheduling(username, token));
                    send.flush();
                }

                // List Billboards
                if (request.equals("ListBillboards")) {

                }

                // List Billboards
                if (request.equals("EditUser")) {
                    System.out.println("EDIT USER");
//                    for admin use
                    String check = receiver.readUTF();
                    if (check == "findUsername") {
                        System.out.println("findUsername");

//                        send.writeObject(getAllUsernames());
//                        send.flush();
                    }
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
    static String plaintextToHashedPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
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

        return hashedPassword;
    }

    // After a password has been hashed, a salt can be added to it before being stored to the server
    public static String addSaltToHashedPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String hashedPasswordSalted = getSaltedHash(password);

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

    static String generateAuthToken(String username) {
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
    static boolean checkTokenIsValid(String username, String token) {
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

    //    NOTE:: STILL WORKING ON THIS
    private static Object getAllUsernames() throws SQLException {
        Object data = new Object();
        String query = "SELECT `user` FROM `users`";

        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);
        // If no user are found in database
        if (!rs.isBeforeFirst()) {
            System.out.println("No user in database"); // Debug use
//            return "No User in database";
        } else {
            int i = 1;
            while (rs.next())
                System.out.println(rs.getString(i));
            data += rs.getString(i) + ",";
            i++;

        }
        return data;
    }

    private static int GetUserID(String username) throws SQLException {

        String query = "select idUsers FROM users where user = '" + username +"';";

        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);
        // If no user are found in database
        if (!rs.isBeforeFirst()) {
            System.out.println("No user in database"); // Debug use
//            return "No User in database";
        } else {
            int i = 1;
            while (rs.next())
                return rs.getInt(i);
        }
        return -1;
    }

//    static boolean CreateBillboard(String[] billboardData) throws ParseException {
//
//        // If the billboard information was sent correctly, data will be parsed
//        if (billboardData[0] != "" && billboardData[4] != "") {
//
//            String title = billboardData[0];
//            int userId = Integer.parseInt(billboardData[1]);
//            Date currentTime = new Date();
//            Date timeModified = new Date();
//            String fileLocation = billboardData[4];
//
//            // Use this for edit billboard
//            // Date timeModified = new SimpleDateFormat("dd/MM/yyyy").parse(billboardData[3]);
//
//            // Add code to be storing the billboard information to the database
//
//            return true;
//        }
//
//        // Returns false as no required data was sent
//        return false;
//    }


    private static boolean CreateBillboard(ObjectInputStream receiver, ObjectOutputStream send) throws IOException, ClassNotFoundException, SQLException {
        String username = receiver.readUTF();
        String token = receiver.readUTF();
        int userId = GetUserID(username);
        if (checkTokenIsValid(username, token) && userId != -1) {
            System.out.println("true");
            send.write(1);
            send.flush();

            Billboard billboard = (Billboard) receiver.readObject();

            billboard.PrintBillboardInformation();
            String fileLocation = CustomXMFile.CreateFileContents(billboard);
            System.out.println(fileLocation);

//            Check if the file already exists in database
            String query = "SELECT idBillboards FROM billboards WHERE billboardName = '"+ billboard.getTitle() +"';";
            Statement st = ServerInit.conn.createStatement();
            st.executeQuery("USE `cab302`;");
            ResultSet rs = st.executeQuery(query);
//            If the billboard name is not found in the database then insert into database, else UPDATE data in database
            if (!rs.isBeforeFirst()) {
                query = "INSERT INTO `billboards` (`idBillboards`, `billboardName`, `userId`, `dateMade`, `dateModify`, `fileLocation`)" +
                        "VALUES (NULL, '" + billboard.getTitle()+  "', "+ userId + ", NULL, NULL, '" + fileLocation +"')";
                st.executeQuery(query);

            } else {
                rs.next();
                System.out.println(rs.getInt("idBillboards"));
                query = "UPDATE `billboards` SET `userId` = " + userId + ", `dateModify` = current_timestamp() WHERE `idBillboards` = "+ rs.getInt("idBillboards") + "";
                st.execute(query);
            }
            send.write(1);
            send.flush();
            return true;
        } else {
            send.writeInt(0);
            send.flush();
            System.out.println("Don't have access to create billboard");
            return false;
        }
    }


    // Gets all the current scheduled billboards
    private static ArrayList<String[]> RequestScheduling(String username, String token) throws SQLException {

        // Before request, check that the user's token is valid
        if (checkTokenIsValid(username, token)) {
            System.out.println("Token is valid. Begin requesting currently scheduled billboards...");

            String query = "SELECT idSchedules, weekday, duration, startTime, userId FROM schedules";
            Statement st = ServerInit.conn.createStatement();
            st.executeQuery("USE `cab302`;");
            ResultSet rs = st.executeQuery(query);

            // checks if there is any data in the database
            if (!rs.isBeforeFirst()) {
                System.out.println("There is currently no scheduled billboards"); // Debug use
//            return "No User in database";
            } else {
                // Contains the schedules in the database
                ArrayList<String[]> schedules = new ArrayList<>();

                System.out.println("Retrieving list of Schedules from database...");

                while (rs.next()) {
                    String id = rs.getString("idSchedules");
                    String weekday = rs.getString("weekday");
                    String duration = rs.getString("duration");
                    String startTime = rs.getString("startTime");
                    String userId = rs.getString("userId");

                    String[] schedule = {id, weekday, duration, startTime, userId};
                    schedules.add(schedule);
                }
                System.out.println("Successfully retrieved list of scheduled billboards");
                System.out.println("Sending list of Schedules to controller...");

                // Send the schedules to the control panel
                return schedules;
            }
        }
        return null;
    }

}
