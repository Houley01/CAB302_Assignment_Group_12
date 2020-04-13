package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

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
                try {
                    controller.sendLoginInfo(user, pass);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return window;

    }
}


