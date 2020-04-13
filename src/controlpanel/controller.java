package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class controller {
    /*
     *  Connection to Server Login
     *  @para user == username
     *  @para password is an array of Char values
     */
    private static void connectionToSever(String username, String pass) throws IOException {
        //      Gathers the information from server.config file
        resources.GetPropertyValues properties = new resources.GetPropertyValues();
        properties.readPropertyFile();

//        create a socket connect
        Socket client = new Socket(properties.serverName,  properties.port);

        OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream();

        ObjectOutputStream send = new ObjectOutputStream(outputStream);
        ObjectInputStream receiver = new ObjectInputStream(inputStream);

//        Send the server which function it needs to run
        send.writeUTF("Login");
        send.writeUTF(username);
        send.writeUTF(pass);
        send.flush(); // Must be done before switching to reading state

//        If the server send back a true or false message decides what to do.
        if (receiver.readBoolean() == true) {
            System.out.println("correct username and password"); // DEBUG CODE

        } else {
//          Alert the user that they have incorrectly entered they username or password with
//          a pop up window.
            System.out.println("wrong username or password"); // DEBUG CODE
// IMPLEMENT AN ERROR MESSAGE FOR USER
            DialogWindow.showErrorPane("Incorrect Login Information", "Error");

//            JOptionPane.showMessageDialog(login.loginScreen(), "Incorrect Login Information",
//                    "Error", JOptionPane.ERROR_MESSAGE);
        }

//      End connections
        send.close();
        receiver.close();
        client.close();
    }

    /*
     *  Connection to Server Creating and editing billboards
     *
     */
    static void connectionToSever(String BillboardName, String text, String background, int userID) throws IOException {
        //      Gathers the information from server.config file
        resources.GetPropertyValues properties = new resources.GetPropertyValues();
        properties.readPropertyFile();


//        create a socket connect
        Socket client = new Socket(properties.serverName,  properties.port);

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
    static void connectionToSever(String username, String password, String fName, String lName) throws IOException {
        //      Gathers the information from server.config file
        resources.GetPropertyValues properties = new resources.GetPropertyValues();
        properties.readPropertyFile();


//        create a socket connect
        Socket client = new Socket(properties.serverName,  properties.port);

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
    static void sendLoginInfo(String user, String password) throws IOException {
        System.out.println("user: " + user); // Debugging use
        System.out.println("Password: " + password); // Debugging use
        connectionToSever(user, password); // connects to the server with information
        password = new String(); // Used to clear the password string (Not very secure though)

    }
}
