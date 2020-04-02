package controlpanel;

import javax.swing.*;

public class controller {
    /*
    *  @para user == username
    *  @para password is an array of Char values
     */
    static void sendLoginInfo(String user, String password) {

        System.out.println("user: " + user); // Debugging use
        System.out.println("Password: " + password); // Debugging use


        // TEMP FUNCTIONS WILL BE REMOVED and replaced with one from the backend functions
        if (user.equals("test") && password.equals("password")) {
//            Set user access level
            JOptionPane.showMessageDialog(login.loginScreen(), "Successful Login",
                    "Successful", JOptionPane.INFORMATION_MESSAGE);
            password = new String(); // Used to clear the password string (Not very secure though)

        } else {
            JOptionPane.showMessageDialog(login.loginScreen(), "Incorrect Login Information",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
