package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


class Login extends JFrame
{
    public static JPasswordField passwordText;
    public static JTextField  usernameText = new JTextField();

    Login(JTextField usernameText) {
        this.usernameText = usernameText;
    }

    static JInternalFrame window = new JInternalFrame( "Login Screen");

    /**
     * Login window. First window you see when you open the application for the control panel.
     * Authentication for users, username and password is done in the Controller.
     * @see Controller
     *
     * @since               JDK13
     * @return window       JFrame object containing configurations and elements created.
     */
    public static JInternalFrame loginScreen() {
        window.setSize(250, 100);
        window.setLocation((ControlPanel.WINDOWWIDTH/2) - 125, (ControlPanel.WINDOWHEIGHT/2) - 100);
        window.setLayout(new GridLayout(3,2));

        JLabel usernameLabel = new JLabel("Username:");

        usernameText = new JTextField(15);
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



        //      Call when the Login button is clicked
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = usernameText.getText();
                String pass = passwordText.getText();
                if (!user.equals("") && !pass.equals("")) {
                    try {
                        if (Controller.AuthenticateUserLogin(user, pass)) {
                            Controller.HideLoginScreen();
                        }
                        ;
                    } catch (IOException | ClassNotFoundException | InvalidKeySpecException | NoSuchAlgorithmException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    DialogWindow.ShowInformationPane("Please put a username and password in the textbox", "Not enough Information");
                }
            }
        });
        return window;

    }

}


