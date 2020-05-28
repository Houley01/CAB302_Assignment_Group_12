package controlpanel;

import resources.Billboard;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * The controller class is the main abstract class that contains
 * the main logic and rendering for the control panel. This includes
 * methods for modifying data in the database and in server memory:
 * <ul>
 *     <li>Connecting to the server</li>
 *     <li>Show / hide billboard</li>
 *     <li>Create billboards / modify billboards</li>
 *     <li>Editing and creating users</li>
 *     <li>User authentication</li>
 *     <li>Login / logout</li>
 * </ul>
 *
 * @version 	%I%, %G%
 * @since       JDK13
 */

public class controller {

    public static boolean loginSuccessful = false;
    private static String loggedInUser;
    private static String token;
    private static ArrayList<String[]> schedules;

    /**
     *  Connection to server via reading server.config file. Throws
     *  an error if file property cannot be read or an IOException occurs.
     *
     *  @return Returns client socket if it's able to read server.config
     *          or does not encounter an error
     *  @exception throws IOexception
     */
    // Attempts to make a connection to the server
    static Socket connectionToServer() throws IOException {
        try {
            // Gathers the information from server.config file
            resources.GetPropertyValues properties = new resources.GetPropertyValues();
            properties.readPropertyFile();

            // Create a socket connect
            Socket client = new Socket(properties.serverName,  properties.port);

            // If the server connection was made
            return client;
        } catch (IOException e) {
            e.printStackTrace();
            // Developer message
            System.out.println("Server connection doesn't exist.");

            // User pop up window
            DialogWindow.showErrorPane("Server is offline", "Error");

            Socket client = new Socket();
            return client;
        }
    }


    /**
     *  Reading client socket connection and creation of billboard and window configuration.
     *  Once the billboard has been created disconnect the user. The user can either pick a
     *  file or enter a URL.
     *
     *
     * @param billboard          Object class Billboard
     */
    static void createBillboard(Billboard billboard) throws IOException {

        // Initializing connections
        Socket client = connectionToServer();                   // User socket connection
        OutputStream outputStream = client.getOutputStream();   // Server output to user
        InputStream inputStream = client.getInputStream();      // User input to server

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

        send.writeUTF("CreateBillboard");
        send.flush();

        send.writeUTF(loggedInUser);
        send.writeUTF(token);
        send.flush();

        int val = receiver.read();
        if (val == 1) {
            send.writeObject(billboard);
            send.flush(); // Must be done before switching to reading state
//            System.out.println(receiver.readUTF());
            if (receiver.read() ==  1) {
                DialogWindow.showInformationPane("Billboard finished creating.", "Completion of Billboard");
            }
        } else {
            DialogWindow.showErrorPane("Sorry you don't have permission to edit", "Error Can't Make Billboard");
        }
//      End connections
        send.close();
        receiver.close();
        client.close();
    }

    /**
     *  Editing an existing user on the server.
     *
     * @param client    Current socket to server
     * @param username  Username of the client to edit
     * @param password
     * @param fName
     * @param lName
     */
    static void editUser(Socket client, String username, String password, String fName, String lName) throws IOException {

        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

//        send.writeUTF("Hello");
//
//        send.flush(); // Must be done before switching to reading state
//        System.out.println(receiver.readUTF());

//      End connections
        send.close();
        receiver.close();
        client.close();
    }
    /**
     *  Authorization of user login based on stored user information in the database / or memory.
     *
     * @param username          Users username.
     * @param password          Users password.
     * @return loginSuccessful  Global class variable determining if the user login was successful or not.
     * @exception               IOException                 If failure in code input or output operations.
     * @exception               ClassNotFoundException      If unable to find defined CLass.
     * @exception               InvalidKeySpecException
     * @exception               NoSuchAlgorithmException    If cryptographic algorithm is not available.
     *
     */
    static boolean authenticateUserLogin(String username, String password) throws IOException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("user: " + username); // Debugging use
        System.out.println("Password: " + password); // Debugging use

        String hashedPassword = plaintextToHashedPassword(password);

        Socket client = connectionToServer();

        // Checking to see if the socket is currently connected to our server.
        if (client.isConnected()) {
            // connects to the server with information and attempts to authenticate the user

            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

//        Send the server which function it needs to run
            send.writeUTF("Login");
            send.writeUTF(username);
            send.writeUTF(hashedPassword);
            send.flush(); // Must be done before switching to reading state


            // If the server send back a true or false message decides what to do.
            if (receiver.readBoolean()) {
                System.out.println("correct username and password"); // DEBUG CODE
                loginSuccessful = true;
                loggedInUser = username;
                getAuthToken();
            } else {
                // Alert the user that they have incorrectly entered they username or password with
                // a pop up window.
                System.out.println("wrong username or password"); // DEBUG CODE
                // IMPLEMENT AN ERROR MESSAGE FOR USER
                DialogWindow.showErrorPane("Incorrect Login Information", "Error");
                loginSuccessful = false;
            }

//      End connections
            send.close();
            receiver.close();
            client.close();

            password = new String(); // Used to clear the password string (Not very secure though)

        }
        return loginSuccessful;
    }

    /**
     * Converting plain text into a MD5 encrypted string.
     *
     * @param password          Password to encrypt
     * @return                  Returns the MD5 hashed password in a string.
     * @exception               InvalidKeySpecException
     * @exception               NoSuchAlgorithmException    If cryptographic algorithm is not available.
     */
    static String plaintextToHashedPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
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

    static String hashNewPassword(String newPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {

        String newPasswordHashed = plaintextToHashedPassword(newPassword);

        System.out.println("hashing new password...");
        System.out.println(newPasswordHashed);

        return newPasswordHashed;
    }

    static void changePassword(String username, String password) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

        Socket client = connectionToServer();

        // connects to the server with information and attempts to get the auth token for the user after successful login
        if (client.isConnected()) {

            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            String newPassword = hashNewPassword(password);

            System.out.println("Sending new password to server");
            System.out.println(username);

            send.writeUTF("changePassword");
            send.writeUTF(username);
            send.writeUTF(newPassword);
            send.writeUTF(loggedInUser);
            send.writeUTF(token);
            send.flush(); // Must be done before switching to reading state


//      End connections
            send.close();
            receiver.close();
            client.close();

        }
    }

    /**
     * Gets the current socket authentication token
     *
     * @exception IOException               If failure in code input or output operations.
     * @exception ClassNotFoundException    If unable to find defined CLass.
     */
    public static void getAuthToken() throws IOException, ClassNotFoundException {
        Socket client = connectionToServer();

        // connects to the server with information and attempts to get the auth token for the user after successful login
        if (client.isConnected()) {

            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("AuthToken");
            send.writeUTF(loggedInUser);
            send.flush(); // Must be done before switching to reading state

            // Store the auth token for the user
            token = (String) receiver.readObject();
            System.out.println(token);

//      End connections
            send.close();
            receiver.close();
            client.close();
        }
    }

    /**
     * Connects to server and stores the current listings.
     * - Todo database connectivity?
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void RequestBillboardScheduling() throws IOException, ClassNotFoundException {

        Socket client = connectionToServer();

        // connects to the server with information and attempts to get the auth token for the user after successful login
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("RequestScheduleBillboards");

            send.writeUTF(loggedInUser);
            send.writeUTF(token);
            send.flush();

            // Store the current schedule listings
            schedules = (ArrayList<String[]>) receiver.readObject();

            if (schedules != null) {
                int scheduleCounter = 1;
                for(String[] schedule : schedules){
                    System.out.println("\nScheduled billboard " + scheduleCounter + ":");
                    System.out.println("Day: " + schedule[1]);
                    System.out.println("Duration: " + schedule[2]);
                    System.out.println("Start Time:  " + schedule[3]);

                    scheduleCounter++;
                }
            } else {
                System.out.println("There is currently no scheduled billboards");
            }

//      End connections
            send.close();
            receiver.close();
            client.close();
        }
    }

    /**
     *  Modifies visibility state of login.window to false and -renables nav bar-
     *
     */
    public static void hideLoginScreen() {
        login.window.setVisible(false);
        ControlPanelFrameHandler.bar.setVisible(true);
    }

    /**
     * Helper function to toggle visibility of windows.
     *
     * @param component     Component needing toggle show / hide functionality.
     */
    public static void toggleVisibility(JComponent component)
    {
        // isVisible() returns a bool
        // true = is currently opened
        // false = is currently closed
        if(component.isVisible())
        {
            component.setVisible(false);
        }
        else if(!component.isVisible())
        {
            component.setVisible(true);
        }
    }


    public static void showCreateBillboard(){
        toggleVisibility(createBillboards.window);
    }
    public static void showListBillboard(){
        toggleVisibility(listBillboards.window);
    }

    /**
     *  Toggle switch to show or hide Schedule window via modifying isVisible state
     */
    public static void showSchedule() throws IOException, ClassNotFoundException {

        toggleVisibility(scheduleBillboards.window);
        if(!scheduleBillboards.window.isVisible())
        {
            RequestBillboardScheduling();
        }
    }
    /**
     *  Toggle switch to show or hide edit user window via modifying isVisible state
     */
    public static void showEditUser() {
        toggleVisibility(usersPage.window);
    }

    /**
     *  Toggle switch to show or hide admin preferences via modifying isVisible state
     */
    public static void showAdminPreferences() {
        toggleVisibility(usersPage.adminWindow);
    }

    /**
     *  Toggle switch to show or hide user preferences via modifying isVisible state
     */
    public static void ShowUserPreferences() {
        toggleVisibility(usersPage.userWindow);
    }

    /**
     *  Toggle switch to show or hide help screen via modifying isVisible state
     */
    public static void showHelpScreen() {
        toggleVisibility(HelpPage.window);
    }

    /**
     * Create base64 image from users selected file
     * @see createBillboards
     *
     * @param file      File to be converted to base64.
     * @return          Converted base64 image from user selected file.
     */
    public static String CreateMD5(File file) {
        String base64Image = "";
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }

    /**
     * Logs the current user out of their session.
     * Todo modify code to actually log the user out to the login page
     */
    public static void logout() {
//       EXIT FOR THE MOMENT WILL CHANGE to login page
        System.exit(0);
    }

    public static String[][] ListBillboards() throws IOException, ClassNotFoundException {
        Socket client = connectionToServer();
        ArrayList<String[]> billboardArrayList;
        String[][] billboardList = new String[][]{
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""}
        };

        //  Checks if the sever is online
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("ListBillboards");
            send.flush();

                // Store the current schedule listings
            int val = receiver.read();
            if (val == 1) {
                billboardArrayList = (ArrayList<String[]>) receiver.readObject();

                if (billboardArrayList != null) {
                    billboardList = new String[billboardArrayList.size()][6];
                    int counter = 0;
                    for (String[] billboard : billboardArrayList) {
                        billboardList[counter][0] = billboard[0];
                        billboardList[counter][1] = billboard[1];
                        billboardList[counter][2] = billboard[2];
                        billboardList[counter][3] = billboard[3];
                        billboardList[counter][4] = billboard[4];
                        billboardList[counter][5] = billboard[5];
                        counter++;
                    }
                }
            }else {
                System.out.println("No data");
            }
//      End connections
            send.close();
            receiver.close();
            client.close();
        }
        return billboardList;
    }

    public static void EditSelectedBillboard(String billboardId) throws IOException, ClassNotFoundException {
        int id = Integer.parseInt(billboardId);
        Socket client = connectionToServer();
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("GetBillboard");
            send.write(id);
            send.flush();

            int val = receiver.read();
            if (val == 1) {
                Billboard billboard = (Billboard) receiver.readObject();
                createBillboards.input1.setText(billboard.getTitle());
                createBillboards.input2.setText(billboard.getMessageText());
                createBillboards.informationColourInput.setText(billboard.getInformationText());
                createBillboards.textDisplayColour.setBackground(Color.decode(billboard.getMessageColour()));
                createBillboards.informationTextColor.setBackground(Color.decode(billboard.getInformationColour()));
                createBillboards.backgroundDisplayColour.setBackground(Color.decode(billboard.getBackgroundColour()));
                createBillboards.window.toFront();
                showCreateBillboard();

            } else {
//                Display POP ERROR MESSAGE
                DialogWindow.showErrorPane("Please refresh the billboard list", "Error: Could NOT find billboard");
            }

            send.close();
            receiver.close();
            client.close();
        }
    }

    static ArrayList<String> getListOfUsers() throws IOException, ClassNotFoundException {

        ArrayList<String> listOfUsers = new ArrayList<>();

        Socket client = connectionToServer();

        // connects to the server with information and attempts to get the auth token for the user after successful login
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("getUsers");
            send.flush();

            // Store the current schedule listings
            listOfUsers = (ArrayList<String>) receiver.readObject();

//      End connections
            send.close();
            receiver.close();
            client.close();
        }
        return listOfUsers;
    }

}

//public static String[][] ListBillboards() throws IOException, ClassNotFoundException {
//        Socket client = connectionToServer();
//        ArrayList<String[]> billboardArrayList;
//        String[][] billboardList = new String[][]{
//                {"", "", "", "", "", ""},
//                {"", "", "", "", "", ""},
//                {"", "", "", "", "", ""},
//                {"", "", "", "", "", ""},
//                {"", "", "", "", "", ""},
//                {"", "", "", "", "", ""}
//        };
//
//        //  Checks if the sever is online
//        if (client.isConnected()) {
//            OutputStream outputStream = client.getOutputStream();
//            InputStream inputStream = client.getInputStream();
//
//            ObjectOutputStream send = new ObjectOutputStream(outputStream);
//            ObjectInputStream receiver = new ObjectInputStream(inputStream);
//
//            send.writeUTF("ListBillboards");
//            send.flush();
//
//                // Store the current schedule listings
//            int val = receiver.read();
//            if (val == 1) {
//                billboardArrayList = (ArrayList<String[]>) receiver.readObject();
//
//                if (billboardArrayList != null) {
//                    billboardList = new String[billboardArrayList.size()][6];
//                    int counter = 0;
//                    for (String[] billboard : billboardArrayList) {
//                        billboardList[counter][0] = billboard[0];
//                        billboardList[counter][1] = billboard[1];
//                        billboardList[counter][2] = billboard[2];
//                        billboardList[counter][3] = billboard[3];
//                        billboardList[counter][4] = billboard[4];
//                        billboardList[counter][5] = billboard[5];
//                        counter++;
//                    }
//                }
//            }else {
//                System.out.println("No data");
//            }
////      End connections
//            send.close();
//            receiver.close();
//            client.close();
//        }
//        return billboardList;
//    }
//
//    public static void EditSelectedBillboard(String billboardId) throws IOException, ClassNotFoundException {
//        int id = Integer.parseInt(billboardId);
//        Socket client = connectionToServer();
//        if (client.isConnected()) {
//            OutputStream outputStream = client.getOutputStream();
//            InputStream inputStream = client.getInputStream();
//
//            ObjectOutputStream send = new ObjectOutputStream(outputStream);
//            ObjectInputStream receiver = new ObjectInputStream(inputStream);
//
//            send.writeUTF("GetBillboard");
//            send.write(id);
//            send.flush();
//
//            int val = receiver.read();
//            if (val == 1) {
//                Billboard billboard = (Billboard) receiver.readObject();
//                createBillboards.input1.setText(billboard.getTitle());
//                createBillboards.input2.setText(billboard.getMessageText());
//                createBillboards.informationColourInput.setText(billboard.getInformationText());
//                createBillboards.textDisplayColour.setBackground(Color.decode(billboard.getMessageColour()));
//                createBillboards.informationTextColor.setBackground(Color.decode(billboard.getInformationColour()));
//                createBillboards.backgroundDisplayColour.setBackground(Color.decode(billboard.getBackgroundColour()));
//                createBillboards.window.toFront();
//                showCreateBillboard();
//
//            } else {
////                Display POP ERROR MESSAGE
//                DialogWindow.showErrorPane("Please refresh the billboard list", "Error: Could NOT find billboard");
//            }
//
//            send.close();
//            receiver.close();
//            client.close();
//        }
//    }

