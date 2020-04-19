package server;

import server.databaseCreation.databaseCreation;
import server.initialisation.ServerInit;

import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {
    private static Boolean connectionInitiated;

    public static void main(String[] args) throws IOException, SQLException {

        // Gathers the information from server.config file
        resources.GetPropertyValues properties = new resources.GetPropertyValues();
        properties.readPropertyFile();

        connectionInitiated = ServerInit.initaliseConnection();

        if (connectionInitiated) {
            databaseCreation.checkDatabaseExistence();
        }

//        Reads the port number from the server.properties file
        ServerSocket serverSocket =  new ServerSocket(properties.port);

        while(true) {
            Socket server = serverSocket.accept();
            System.out.println("Connected to " + server.getInetAddress());

            ObjectInputStream receiver = new ObjectInputStream(server.getInputStream());
            ObjectOutputStream send = new ObjectOutputStream(server.getOutputStream());


            String request = receiver.readUTF();

//             Username and password
            if (request.equals("Login")) {
                String uname = receiver.readUTF();
                String pass = receiver.readUTF();
//                func check username and database
                send.writeBoolean(checkUserDetails(uname, pass));
                send.flush();
            }
//            Create Billboard Information
            if (request.equals( "CreateBillboards")) {

            }
//            Schedule Billboards
            if (request.equals("ScheduleBillboards")) {

            }
//            List Billboards
            if (request.equals("ListBillboards")) {

            }
//            List Billboards
            if (request.equals("EditUser")) {

            }
//            Viewer Request Billboards
            if (request.equals("ViewerRequest")) {

            }


//      End connections
            receiver.close();
            send.close();
            server.close();
        }

    }

    static boolean checkUserDetails(String uName, String pass) throws SQLException {
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
            if (pass.equals(rs.getString("pass"))) {
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
}
