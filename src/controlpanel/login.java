package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
<<<<<<< Updated upstream
class login extends JFrame implements ActionListener
{
    JButton loginButton;
    JPanel loginWindow;
    JLabel usernameLabel, passwordLabel;
    final JTextField  usernameText, passwordText;
    login()
    {
//       Username
        usernameLabel = new JLabel("Username:");
        usernameText = new JTextField(15);
//      Password
        passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField(15); // Hides the Password from the screen


        loginButton = new JButton("Login");
//      Set window to Grid layout
        loginWindow = new JPanel(new GridLayout(3,2));
//      Add items to the window
        loginWindow.add(usernameLabel);
        loginWindow.add(usernameText);
        loginWindow.add(passwordLabel);
        loginWindow.add(passwordText);
        loginWindow.add(loginButton);
        add(loginWindow,BorderLayout.CENTER);
        loginButton.addActionListener(this);
        setTitle("Login Screen");
    }


    public void actionPerformed(ActionEvent e) {

        String uname = usernameText.getText();
        String pass = passwordText.getText();

        // TEMP FUNCTIONS WILL BE REMOVED and replaced with one from the backend functions
        if (uname.equals("john1") && pass.equals("Password.1")) {
//            Set user access level
            JOptionPane.showMessageDialog(this, "Successful Login",
                    "Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect Login Informations",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(uname);
            System.out.println(pass);

        }

//      REAL FUNCTION with temp place Holders
        /*      Temp function for sending the username and password to the server application */
//        if (sendUsernameAndpassword(uname, hashfunction(pass).equals(true))) {
//            Set user access level
//        } else {
//            JOptionPane.showMessageDialog(this, "Incorrect Login Informations",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//        }


=======

class login extends JFrame
{
    private static JPasswordField passwordText;
    final JTextField  usernameText;

    login(JTextField usernameText) {
        this.usernameText = usernameText;
    }



    public static JInternalFrame loginScreen() {
        JInternalFrame window = new JInternalFrame( "Login Screen");
        window.setSize(250, 100);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 125, (controlPanel.WINDOWHIGHT/2) - 100);
        window.setLayout(new GridLayout(3,2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameText = new JTextField(15);
        //      Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField(15); // Hides the Password from the screen


        JButton loginButton = new JButton("Login");
        //      Set window to Grid layout
        loginButton.setPreferredSize(new Dimension(25,25));


        //      Add items to the window
        window.add(usernameLabel);
        window.add(usernameText);
        window.add(passwordLabel);
        window.add(passwordText);
        window.add(loginButton);



        //      Call when the login button is clicked
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = usernameText.getText();
                String pass = passwordText.getText();
//              Add Hash function here
                controller.sendLoginInfo(user, pass);
            }
        });
        return window;
>>>>>>> Stashed changes

    }
}


