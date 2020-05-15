package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

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

//    /*
//     *  Connection to Server Login
//     *  @para user == username
//     *  @para password is an array of Char values
//     */
//    static void authenticateUserLogin(Socket client, String username, String password) throws IOException {
//            OutputStream outputStream = client.getOutputStream();
//
//            System.out.println(outputStream);
//
//            InputStream inputStream = client.getInputStream();
//
//            ObjectOutputStream send = new ObjectOutputStream(outputStream);
//            ObjectInputStream receiver = new ObjectInputStream(inputStream);
//
////        Send the server which function it needs to run
//            send.writeUTF("Login");
//            send.writeUTF(username);
//            send.writeUTF(password);
//            send.flush(); // Must be done before switching to reading state
//
//
//            // If the server send back a true or false message decides what to do.
//            if (receiver.readBoolean() == true) {
//                System.out.println("correct username and password"); // DEBUG CODE
//                DialogWindow.showInformationPane("Successful Login", "Success");
//                loginSuccessful = true;
//                ControlPanelFrameHandler.billboardFrame(true);
//                ControlPanelFrameHandler.loginFrame(false);
//            } else {
//                // Alert the user that they have incorrectly entered they username or password with
//                // a pop up window.
//                System.out.println("wrong username or password"); // DEBUG CODE
//                // IMPLEMENT AN ERROR MESSAGE FOR USER
//                DialogWindow.showErrorPane("Incorrect Login Information", "Error");
//                loginSuccessful = false;
//            }
//
////      End connections
//            send.close();
//            receiver.close();
//            client.close();
//    }

    /*
     *  Connection to Server Creating and editing billboards
     *
     */
    static void createBillboard(Socket client, String BillboardName, String text, String background, int userID) throws IOException {

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
    static boolean authenticateUserLogin(String username, String password) throws IOException, ClassNotFoundException {
        System.out.println("user: " + username); // Debugging use
        System.out.println("Password: " + password); // Debugging use

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
            send.writeUTF(password);
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

    public static void logout() {
//       EXIT FOR THE MOMENT WILL CHANGE to login page
        System.exit(0);
    }

}
