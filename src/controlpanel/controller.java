package controlpanel;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class controller {

    public static boolean loginSuccessful = false;
    private static String loggedInUser;
    private static String token;

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


    /*
     *  Connection to Server Creating and editing billboards
     *  @parm urlOrImage (-1 == none), (0 == url), (1 == image)
     *
     */
    static void createBillboard(String billboardName, String text, String textColour, String backgroundColour, int urlOrImage, String image) throws IOException {

        Socket client = connectionToServer();
        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);


        send.writeUTF("CreateBillboards");
        send.flush();

        send.writeUTF(loggedInUser);
        send.writeUTF(token);
        send.writeInt(urlOrImage);
        if (urlOrImage != -1 ) {
            if (urlOrImage == 1) {
                image = CreateMD5(image);
            }
            send.writeUTF(image);
        }
        send.flush();

        int val = receiver.read();
        if (val == 1) {
            send.writeUTF(billboardName);
            send.writeUTF(text);
            send.writeUTF(textColour);
            send.writeUTF(backgroundColour);
//
            send.flush(); // Must be done before switching to reading state
            System.out.println(receiver.readUTF());
        } else {

        }
//      End connections
        send.close();
        receiver.close();
        client.close();
    }

    /*
     *  Connection to Server Editing User
     *
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

    /*
     *  Sends the username and password to server
     */
    static boolean authenticateUserLogin(String username, String password) throws IOException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("user: " + username); // Debugging use
        System.out.println("Password: " + password); // Debugging use

        String hashedPassword = plaintextToHashedPassword(password);

        Socket client = connectionToServer();

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

    // Returns a hash of the entered password by the user to be sent to the server over the network
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

    public static void hideLoginScreen() {
        login.window.setVisible(false);
        ControlPanelFrameHandler.bar.setVisible(true);
    }

    public static void showCreateBillboard() {
        if (createBillboards.window.isVisible() == true) {
            createBillboards.window.setVisible(false);
        } else if (createBillboards.window.isVisible() == false) {
            createBillboards.window.setVisible(true);
        }
    }

    public static void showListBillboard() {
        if (listBillboards.window.isVisible() == true) {
            listBillboards.window.setVisible(false);
        } else if (listBillboards.window.isVisible() == false) {
            listBillboards.window.setVisible(true);
        }
    }

    public static void showSchedule() {
        if (scheduleBillboards.window.isVisible() == true) {
            scheduleBillboards.window.setVisible(false);
        } else if (scheduleBillboards.window.isVisible() == false) {
            scheduleBillboards.window.setVisible(true);
        }
    }

    public static void showEditUser() {
        if (usersPage.window.isVisible() == true) {
            usersPage.window.setVisible(false);
        } else if (usersPage.window.isVisible() == false) {
            usersPage.window.setVisible(true);
        }
    }

    //      Open Admin Preferences
    public static void showAdminPreferences() {
        if(usersPage.adminWindow.isVisible() == true) {
            usersPage.adminWindow.setVisible(false);
        } else if (usersPage.adminWindow.isVisible() == false) {
            usersPage.adminWindow.setVisible(true);
        }
    }

    //      Open User Preferences
    public static void ShowUserPreferences() {
        if(usersPage.userWindow.isVisible() == true) {
            usersPage.userWindow.setVisible(false);
        } else if (usersPage.userWindow.isVisible() == false) {
            usersPage.userWindow.setVisible(true);
        }
    }


    public static void showHelpScreen() {
        // TEMP FUNC
        System.out.println("HELP INFO");
        // if (helpScreen.window.isVisible() == true) {
        //     helpScreen.window.setVisible(false);
        // } else if (helpScreen.window.isVisible() == false) {
        //     helpScreen.window.setVisible(true);
        // }
    }

    public static String CreateMD5(String imagePath) {
        String base64Image = "";
        File file = new File(imagePath);
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

    public static void logout() {
//       EXIT FOR THE MOMENT WILL CHANGE to login page
        System.exit(0);
    }

}
