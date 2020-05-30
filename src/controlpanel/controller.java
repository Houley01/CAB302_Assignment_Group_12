package controlpanel;

import resources.Billboard;
import resources.GetPropertyValues;
import resources.UserPermission;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

/**
 * The Controller class is the main abstract class that contains
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

public class Controller {

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
     */
    // Attempts to make a connection to the server
    public static Socket ConnectionToServer(){
        try {
            // Gathers the information from server.config file
            resources.GetPropertyValues properties = new resources.GetPropertyValues();
            properties.readPropertyFile();

            // Create a socket connect If the server connection was made
            return new Socket(properties.serverName, GetPropertyValues.port);
        } catch (IOException e) {

            // Alert the user that the server is offline. Produce a pop up window
            DialogWindow.ShowErrorPane("Server is offline", "Error");
            return new Socket();
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
    static void CreateBillboard(Billboard billboard) throws IOException {

        // Initializing connections
        Socket client = ConnectionToServer();                   // User socket connection
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
            if (receiver.read() ==  1) {
                DialogWindow.ShowInformationPane("Billboard finished creating.", "Completion of Billboard");
            }
        } else {
            DialogWindow.ShowErrorPane("Sorry you don't have permission to edit", "Error Can't Make Billboard");
        }
//      End connections
        send.close();
        receiver.close();
        client.close();
    }

    /**
     *  Authorization of user Login based on stored user information in the database / or memory.
     *
     * @param username          Users username.
     * @param password          Users password.
     * @return loginSuccessful  Global class variable determining if the user Login was successful or not.
     * @exception               IOException                 If failure in code input or output operations.
     * @exception               ClassNotFoundException      If unable to find defined CLass.
     * @exception               InvalidKeySpecException     If the character key is invalid character for UTF-8
     * @exception               NoSuchAlgorithmException    If cryptographic algorithm is not available.
     *
     */
    static boolean AuthenticateUserLogin(String username, String password) throws IOException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException {
        String hashedPassword = PlaintextToHashedPassword(password);
        Socket client = ConnectionToServer();

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
                loginSuccessful = true;
                loggedInUser = username;
                boolean[] temp = (boolean[]) receiver.readObject();
                permission.SetUserPermission(temp);
                GetAuthToken();
                Login.passwordText.setText("");
                ListBillboards();
            } else {
                // Alert the user that they have incorrectly entered they username or password with
                // a pop up window.
                DialogWindow.ShowErrorPane("Incorrect Login Information", "Error");
                loginSuccessful = false;
            }

//      End connections
            send.close();
            receiver.close();
            client.close();

            password = ""; // Used to clear the password string (Not very secure though)

        }
        return loginSuccessful;
    }

    /**
     * Converting plain text into a MD5 encrypted string.
     *
     * @param password          Password to encrypt
     * @return                  Returns the MD5 hashed password in a string.
     * @exception               InvalidKeySpecException     If the character key is invalid character for UTF-8
     * @exception               NoSuchAlgorithmException    If cryptographic algorithm is not available.
     */
    static String PlaintextToHashedPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
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

    static String HashNewPassword(String newPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {

        String newPasswordHashed = PlaintextToHashedPassword(newPassword);

//        System.out.println("hashing new password...");
//        System.out.println(newPasswordHashed);

        return newPasswordHashed;
    }

    static void ChangePassword(String username, String password) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

        Socket client = ConnectionToServer();

        // connects to the server with information and attempts to get the auth token for the user after successful Login
        if (client.isConnected()) {

            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            String newPassword = HashNewPassword(password);

//            System.out.println("Sending new password to server");
//            System.out.println(username);

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
    public static void GetAuthToken() throws IOException, ClassNotFoundException {
        Socket client = ConnectionToServer();

        // connects to the server with information and attempts to get the auth token for the user after successful Login
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
//            System.out.println(token);

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

        Socket client = ConnectionToServer();

        // connects to the server with information and attempts to get the auth token for the user after successful Login
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
//                    System.out.println("\nScheduled billboard " + scheduleCounter + ":");
//                    System.out.println("Day: " + schedule[1]);
//                    System.out.println("Duration: " + schedule[2]);
//                    System.out.println("Start Time:  " + schedule[3]);

                    scheduleCounter++;
                }
            } else {
                DialogWindow.ShowInformationPane("There is currently no scheduled billboards", "No billboard scheduled");
//                System.out.println("There is currently no scheduled billboards");
            }

//      End connections
            send.close();
            receiver.close();
            client.close();
        }
    }

    /**
     *  Modifies visibility state of Login.window to false and -renables nav bar-
     *
     */
    public static void HideLoginScreen() {
        Login.window.setVisible(false);
        ControlPanelFrameHandler.bar.setVisible(true);
    }

    /**
     * Helper function to toggle visibility of windows.
     *
     * @param component     Component needing toggle show / hide functionality.
     */
    public static void ToggleVisibility(JComponent component)
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


    public static void ShowCreateBillboard(){
        ToggleVisibility(CreateBillboards.window);
    }
    public static void ShowListBillboard(){
        ToggleVisibility(ListBillboards.window);
    }

    /**
     *  Toggle switch to show or hide Schedule window via modifying isVisible state
     */
    public static void ShowSchedule() throws IOException, ClassNotFoundException {
        ToggleVisibility(ScheduleBillboards.window);
    }
    /**
     *  Toggle switch to show or hide edit user window via modifying isVisible state
     */
    public static void ShowEditUser() {
        ToggleVisibility(UsersPage.window);
    }

    /**
     *  Toggle switch to show or hide admin preferences via modifying isVisible state
     */
    public static void ShowAdminPreferences() {
        if (Controller.permission.GetUserPermission("EditUser")) {
            ToggleVisibility(UsersPage.adminWindow);
        } else {
            DialogWindow.NoAccessTo("Admin options");
        }
    }

    /**
     *  Toggle switch to show or hide help screen via modifying isVisible state
     */
    public static void ShowHelpScreen() {
        ToggleVisibility(HelpPage.window);
    }

    /**
     *  Toggle switch to show or hide help screen via modifying isVisible state
     */
    public static void ShowCreateSchedule(){
        ToggleVisibility(CreateSchedule.window);
    }

    /**
     * Create base64 image from users selected file
     * @see CreateBillboards
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
            DialogWindow.ShowErrorPane("Image could not be found.", "Error Image Missing");
//            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            DialogWindow.ShowErrorPane("Error while the image was being converted.", "Error Converting Image");
//            System.out.println("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }

    /**
     * Logs the current user out of their session.
     * Sends a message to the server to remove
     */
    public static void Logout() throws IOException {
        ControlPanelFrameHandler.LogoutWindow();
        Socket client = ConnectionToServer();
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
//        Resetting the application for the user on the client-side
        loggedInUser = "";
        Login.usernameText.setText("");
        loginSuccessful = false;
        boolean[] falsePermission = {false, false, false, false};
        permission.SetUserPermission(falsePermission);
        token = "";
    }

    public static String[][] ListBillboards() throws IOException, ClassNotFoundException {
        Socket client = ConnectionToServer();
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
            }
//            else {
//                System.out.println("No data");
//            }
//      End connections
            send.close();
            receiver.close();
            client.close();
        }
        return billboardList;
    }

    public static void EditSelectedBillboard(String billboardId) throws IOException, ClassNotFoundException {
        int id = Integer.parseInt(billboardId);
        Socket client = ConnectionToServer();
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
                CreateBillboards.input1.setText(billboard.getTitle());
                CreateBillboards.input2.setText(billboard.getMessageText());
                CreateBillboards.informationColourInput.setText(billboard.getInformationText());
                CreateBillboards.textDisplayColour.setBackground(Color.decode(billboard.getMessageColour()));
                CreateBillboards.informationTextColor.setBackground(Color.decode(billboard.getInformationColour()));
                CreateBillboards.backgroundDisplayColour.setBackground(Color.decode(billboard.getBackgroundColour()));
                CreateBillboards.window.toFront();
                ShowCreateBillboard();

            }
            if (val == -1) {
                DialogWindow.NoAccessTo("to edit this billboard");
            }

            else {
//                Display POP ERROR MESSAGE
                DialogWindow.ShowErrorPane("Please refresh the billboard list", "Error: Could NOT find billboard");
            }

            send.close();
            receiver.close();
            client.close();
        }
    }

    public static void DeleteBillboard(String billboardId) throws IOException {
        int id = Integer.parseInt(billboardId);
        Socket client = ConnectionToServer();
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
                int yesOrNo = DialogWindow.ShowYesNoPane("Are you sure you want to delete billboard: " +
                        billboardName + "?", "Alert Deleting Billboard");
                send.write(yesOrNo);
                send.flush();
            } else {
//                Display POP ERROR MESSAGE
                if (val == 0) {
                    DialogWindow.NoAccessTo("to delete this billboard");
                } else if (val == -1) {
                    DialogWindow.ShowErrorPane("Please refresh the billboard list",
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

    public static String[][] ListSchedule() throws IOException, ClassNotFoundException {
        Socket client = ConnectionToServer();           // Get user connection

        ArrayList<String[]> scheduled;         // Create billboard array list


        if(!client.isConnected())
        {
            return new String[][]{};
        }

        //  Checks if the sever is online
        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

        send.writeUTF("RequestScheduleBillboards");
        send.flush();

        int val = receiver.read();
        ArrayList<String[]> list = (ArrayList<String[]>) receiver.readObject();
        scheduled =  list;
        if(val == 1)
        {
//            System.out.println(scheduled);
            String[][] test = MListToMArray(scheduled);
//            System.out.println(Arrays.toString(test));
        }
        // End connections
        send.close();
        receiver.close();
        client.close();
        return new String[][]{};
    }

    public static void CreateNewSchedule(ArrayList<String> vals) throws IOException {
        Socket client = ConnectionToServer();
        if(!client.isConnected()) return;
        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

//        System.out.println("Creating new schedule");
        send.writeUTF("CreateSchedule");
        send.writeObject(vals);
        send.flush();
    }

    static ArrayList<String> GetListOfUsers() throws IOException, ClassNotFoundException {

        ArrayList<String> listOfUsers = new ArrayList<>();

        Socket client = ConnectionToServer();

        // connects to the server with information and attempts to get the auth token
        // for the user after successful Login
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

    static String[] GetUserInfo(String username) throws IOException, ClassNotFoundException {

        String[] userInfo = new String[2];

        Socket client = ConnectionToServer();

        // connects to the server with information and attempts to get the auth token
        // for the user after successful Login
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

    static String[] UpdateUserInfo(String firstName, String lastName) {
        String[] updatedUserInfo = { firstName, lastName };
        return updatedUserInfo;
    }

    static void UpdateUserDetails(String username, String firstName, String lastName) throws IOException {

        Socket client = ConnectionToServer();

        // connects to the server with information and attempts to get the auth token
        // for the user after successful Login
        if (client.isConnected()) {
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            String[] updatedUserInfo = UpdateUserInfo(firstName, lastName);

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

    static boolean DeleteUser(ArrayList<String> userList, String username) {
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
                DialogWindow.ShowErrorPane("Cannot delete yourself. Please select a user that isn't yourself.",
                        "Error: Attempted to delete self");
            }
        }

        return wasUserInList;
    }

    static void DeleteUserFromDB(ArrayList<String> userList, String username) throws IOException {

        // If the user does exist in the list of users pulled from the database, send
        // delete request to the server
        if (DeleteUser(userList, username)) {
            Socket client = ConnectionToServer();
//            System.out.println("Sending request to server to delete user");
            // connects to the server with information and attempts to get the auth token
            // for the user after successful Login
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

    static boolean[] GetUserPermissions(String username) throws IOException, ClassNotFoundException {

        boolean[] userPermissions = new boolean[4];

        Socket client = ConnectionToServer();

        // connects to the server with information and attempts to get the auth token
        // for the user after successful Login
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
            userPermissions = (boolean[]) receiver.readObject();

            // End connections
            send.close();
            receiver.close();
            client.close();
        }
        return userPermissions;
    }

    static boolean[] UpdateUserPermissions(boolean[] currentPermissions, boolean[] updatedPermissions, String username) {
        boolean[] newPermissionsForUser = currentPermissions;

        if (!username.equals(loggedInUser)) {
            newPermissionsForUser = updatedPermissions;
        } else if (!loggedInUser.equals("ThisIsATestUser")) {
            // Display POP ERROR MESSAGE
            DialogWindow.ShowErrorPane("Cannot change own permissions. Please select a user that isn't yourself.",
                    "Error: Attempted to update own permissions");
        }

        return newPermissionsForUser;
    }

    static void UpdateUserPermissionsToDB(boolean[] currentPermissions, boolean[] updatedPermissions, String username) throws IOException {
//        System.out.println(username);
        boolean[] newPermissionsForUser = UpdateUserPermissions(currentPermissions, updatedPermissions, username);

        // If the permissions of a user that are being updated is not the user attempting to change their own, send the request to the server
        if (!Arrays.equals(newPermissionsForUser, currentPermissions)) {
            Socket client = ConnectionToServer();
//            System.out.println("Sending request to server to update user permissions for user: " + username);
            // connects to the server with information and attempts to get the auth token
            // for the user after successful Login
            if (client.isConnected()) {
                OutputStream outputStream = client.getOutputStream();
                InputStream inputStream = client.getInputStream();

                ObjectOutputStream send = new ObjectOutputStream(outputStream);
                ObjectInputStream receiver = new ObjectInputStream(inputStream);

//                for (boolean b : newPermissionsForUser) {
//                    System.out.println(b);
//                }

                send.writeUTF("updatePermissions");

                send.writeObject(newPermissionsForUser);
//                send.writeBoolean(newPermissionsForUser[0]);
//                send.writeBoolean(newPermissionsForUser[1]);
//                send.writeBoolean(newPermissionsForUser[2]);
//                send.writeBoolean(newPermissionsForUser[3]);
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

    static boolean CreateNewUser(String[] userData, ArrayList<String> listOfUsers) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        // We assume the data entered had no missing fields until we find a field that was either null or empty
        boolean wasAllUserDataEnteredCorrect = true;

        if (!listOfUsers.contains(userData[0])) {
            for (String s : userData) {
                if ((s == null || s.isEmpty())) {
                    wasAllUserDataEnteredCorrect = false;

                    // Display the GUI error if the above condition is not a result of the unit test
                    if (!userData[0].equals("ThisIsATestUser")) {
                        DialogWindow.ShowErrorPane("Attempted to create a user with empty fields. Please ensure you fill every field and try again.",
                                "Error: Attempted to create user with missing fields");
                        break;
                    }
                }
            }
            userData[3] = PlaintextToHashedPassword(userData[3]);
        }
         else {
            // Username already exists
            wasAllUserDataEnteredCorrect = false;

            // Display the GUI error if the above condition is not a result of the unit test
            if (!userData[0].equals("ThisIsATestUser")) {
                DialogWindow.ShowErrorPane("Attempted to create a user with a username that already exists. Please choose a different username and try again.",
                        "Error: Attempted to create user with a username that already exists");
            }
        }
        return wasAllUserDataEnteredCorrect;
    }

    static void CreateNewUserToDB(String[] userData, boolean[] userPermissions) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {

        // If data had no missing fields, submit the user creation details to the server
        if (CreateNewUser(userData, GetListOfUsers())) {
            Socket client = ConnectionToServer();

            // connects to the server with information and attempts to get the auth token
            // for the user after successful Login
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
