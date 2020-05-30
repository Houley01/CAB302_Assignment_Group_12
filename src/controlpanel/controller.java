package controlpanel;

import resources.Billboard;
import resources.UserPermission;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
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
    static String loggedInUser;
    private static String token;
    private static ArrayList<String[]> schedules;
    public static UserPermission permission = new UserPermission();

    /**
     *  Connection to server via reading server.config file. Throws
     *  an error if file property cannot be read or an IOException occurs.
     *
     *  @return Returns client socket if it's able to read server.config
     *          or does not encounter an error
     *  @exception throws IOexception
     */
    // Attempts to make a connection to the server
    public static Socket connectionToServer() throws IOException {
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
                boolean[] temp = (boolean[]) receiver.readObject();
                permission.SetUserPermission(temp);
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
        scheduleBillboards.reload(scheduleBillboards.tableCalendar);
        toggleVisibility(scheduleBillboards.window);
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
        if (controller.permission.GetUserPermission("EditUser")) {
            toggleVisibility(usersPage.adminWindow);
        } else {
            DialogWindow.NoAccessTo("Admin options");
        }
    }

    /**
     *  Toggle switch to show or hide user preferences via modifying isVisible state
     */
    public static void ShowUserPreferences() {
        //toggleVisibility(usersPage.userWindow);
    }

    /**
     *  Toggle switch to show or hide help screen via modifying isVisible state
     */
    public static void showHelpScreen() {
        toggleVisibility(HelpPage.window);
    }

    public static void showCreateSchedule(){toggleVisibility(createSchedule.window);}

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
    public static void Logout() throws IOException, ClassNotFoundException {
        ControlPanelFrameHandler.LogoutWindow();
        Socket client = connectionToServer();
        //  Checks if the sever is online
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("Logout"); // Send logout to server
            send.writeUTF(loggedInUser); // Send username to server
            send.flush();
//      End connections
            send.close();
            receiver.close();
            client.close();
        }
//        Clear local client stored information
        loggedInUser = "";
        loginSuccessful = false;
        boolean[] falsePermission = {false, false, false, false};
        permission.SetUserPermission(falsePermission);
        token = "";
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
            send.writeUTF(loggedInUser);
            send.flush();

            int val = receiver.readInt();
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

            }
            if (val == -1) {
                DialogWindow.NoAccessTo("to edit this billboard");
            }

            else {
//                Display POP ERROR MESSAGE
                DialogWindow.showErrorPane("Please refresh the billboard list", "Error: Could NOT find billboard");
            }

            send.close();
            receiver.close();
            client.close();
        }
    }

    public static void DeleteBillboard(String billboardId) throws IOException {
        int id = Integer.parseInt(billboardId);
        Socket client = connectionToServer();
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("DeleteBillboard");
            send.write(id);
            send.flush();

            int val = receiver.readInt();
            if (val == 1) {
                String billboardName = receiver.readUTF();
                int yesOrNo = DialogWindow.showYesNoPane("Are you sure you want to delete billboard: " +
                        billboardName + "?", "Alert Deleting Billboard");
                send.write(yesOrNo);
                send.flush();
            } else {
//                Display POP ERROR MESSAGE
                if (val == 0) {
                    DialogWindow.NoAccessTo("to delete this billboard");
                } else if (val == -1) {
                    DialogWindow.showErrorPane("Please refresh the billboard list",
                            "Error: Could NOT find billboard"
                    );
                }
            }

//      End connections
                send.close();
                receiver.close();
                client.close();
        }
    }

    /**
     * 3D Array list to 3D String array. Found on stackoverflow.
     * <a href="https://stackoverflow.com/questions/34744288/java-3d-arraylist-into-a-3d-array">here</a>.
     * Modified version to fit the application the version provided on stack overflow is for 3 layers of arrays.
     *
     * @author lupz
     * @param input
     * @return
     */
    public static String[][] MListToMArray(ArrayList<String[]> input)
    {
        String[][]                            output;
        String[][]                              tmp;
        String[] lvl2;
        ArrayList<String>                       lvl3;


        output = new String[input.size()][];
        for (int outer = 0; outer < input.size(); ++outer) {
            output[outer] = input.get(outer);
        }
        return output;
    }

    public static String[][] listSchedule() throws IOException, ClassNotFoundException {
        System.out.println(loggedInUser);
        if(loggedInUser == null) return new String[][]{};
        Socket client = connectionToServer();           // Get user connection

        ArrayList<String[]> scheduled;         // Create billboard array list

        if(!client.isConnected() || loggedInUser == null)
        {
            System.out.println("No data");
            client.close();
            return new String[][]{};
        }

        //  Checks if the sever is online
        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

        System.out.println("Connecting to server");

        send.writeUTF("RequestScheduleBillboards");
        send.writeUTF(loggedInUser);
        send.writeUTF(token);
        send.flush();

        int val = receiver.read();
        System.out.println(val);
        ArrayList<String[]> list = (ArrayList<String[]>) receiver.readObject();
        scheduled =  list;
        if(val == 1)
        {
            System.out.println(scheduled);
            String[][] test = MListToMArray(scheduled);
            System.out.println(Arrays.toString(test));
        }
        // End connections
        send.close();
        receiver.close();
        client.close();
        return new String[][]{};
    }

    public static String GetBillboardFromID(String id) throws IOException, ClassNotFoundException {
        Socket client = connectionToServer();
        if(!client.isConnected()) return new String();
        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

        send.writeUTF("GetBillboardFromID");
        send.writeUTF(id);
        send.flush();

        String temp = receiver.readUTF();

        send.close();
        receiver.close();
        client.close();
        return temp;
    }

    public static ArrayList<String> GetBillBoardFromTimes(String min, String max) throws IOException, ClassNotFoundException {
        Socket client = connectionToServer();
        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

        System.out.println("Creating new schedule");
        send.writeUTF("GetScheduledBillboard");
        send.writeUTF(min);
        send.writeUTF(max);
        send.flush();

        ArrayList<String> temp = new ArrayList<>();

        receiver.read();
        try
        {
            temp = (ArrayList<String>) receiver.readObject();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(temp);
        send.close();
        receiver.close();
        client.close();

        return temp;
    }

    public static void createNewSchedule(ArrayList<String> vals) throws IOException {
        Socket client = connectionToServer();
        if(!client.isConnected()) return;
        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

        System.out.println("Creating new schedule");
        send.writeUTF("CreateSchedule");
        send.writeObject(vals);
        send.flush();


    }

    public static ArrayList<String[]> RequestScheduleBillboards() throws IOException, ClassNotFoundException {
        if(loggedInUser == null) return new ArrayList<>();
        Socket client = connectionToServer();
        if(!client.isConnected()) return new ArrayList<String[]>();
        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);


        System.out.println("Requesting new schedule");
        send.writeUTF("RequestScheduleBillboards");
        send.writeUTF(loggedInUser);
        send.writeUTF(token);
        send.flush();

        receiver.read();
        ArrayList<String[]> temp = (ArrayList<String[]>) receiver.readObject();
        send.close();
        receiver.close();
        client.close();

        return temp;

    }

    static ArrayList<String> getListOfUsers() throws IOException, ClassNotFoundException {

        ArrayList<String> listOfUsers = new ArrayList<>();

        Socket client = connectionToServer();

        // connects to the server with information and attempts to get the auth token
        // for the user after successful login
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("getUsers");
            send.flush();

            // Store the current schedule listings
            listOfUsers = (ArrayList<String>) receiver.readObject();

            // End connections
            send.close();
            receiver.close();
            client.close();
        }
        return listOfUsers;
    }

    static String[] getUserInfo(String username) throws IOException, ClassNotFoundException {

        String[] userInfo = new String[2];

        Socket client = connectionToServer();

        // connects to the server with information and attempts to get the auth token
        // for the user after successful login
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("getUserInfo");
            send.writeUTF(username);
            send.writeUTF(loggedInUser);
            send.writeUTF(token);
            send.flush();

            // Store the current list of users
            userInfo = (String[]) receiver.readObject();

            // End connections
            send.close();
            receiver.close();
            client.close();
        }
        return userInfo;
    }

    static String[] updateUserInfo(String firstName, String lastName) {
        String[] updatedUserInfo = { firstName, lastName };
        return updatedUserInfo;
    }

    static void updateUserDetails(String username, String firstName, String lastName) throws IOException {

        Socket client = connectionToServer();

        // connects to the server with information and attempts to get the auth token
        // for the user after successful login
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            String[] updatedUserInfo = updateUserInfo(firstName, lastName);

            send.writeUTF("updateUserInfo");
            send.writeUTF(username);
            send.writeUTF(updatedUserInfo[0]);
            send.writeUTF(updatedUserInfo[1]);
            send.writeUTF(loggedInUser);
            send.writeUTF(token);
            send.flush();

            // End connections
            send.close();
            receiver.close();
            client.close();
        }
    }

    static boolean deleteUser(ArrayList<String> userList, String username) {
        boolean wasUserInList = false;

        ArrayList<String> currentUserList = userList;

        if (!username.equals(loggedInUser)) {
            if (currentUserList.contains(username)) {
                currentUserList.remove(username);
                wasUserInList = true;
            }
        } else {
            // Ensure that the dialog doesn't attempt to show up for the unit test
            if (!loggedInUser.equals("ThisIsATestUser")) {
                // Display POP ERROR MESSAGE
                DialogWindow.showErrorPane("Cannot delete yourself. Please select a user that isn't yourself.",
                        "Error: Attempted to delete self");
            }
        }

        return wasUserInList;
    }

    static void deleteUserFromDB(ArrayList<String> userList, String username) throws IOException {

        // If the user does exist in the list of users pulled from the database, send
        // delete request to the server
        if (deleteUser(userList, username)) {
            Socket client = connectionToServer();
            System.out.println("Sending request to server to delete user");
            // connects to the server with information and attempts to get the auth token
            // for the user after successful login
            if (client.isConnected()) {
                OutputStream outputStream = client.getOutputStream();
                InputStream inputStream = client.getInputStream();

                ObjectOutputStream send = new ObjectOutputStream(outputStream);
                ObjectInputStream receiver = new ObjectInputStream(inputStream);

                send.writeUTF("deleteUser");
                send.writeUTF(username);
                send.writeUTF(loggedInUser);
                send.writeUTF(token);
                send.flush();

                // End connections
                send.close();
                receiver.close();
                client.close();
            }
        }
    }

    static boolean[] getUserPermissions(String username) throws IOException, ClassNotFoundException {

        boolean[] userPermissions = new boolean[4];

        Socket client = connectionToServer();

        // connects to the server with information and attempts to get the auth token
        // for the user after successful login
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("getUserPermissions");
            send.writeUTF(username);
            send.writeUTF(loggedInUser);
            send.writeUTF(token);
            send.flush();

            // Store the list of permissions for the user
            userPermissions[0] = receiver.readBoolean();
            userPermissions[1] = receiver.readBoolean();
            userPermissions[2] = receiver.readBoolean();
            userPermissions[3] = receiver.readBoolean();

            // End connections
            send.close();
            receiver.close();
            client.close();
        }
        return userPermissions;
    }

    static boolean[] updateUserPermissions(boolean[] currentPermissions, boolean[] updatedPermissions, String username) {
        boolean[] newPermissionsForUser = currentPermissions;

        if (!username.equals(loggedInUser)) {
            newPermissionsForUser = updatedPermissions;
        } else if (!loggedInUser.equals("ThisIsATestUser")) {
            // Display POP ERROR MESSAGE
            DialogWindow.showErrorPane("Cannot change own permissions. Please select a user that isn't yourself.",
                    "Error: Attempted to update own permissions");
        }

        return newPermissionsForUser;
    }

    static void updateUserPermissionsToDB(boolean[] currentPermissions, boolean[] updatedPermissions, String username) throws IOException {
        System.out.println(username);
        boolean[] newPermissionsForUser = updateUserPermissions(currentPermissions, updatedPermissions, username);

        // If the permissions of a user that are being updated is not the user attempting to change their own, send the request to the server
        if (!Arrays.equals(newPermissionsForUser, currentPermissions)) {
            Socket client = connectionToServer();
            System.out.println("Sending request to server to update user permissions for user: " + username);
            // connects to the server with information and attempts to get the auth token
            // for the user after successful login
            if (client.isConnected()) {
                OutputStream outputStream = client.getOutputStream();
                InputStream inputStream = client.getInputStream();

                ObjectOutputStream send = new ObjectOutputStream(outputStream);
                ObjectInputStream receiver = new ObjectInputStream(inputStream);

                for (boolean b : newPermissionsForUser) {
                    System.out.println(b);
                }

                send.writeUTF("updatePermissions");
                send.writeBoolean(newPermissionsForUser[0]);
                send.writeBoolean(newPermissionsForUser[1]);
                send.writeBoolean(newPermissionsForUser[2]);
                send.writeBoolean(newPermissionsForUser[3]);
                send.writeUTF(username);
                send.writeUTF(loggedInUser);
                send.writeUTF(token);
                send.flush();

                // End connections
                send.close();
                receiver.close();
                client.close();
            }
        }
    }

    static boolean createNewUser(String[] userData, ArrayList<String> listOfUsers) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        // We assume the data entered had no missing fields until we find a field that was either null or empty
        boolean wasAllUserDataEnteredCorrect = true;

        if (!listOfUsers.contains(userData[0])) {
            for (String s : userData) {
                if ((s == null || s.isEmpty())) {
                    wasAllUserDataEnteredCorrect = false;

                    // Display the GUI error if the above condition is not a result of the unit test
                    if (!userData[0].equals("ThisIsATestUser")) {
                        DialogWindow.showErrorPane("Attempted to create a user with empty fields. Please ensure you fill every field and try again.",
                                "Error: Attempted to create user with missing fields");
                        break;
                    }
                }
            }
            userData[3] = plaintextToHashedPassword(userData[3]);
        }
         else {
            // Username already exists
            wasAllUserDataEnteredCorrect = false;

            // Display the GUI error if the above condition is not a result of the unit test
            if (!userData[0].equals("ThisIsATestUser")) {
                DialogWindow.showErrorPane("Attempted to create a user with a username that already exists. Please choose a different username and try again.",
                        "Error: Attempted to create user with a username that already exists");
            }
        }
        return wasAllUserDataEnteredCorrect;
    }

    static void createNewUserToDB(String[] userData, boolean[] userPermissions) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {

        // If data had no missing fields, submit the user creation details to the server
        if (createNewUser(userData, getListOfUsers())) {
            Socket client = connectionToServer();

            // connects to the server with information and attempts to get the auth token
            // for the user after successful login
            if (client.isConnected()) {
                OutputStream outputStream = client.getOutputStream();
                InputStream inputStream = client.getInputStream();

                ObjectOutputStream send = new ObjectOutputStream(outputStream);
                ObjectInputStream receiver = new ObjectInputStream(inputStream);

                send.writeUTF("createNewUser");
                send.writeUTF(userData[0]);
                send.writeUTF(userData[1]);
                send.writeUTF(userData[2]);
                send.writeUTF(userData[3]);
                send.writeBoolean(userPermissions[0]);
                send.writeBoolean(userPermissions[1]);
                send.writeBoolean(userPermissions[2]);
                send.writeBoolean(userPermissions[3]);
                send.writeUTF(loggedInUser);
                send.writeUTF(token);
                send.flush();

                // End connections
                send.close();
                receiver.close();
                client.close();
            }
        }
        }
}
