package server;

import org.xml.sax.SAXException;
import resources.Billboard;
import resources.CustomXMFile;
import server.databaseCreation.databaseCreation;
import server.initialisation.ServerInit;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

import static resources.CustomXMFile.ReadXMLFile;

public class Server {
    /**
     * Reads database connection properties and connects to database as a root user.
     * The method returns a bool. True if connected, false if it cannot connect.
     */
    private static Boolean connectionInitiated;
    /**
     * Used for authentication tokens. Example of a token would be
     * <code>{username, {token, expiry}}</code>
     */
    static HashMap<String, String[]> usersAuthenticated = new HashMap<String, String[]>();

    /**
     *  Initiates logic for connecting to database and server properties.
     *  If the server can connect to the database it will open a socket
     *  on the port from server.properties <code>default: 8888</code>
     *  and run through a while loop. This while loop is an if tree
     *  for most user interactions with the billboard system and calls
     *  methods / classes accordingly.
     *  <ul>
     *      <li>User login</li>
     *      <li>User authentication</li>
     *      <li><h3>Billboard Controller</h3></li>
     *      <li>Billboard creation</li>
     *      <li>Billboard Schedule</li>
     *      <li>Billboard Listing</li>
     *      <li>User modification</li>
     *      <li><h3>Billboard viewer</h3></li>
     *      <li>Viewer request billboard</li>
     *  </ul>
     *
     * @param args                      arguments supplied via cmdlet
     * @throws IOException
     * @throws SQLException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws ClassNotFoundException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, SQLException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException, ParseException {

        // Gathers the information from server.config file
        resources.GetPropertyValues properties = new resources.GetPropertyValues();
        properties.readPropertyFile();

        connectionInitiated = ServerInit.initaliseConnection();

        if (connectionInitiated) {
            databaseCreation.checkDatabaseExistence();

            // Reads the port number from the server.properties file
            ServerSocket serverSocket = new ServerSocket(properties.port);

            while (true) {
                Socket server = serverSocket.accept();
//                System.out.println("Connected to " + server.getInetAddress());

                ObjectInputStream receiver = new ObjectInputStream(server.getInputStream());
                ObjectOutputStream send = new ObjectOutputStream(server.getOutputStream());

                String request = receiver.readUTF();

                // Username and password
                if (request.equals("Login")) {
                    String uname = receiver.readUTF();
                    String pass = receiver.readUTF();
                    // func check username and database
                    boolean chekuser = CheckUserDetails(uname, pass);
                    if (chekuser == true) {
                        send.writeBoolean(true);
                        send.writeObject(UserPermission(uname));

                    }
                    send.flush();
                }
                // Auth token after successful login
                if (request.equals("AuthToken")) {
                    String uname = receiver.readUTF();
                    // Generate an authentication token for the user
                    send.writeObject(GenerateAuthToken(uname));
                    send.flush();
                }
                // Create Billboard Information
                if (request.equals("CreateBillboard")) {
                    CreateBillboard(receiver, send);
                }

// NOTE:: BEN IS WORKING HERE
                // Schedule Billboards
                if (request.equals("ScheduleBillboards")) {

                }

                if (request.equals("RequestScheduleBillboards")) {
//                    System.out.println("Requested schedule billboards");
//                    String username = receiver.readUTF();
//                    String token = receiver.readUTF();
//                    System.out.println("Accessing data");
                    send.writeObject(RequestScheduling());
//                    System.out.println("Sending data");
                    send.flush();
                }

                if(request.equals("CreateSchedule"))
                {
//                    System.out.println("Creating new schedule ...");
                    CreateNewSchedule(receiver, send);
                }
// NOTE:: BEN IS WORKING HERE

                // List Billboards
                if (request.equals("ListBillboards")) {
                    RequestBillboardList(receiver, send);
                }
//                Find the selected billboard
                if (request.equals("GetBillboard")) {
                    try {
                        GetBillboardInfo(receiver, send);
                    } catch (ParserConfigurationException | SAXException e) {
                        e.printStackTrace();
                    }
                }
                if (request.equals("DeleteBillboard")) {
                    DeleteBillboard(receiver, send);
                }

                // Changes user's password
                if (request.equals("changePassword")) {
//                    System.out.println("request: Password...");
                    String username = receiver.readUTF();
                    String password = receiver.readUTF();
                    String loggedInUser = receiver.readUTF();
                    String token = receiver.readUTF();

                    if (CheckTokenIsValid(loggedInUser, token)) {
//                        System.out.println("Changing Password...");
                        ChangeUserPassword(username, password);
//                        System.out.println("Changed Password...");
                    }
                    //else {
//                        System.out.println("Token wasn't valid");
                    //}
                    send.flush();
                }

                // Viewer Request Billboards
                if (request.equals("ViewerRequest")) {
                    ViewerRequest(send);
                }

                // Get a list of users in the database
                if (request.equals("getUsers")) {
                    send.writeObject(GetAllUsernames());
                    send.flush();
                }

                // Retrieve information on a user for editing their details
                if (request.equals("getUserInfo")) {
                    String username = receiver.readUTF();
                    String loggedInUser = receiver.readUTF();
                    String token = receiver.readUTF();

                    if (CheckTokenIsValid(loggedInUser, token)) {
                        send.writeObject(GetUserInfo(username));
                    }
//                    else {
//                        System.out.println("Token wasn't valid");
//                    }
                    send.flush();
                }

                // Update information on a user for editing their details
                if (request.equals("updateUserInfo")) {
//                    System.out.println("request: update user details...");
                    String username = receiver.readUTF();
                    String firstName = receiver.readUTF();
                    String lastName = receiver.readUTF();
                    String loggedInUser = receiver.readUTF();
                    String token = receiver.readUTF();

                    if (CheckTokenIsValid(loggedInUser, token)) {
//                        System.out.println("Updating user info...");
                        UpdateUserInfo(username, firstName, lastName);
//                        System.out.println("Updated user info");
                    }
//                    else {
//                        System.out.println("Token wasn't valid");
//                    }
                    send.flush();
                }

                // Delete a user
                if (request.equals("deleteUser")) {
//                    System.out.println("request: delete user details...");
                    String username = receiver.readUTF();
                    String loggedInUser = receiver.readUTF();
                    String token = receiver.readUTF();

                    if (CheckTokenIsValid(loggedInUser, token)) {
//                        System.out.println("Deleting user'" + username + "'...");
                        DeleteUser(username);
//                        System.out.println("Deleted user");
                    }
//                    else {
//                        System.out.println("Token wasn't valid");
//                    }
                    send.flush();
                }

                // Get a user's permissions
                if (request.equals("getUserPermissions")) {
//                    System.out.println("request: get user permissions...");
                    String username = receiver.readUTF();
                    String loggedInUser = receiver.readUTF();
                    String token = receiver.readUTF();

                    if (CheckTokenIsValid(loggedInUser, token)) {
//                        System.out.println("Retrieving user permissions...");
                        boolean[] userPermissions = GetUserPermissions(username);
                        send.writeObject(userPermissions);
                    } else {
//                        System.out.println("Token wasn't valid");
                    }
                    send.flush();
                }

                // Update a user's permissions
                if (request.equals("updatePermissions")) {
//                    System.out.println("request: update user permissions...");
//                    boolean createBillboardPermission = receiver.readBoolean();
//                    boolean editBillboardPermission = receiver.readBoolean();
//                    boolean scheduleBillboardPermission = receiver.readBoolean();
//                    boolean editUserPermission = receiver.readBoolean();
                    boolean[] permissions = (boolean[]) receiver.readObject();
                    String username = receiver.readUTF();
                    String loggedInUser = receiver.readUTF();
                    String token = receiver.readUTF();

                    if (CheckTokenIsValid(loggedInUser, token)) {
//                        System.out.println("Updating user permissions...");
                        UpdateUserPermissions(permissions, username);
//                        System.out.println("Updated user permissions");
                    } else {
//                        System.out.println("Token wasn't valid");
                    }
                    send.flush();
                }

                // Create a new user
                if (request.equals("createNewUser")) {
//                    System.out.println("request: create new user...");
                    String username = receiver.readUTF();
                    String firstName = receiver.readUTF();
                    String lastName = receiver.readUTF();
                    String password = receiver.readUTF();
                    boolean createBillboardPermission = receiver.readBoolean();
                    boolean editBillboardPermission = receiver.readBoolean();
                    boolean scheduleBillboardPermission = receiver.readBoolean();
                    boolean editUserPermission = receiver.readBoolean();
                    String loggedInUser = receiver.readUTF();
                    String token = receiver.readUTF();

                    String[] userData = {username, firstName, lastName, password};
                    boolean[] permissions = {createBillboardPermission, editBillboardPermission, scheduleBillboardPermission, editUserPermission};

                    if (CheckTokenIsValid(loggedInUser, token)) {
//                        System.out.println("Creating new user...");
                        CreateNewUser(userData, permissions);
//                        System.out.println("Created new user");
                    } else {
//                        System.out.println("Token wasn't valid");
                    }
                    send.flush();
                }

                if (request.equals("Logout")) {
                    String username = receiver.readUTF(); // Get username from client
                    Logout(username);
                    send.flush();
                }


                // End connections
                receiver.close();
                send.close();
                server.close();
            }
        }
    }

    /**
     *  Checks the database to see if a user matches the username given and
     *  if the password in the database matches one the user has given.
     *  The method returns a bool if the user exits in the database or not.
     *
     * @param uName                     User name to get details for.
     * @param pass                      Password to check against other passwords in the system.
     * @return bool                     True: User is found
     * @return bool                     False: User not found
     *
     * @throws SQLException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    static boolean CheckUserDetails(String uName, String pass) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String query = "SELECT * FROM `users` WHERE `user` = \"" + uName + "\";";

        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);
        // If no user name is found
        if (!rs.isBeforeFirst()) {
//            System.out.println("No MATCH");
            return false;
        } else {
            rs.next();

            // Get the salt+hash for the user from the database
            String storedPass = rs.getString("pass");

            // Compare the entered password by the user with the salt+hash for the user and check if it matches
            if (Check(pass, storedPass)) {
                return true;
            } else return false;
        }
    }

    // Returns a hash of the entered password for a new user

    /**
     * Hashes the password via MD5 encryption and using MessageDigest
     * algorithm
     *
     *
     * @param password                  Password to be hashed
     * @return hashed password
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    static String PlaintextToHashedPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
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
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashedPassword;
    }

    // After a password has been hashed, a salt can be added to it before being stored to the server

    /**
     * Adds salt to a hashed password for storage in database.
     *
     * @param password                  Password to be salted
     * @return salted and hashed password
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String AddSaltToHashedPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String hashedPasswordSalted = GetSaltedHash(password);
        return hashedPasswordSalted;
    }

    // Creates a salted hash of the hashed password entered from the user that can then be used to store in the database for the user

    /**
     * Creates a salted hashed of the password entered that can be stored in the DB.
     * Salted part of the hash is encoded in base64.
     *
     * @param password                      Password to be salted and hashed.
     * @return salted and hashed password
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static String GetSaltedHash(String password) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
        // store the salt with the password
//        System.out.println(Base64.getEncoder().encodeToString(salt) + "$" + Hash(password, salt));

        return Base64.getEncoder().encodeToString(salt) + "$" + Hash(password, salt);
    }

    //Checks the hashed password entered by the user corresponds to a stored salted hash of the password

    /**
     * Checks toi see if the password and stored password match. Firstly checks if the stored
     * password has a valid salt and hash. (It does this by splitting the stored password into
     * an array via the $ separating the salt and the hash). Then encodes the supplied password
     * from the user to see if it matches the hash of the stored password.
     *
     * @param password      Password entered by user.
     * @param stored        Stored password from database.
     * @return hashed password
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws IllegalStateException        Incorrect format for hashed and salted passwords.
     */
    public static boolean Check(String password, String stored) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        String[] saltAndHash = stored.split("\\$");
        if (saltAndHash.length != 2) {
            throw new IllegalStateException(
                    "The stored password must have the form 'salt$hash'");
        }
        String hashOfInput = Hash(password, Base64.getDecoder().decode(saltAndHash[0]));
        return hashOfInput.equals(saltAndHash[1]);
    }

    // Returns a hash to check for hash comparison of the users entered password and the one stored in the database

    /**
     * Hashes the user entered password with SecretKeyFactory (Bi-directional cryptographic opaque key
     * (encryption and decryption method)) and salts the user defined password with the salted password
     * from the server.
     *
     * @param password      Password user entered
     * @param salt          Salt from stored password
     * @return hashed password
     * @exception InvalidKeySpecException     If the character key is invalid character for UTF-8
     * @exception NoSuchAlgorithmException    If cryptographic algorithm is not available.
     * @throws IllegalArgumentException     Password input was empty or null.
     */
    private static String Hash(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, 20 * 1000, 256));
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Generates a token using a random number generator and shifting through a byte array
     * X amount of times and encoding the byte array into base64 as the user token. Then
     * we get the servers current Unix timestamp and set the expiry for authToken to be
     * 24 hours from time of generation.
     *
     * @param username      User to generate a token for
     * @return token
     */
    static String GenerateAuthToken(String username) {
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

        usersAuthenticated.put(username, new String[]{token, expiry});

        return token;
    }

    /**
     * Authentication checker. Checks to see if a token is still valid, hasn't expired or doesn't match the stored
     * token from the server. This check is to be done when a user does any action that involves the server.
     *
     * @param username      Username (Used as an ID).
     * @param token         Token user is currently using.
     * @return true         If the token is still valid.
     * @return false        If the token is not valid.
     */
    static boolean CheckTokenIsValid(String username, String token) {
        // Check that a user has been given an auth token
        if (usersAuthenticated.containsKey(username)) {

            // Contains the auth token and expiry date for the user
            ArrayList<String> userValues = new ArrayList<String>();
            // Store the auth token and expiry date for the user
            for (String s : usersAuthenticated.get(username)) {
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
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Queries the database for all users from the users table.
     *
     * Note: Still working on this
     *
     * @return All users in object
     * @throws SQLException
     */
    private static ArrayList<String> GetAllUsernames() throws SQLException {
//        System.out.println("Getting usernames...");
        ArrayList<String> listOfUsers = new ArrayList<>();
        String query = "SELECT user FROM users";

        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE cab302;");
        ResultSet rs = st.executeQuery(query);
        // If no user are found in database
        if (!rs.isBeforeFirst()) {
//            System.out.println("No user in database"); // Debug use
//            return "No User in database";
        } else {
            while (rs.next()) {
                String user = rs.getString("user");
                listOfUsers.add(user);
            }
        }

//        System.out.println(listOfUsers);

        return listOfUsers;
    }

    private static String[] GetUserInfo(String username) throws SQLException {
//        System.out.println("Getting user info...");
        String[] userInfo = new String[2];
        String query = "SELECT fName, lName FROM users WHERE user = '" + username + "'";

        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE cab302;");
        ResultSet rs = st.executeQuery(query);
        // If no user are found in database
        if (!rs.isBeforeFirst()) {
//            System.out.println("No user in database"); // Debug use
//            return "No User in database";
        } else {
            while (rs.next()) {
                String firstName = rs.getString("fName");
                String lastName = rs.getString("lName");
                userInfo[0] = firstName;
                userInfo[1] = lastName;
            }
        }

//        System.out.println(userInfo[0]);
//        System.out.println(userInfo[1]);

        return userInfo;
    }

    private static void UpdateUserInfo(String username, String firstName, String lastName) throws SQLException {
//        System.out.println("Updating user info...");

        String query = "UPDATE users SET fName = '" + firstName + "', lName = '" + lastName + "' WHERE user = '" + username + "'";
//        System.out.println(query);
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        st.executeQuery(query);
    }

    private static void DeleteUser(String username) throws SQLException {

        String query = "DELETE FROM users WHERE user = '" + username + "'";
//        System.out.println(query);
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        st.executeQuery(query);
    }

    private static void ChangeUserPassword(String username, String password)
            throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, SQLException {
        String saltHashedPassword = AddSaltToHashedPassword(password);

        String query = "UPDATE users SET pass = '" + saltHashedPassword + "' WHERE user = '" + username + "'";
//        System.out.println(query);
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        st.executeQuery(query);
    }

    private static boolean[] GetUserPermissions(String username) throws SQLException {

        boolean[] userPermissions = new boolean[4];

        String query = "SELECT createBillboard, editAllBillboard, scheduleBillboard, editUser FROM users WHERE user = '" + username + "'";
//        System.out.println(query);
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);

        if (!rs.isBeforeFirst()) {
//            System.out.println("No user in database"); // Debug use
//            return "No User in database";
        } else {
            while (rs.next()) {
                int createBillboard = rs.getInt("createBillboard");
                int editAllBillboard = rs.getInt("editAllBillboard");
                int scheduleBillboard = rs.getInt("scheduleBillboard");
                int editUser = rs.getInt("editUser");


                userPermissions[0] = createBillboard == 1 ? true : false;
                userPermissions[1] = editAllBillboard == 1 ? true : false;
                userPermissions[2] = scheduleBillboard == 1 ? true : false;
                userPermissions[3] = editUser == 1 ? true : false;
            }
        }

//        for (boolean b : userPermissions) {
//            System.out.println(b);
//        }

        return userPermissions;
    }

    private static void UpdateUserPermissions(boolean[] permissions, String username) throws SQLException {

//        for (boolean b : permissions) {
//            System.out.println(b);
//        }

        int[] updatedPermissions = new int[4];

        updatedPermissions[0] = permissions[0] ? 1 : 0;
        updatedPermissions[1] = permissions[1] ? 1 : 0;
        updatedPermissions[2] = permissions[2] ? 1 : 0;
        updatedPermissions[3] = permissions[3] ? 1 : 0;

        String query = "UPDATE users SET createBillboard = '" + updatedPermissions[0] + "', editAllBillboard = '" + updatedPermissions[1] + "'" +
                ", scheduleBillboard = '" + updatedPermissions[2] + "', editUser = '" + updatedPermissions[3] + "' WHERE user = '" + username + "'";
        System.out.println(query);
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        st.executeQuery(query);
    }

    private static void CreateNewUser(String[] userData, boolean[] permissions) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {

        String saltHashedPassword = AddSaltToHashedPassword(userData[3]);

        for (boolean b : permissions) {
            System.out.println(b);
        }

        int[] updatedPermissions = new int[4];

        updatedPermissions[0] = permissions[0] ? 1 : 0;
        updatedPermissions[1] = permissions[1] ? 1 : 0;
        updatedPermissions[2] = permissions[2] ? 1 : 0;
        updatedPermissions[3] = permissions[3] ? 1 : 0;

        String query = "INSERT INTO users (user, pass, fName, lName, createBillboard, editAllBillboard, scheduleBillboard, editUser) VALUES ('" + userData[0] + "', '" + saltHashedPassword + "', " +
                "'" + userData[1] + "', '" + userData[2] + "', '" + updatedPermissions[0] + "', '" + updatedPermissions[1] + "', '" + updatedPermissions[2] + "', '" + updatedPermissions[3] + "');";
        System.out.println(query);
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        st.executeQuery(query);
    }

    /**
     * Queries for the user ID from the database via their username.
     *
     * @param username      Username
     * @return int          UserID.
     * @return int          -1 if no userID was found.
     * @throws SQLException
     */
    private static int GetUserID(String username) throws SQLException {

        String query = "select idUsers FROM users where user = '" + username + "';";

        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);
        // If no user are found in database
        if (!rs.isBeforeFirst()) {
//            System.out.println("No user in database"); // Debug use
//            return "No User in database";
        } else {
            int i = 1;
            while (rs.next())
                return rs.getInt(i);
        }
        return -1;
    }

    /**
     * Calls createBillboard class from the control panel and creates a XML file with all the
     * current billboards. If the billboard name is found in the database it updates the information
     * related to that entry to the new information entered from the user. Otherwise it creates a new
     * billboard entry.
     *
     * @param receiver      Stream receiver
     * @param send          Stream sender
     * @return true         Billboard was created and database was updated successfully.
     * @return false        If user does not have permissions or userID does not exist.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static boolean CreateBillboard(ObjectInputStream receiver, ObjectOutputStream send) throws IOException, ClassNotFoundException, SQLException {
        String username = receiver.readUTF();
        String token = receiver.readUTF();
        int userId = GetUserID(username);
        if (CheckTokenIsValid(username, token) && userId != -1) {
            send.write(1);
            send.flush();

            Billboard billboard = (Billboard) receiver.readObject();
            billboard.PrintBillboardInformation();
            String fileLocation = CustomXMFile.CreateFileContents(billboard);
//            Check if the file already exists in database
            String query = "SELECT idBillboards FROM billboards WHERE billboardName = '" + billboard.getTitle() + "';";
            Statement st = ServerInit.conn.createStatement();
            st.executeQuery("USE `cab302`;");
            ResultSet rs = st.executeQuery(query);
//            If the billboard name is not found in the database then insert into database, else UPDATE data in database
            if (!rs.isBeforeFirst()) {
                query = "INSERT INTO `billboards` (`idBillboards`, `billboardName`, `userId`, `dateMade`, `dateModify`, `fileLocation`)" +
                        "VALUES (NULL, '" + billboard.getTitle() + "', " + userId + ", NULL, NULL, '" + fileLocation + "')";
                st.executeQuery(query);

            } else {
                rs.next();
                query = "UPDATE `billboards` SET `userId` = " + userId + ", `dateModify` = current_timestamp() WHERE `idBillboards` = " + rs.getInt("idBillboards") + "";
                st.execute(query);
            }
            send.write(1);
            send.flush();
            return true;
        } else {
            send.writeInt(0);
            send.flush();
//            System.out.println("Don't have access to create billboard");
            return false;
        }
    }


    // Gets all the current scheduled billboards

    /**
     * Scheduling listed from the database. Code checks to see if the current token
     * is valid and retrieves the data from the database then forms it into an array list.
     *
     * @return schedules
     * @return null         if token is not valid
     * @throws SQLException
     */
    private static ArrayList<String[]> RequestScheduling() throws SQLException {
        // Before request, check that the user's token is valid
        if (true) {
//            System.out.println("Token is valid. Begin requesting currently scheduled billboards...");

            String query = "SELECT idSchedules, weekday, duration, startTime, userId FROM schedules";
            Statement st = ServerInit.conn.createStatement();
            st.executeQuery("USE `cab302`;");
            ResultSet rs = st.executeQuery(query);

            // checks if there is any data in the database
            if (!rs.isBeforeFirst()) {
//                System.out.println("There is currently no scheduled billboards"); // Debug use
//            return "No User in database";
            } else {
                // Contains the schedules in the database
                ArrayList<String[]> schedules = new ArrayList<>();

//                System.out.println("Retrieving list of Schedules from database...");

                while (rs.next()) {
                    String id = rs.getString("idSchedules");
                    String weekday = rs.getString("weekday");
                    String duration = rs.getString("duration");
                    String startTime = rs.getString("startTime");
                    String userId = rs.getString("userId");

                    String[] schedule = {id, weekday, duration, startTime, userId};
                    schedules.add(schedule);
                }
//                System.out.println("Successfully retrieved list of scheduled billboards");
//                System.out.println("Sending list of Schedules to controller...");

                // Send the schedules to the control panel
                return schedules;
            }
        }
        return null;
    }

    private static void RequestBillboardList(ObjectInputStream receiver, ObjectOutputStream send) throws IOException, ClassNotFoundException, SQLException {
        String query = "SELECT idBillboards, billboardName, users.fName, users.lName, dateMade, " +
                "dateModify, fileLocation FROM billboards LEFT JOIN users ON billboards.userId = users.idUsers " +
                "order by billboards.idBillboards;";
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);

        // checks if there is any data in the database
        if (!rs.isBeforeFirst()) {
            send.write(-1);
            send.flush();
//            System.out.println("There is no billboards to be displayed"); // Debug use
//            return "No User in database";
        } else {
            send.write(1);
            send.flush();
            // Contains the schedules in the database
            ArrayList<String[]> billboardList = new ArrayList<>();

//            System.out.println("Retrieving list of Schedules from database...");

            while (rs.next()) {
                String id = rs.getString("idBillboards");
                String name = rs.getString("billboardName");
                String creator = rs.getString("users.fName") + " " + rs.getString("users.lName");
                String dateMade = rs.getString("dateMade");
                String dateModify = rs.getString("dateModify");
                String fileLocation = rs.getString("fileLocation");

                String[] billboard = {id, name, creator, dateMade, dateModify, fileLocation};
                billboardList.add(billboard);
            }
            send.writeObject(billboardList);
            send.flush();
        }
    }

    public static void ViewerRequest(ObjectOutputStream send) throws IOException, ClassNotFoundException, SQLException
    {
        String query = "SELECT idBillboards, billboardName, users.fName, users.lName, dateMade, " +
                "dateModify, fileLocation FROM billboards LEFT JOIN users ON billboards.userId = users.idUsers " +
                "order by billboards.idBillboards;";
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);

        // checks if there is any data in the database
        if (!rs.isBeforeFirst()) {
            send.write(-1);
            send.flush();
//            System.out.println("There is no billboards to be displayed"); // Debug use
        } else {
            send.write(1);
            send.flush();
            // Contains the schedules in the database
            List<String> billboardImages = new ArrayList<>();

//            System.out.println("Retrieving list of Schedules from database...");

            while (rs.next()) {
                String fileLocation = rs.getString("fileLocation");
                billboardImages.add(fileLocation);
            }
            send.writeObject(billboardImages);
            send.flush();
        }
    }

    private static String CheckIfBillboardIsScheduled(int billboardID, boolean scheduled) {
        if (scheduled == false) {
            return  "SELECT * FROM billboards " +
                    "LEFT JOIN schedules ON schedules.idBillboard = billboards.idBillboards " +
                    "LEFT JOIN users ON billboards.userId = users.idUsers " +
                    "WHERE (schedules.idBillboard IS NULL) AND (billboards.idBillboards = " + billboardID + ")";
        } else {
            return  "SELECT * FROM billboards " +
                    "LEFT JOIN schedules ON schedules.idBillboard = billboards.idBillboards " +
                    "LEFT JOIN users ON billboards.userId = users.idUsers " +
                    "WHERE (schedules.idBillboard IS NOT NULL) AND (billboards.idBillboards = " + billboardID + ");";
        }
    }    


    private static void GetBillboardInfo(ObjectInputStream receiver, ObjectOutputStream send) throws IOException, SQLException, ParserConfigurationException, SAXException, ParserConfigurationException, SAXException {
        int billboardID = receiver.read();
        String username = receiver.readUTF();

        //        Returns if the billboard is not scheduled
        String query = CheckIfBillboardIsScheduled(billboardID, false);
        //        Returns if the billboard is currently scheduled
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);
//        If Data is found
        if (rs.isBeforeFirst()) {
            rs.next();
            if (rs.getInt("createBillboard") == 1
                            || rs.getInt("editAllBillboard") == 1) {
                send.writeInt(1);
                String name = rs.getString("billboardName");
                String fileLocation = rs.getString("fileLocation");
                Billboard billboard = ReadXMLFile(new File(fileLocation), name);
                send.writeObject(billboard);
                send.flush();
            }
            else {
                send.writeInt(-1);
            }

//            IF the billboard is scheduled in the database
        } else {
            query = CheckIfBillboardIsScheduled(billboardID, true);
            st = ServerInit.conn.createStatement();
            st.executeQuery("USE `cab302`;");
            rs = st.executeQuery(query);

            if (rs.isBeforeFirst()) {
                rs.next();
                if (rs.getInt("editAllBillboard") == 1) {
                    send.writeInt(1);
                    String name = rs.getString("billboardName");
                    String fileLocation = rs.getString("fileLocation");
                    Billboard billboard = ReadXMLFile(new File(fileLocation), name);
                    send.writeObject(billboard);
                } else {
//                    System.out.println("There is no billboards to be displayed");
                    send.writeInt(-1);
                }
                send.flush();
            }
        }
        send.flush();
    }

    private static void DeleteBillboard(ObjectInputStream receiver, ObjectOutputStream send) throws IOException, SQLException {
        int billboardID = receiver.read();

//        Returns if the billboard is not scheduled
        String query = CheckIfBillboardIsScheduled(billboardID, false);
        //        Returns if the billboard is currently scheduled
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);
//        If Data is found
        if (rs.isBeforeFirst()) {
            rs.next();
            if (rs.getInt("createBillboard") == 1 || rs.getInt("editAllBillboard") == 1) {
                send.writeInt(1);
                send.writeUTF(rs.getString("billboardName"));
                send.flush();
                if (receiver.read() == 0) {
                    query = "DELETE FROM billboards WHERE idBillboards = " + billboardID + ";";
                    st = ServerInit.conn.createStatement();
                    st.executeQuery("USE `cab302`;");
                    rs = st.executeQuery(query);
                }
            } else {
                send.writeInt(0);
            }

//            IF the billboard is scheduled in the database
        } else {
            query = CheckIfBillboardIsScheduled(billboardID, true);
            st = ServerInit.conn.createStatement();
            st.executeQuery("USE `cab302`;");
            rs = st.executeQuery(query);

            if (rs.isBeforeFirst()) {
                rs.next();
                if (rs.getInt("editAllBillboard") == 1) {
                    send.writeInt(1);
                    send.writeUTF(rs.getString("billboardName"));
                    send.flush();
                    if (receiver.read() == 0) {
                        String deleteSchedule = "DELETE FROM schedules WHERE schedules.idBillboard = " + billboardID + ";";
                        query = "DELETE FROM billboards WHERE idBillboards = " + billboardID + ";";
                        st = ServerInit.conn.createStatement();
                        st.executeQuery("USE `cab302`;");
                        st.executeQuery(deleteSchedule);
                        rs = st.executeQuery(query);
                    }
                } else {
                    send.writeInt(0);
                }
            }
            send.flush();
        }
    }

//    Returns the boolean array containing the permission
//    INPUT Sting username
    private static boolean[] UserPermission(String username) throws SQLException {
        boolean[] permission = new boolean[4];
        String query = "SELECT `createBillboard`, `editAllBillboard`, `scheduleBillboard`, `editUser` FROM `users` WHERE `user` = \""+ username + "\";";
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(query);
        if (!rs.isBeforeFirst()) {
//            System.out.println("There is no user");
        } else {
            rs.next();
//            Check to see if the 'Create Billboard' permission is ON or OFF
            if (rs.getInt("createBillboard") == 1) {
                permission[0] = true;
            } else {
                permission[0] = false;
            }
//            Check to see if the 'Edit All Billboards' permission is ON or OFF
            if (rs.getInt("editAllBillboard") == 1) {
                permission[1] = true;
            } else {
                permission[1] = false;
            }
//            Check to see if the 'Schedule Billboard' permission is ON or OFF
            if (rs.getInt("scheduleBillboard") == 1) {
                permission[2] = true;
            } else {
                permission[2] = false;
            }
//            Check to see if the 'Edit Billboard' permission is ON or OFF
            if (rs.getInt("editUser") == 1) {
                permission[3] = true;
            } else {
                permission[3] = false;
            }
        }
        return permission;
    }

    private static String GetBillboardId(ArrayList<String> data) throws SQLException {
        String getBillboard = "SELECT `idBillboards` FROM `billboards` WHERE `billboardName` = '"+data.get(0)+"';";
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        ResultSet rs = st.executeQuery(getBillboard);
        if(!rs.isBeforeFirst())
        {
//            System.out.println("No billboard data");
            return "";
        }
        rs.next();
        String billid = rs.getString("idBillboards");
        return billid;
    }

    private static String GetUserId(ArrayList<String> data) throws SQLException {
        String user = "";
        for(String username : usersAuthenticated.keySet())
        {
            user = username;
        }
        String getUser = "SELECT * FROM `users` WHERE `user` = '"+user+"';";
        Statement st2 = ServerInit.conn.createStatement();
        st2.executeQuery("USE `cab302`;");
        ResultSet rs2 = st2.executeQuery(getUser);
        if(!rs2.isBeforeFirst())
        {
//            System.out.println("No user data");
            return "";
        }
        rs2.next();
        String userid = rs2.getString("idUsers");
        return userid;
    }

    private static Calendar FormatTime(ArrayList<String> data)
    {
        Calendar c = Calendar.getInstance();
        String hoursClean = data.get(2).split(" ")[0].replace("h", "");
        String minsClean = data.get(2).split(" ")[1].replace("m", "");
        int hour = Integer.parseInt(hoursClean);
        int min = Integer.parseInt(minsClean);

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        return c;
    }

    private static void CreateNewSchedule(ObjectInputStream receiver, ObjectOutputStream send) throws SQLException, IOException, ClassNotFoundException {
        receiver.read();
        ArrayList<String> data = (ArrayList<String>) receiver.readObject();

        String billid = GetBillboardId(data);
        String userid = GetUserId(data);
        String date = FormatTime(data).getTime().toString().split(" ")[3];
        int recurring = 0;
        if(data.get(4).equals("Daily"))
        {
            recurring = 1440;
        }
        else if(data.get(4).equals("Hourly"))
        {
            recurring = 60;
        }


        String query = "INSERT INTO schedules (`weekday`, `duration`, `startTime`, `recurring`, `idBillboard`, `userId`) " +
                "VALUES ('"+data.get(1)+"', '"+data.get(3)+"','"+date+"', '"+recurring+"','"+billid+"', '"+userid+"');";
//        System.out.println(query);
        Statement st = ServerInit.conn.createStatement();
        st.executeQuery("USE `cab302`;");
        st.executeQuery(query);
    }

    static void Logout(String userToRemove) throws IOException {
        boolean DEBUGGING = false; // Change this to see if the session token is removed from server
        if (DEBUGGING) {
            System.out.println("Before Logout");
            for (String username : usersAuthenticated.keySet()) {
                String value = usersAuthenticated.get(username).toString();
                System.out.println("Username: " + username + " Value: " + value);
            }
        }
        usersAuthenticated.remove(userToRemove);  // Remove session token from list
        if (DEBUGGING) {
            System.out.println("After Logout");
            for (String username : usersAuthenticated.keySet()) {
                String value = usersAuthenticated.get(username).toString();
                System.out.println("Username: " + username + " Value: " + value);
            }
        }
    }
}
