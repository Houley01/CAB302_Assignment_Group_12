package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


class login extends JFrame
{
    private static JPasswordField passwordText;
    final JTextField  usernameText;

    login(JTextField usernameText) {
        this.usernameText = usernameText;
    }

    static JInternalFrame window = new JInternalFrame( "Login Screen");

    /**
     * Login window. First window you see when you open the application for the control panel.
     * Authentication for users, username and password is done in the controller.
     * @see controller
     *
     * @deprecated          .getText()
     * @since               JDK13
     * @return window       JFrame object containing configurations and elements created.
     */
    public static JInternalFrame loginScreen() {
        window.setSize(250, 100);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 125, (controlPanel.WINDOWHEIGHT/2) - 100);
        window.setLayout(new GridLayout(3,2));

        JLabel usernameLabel = new JLabel("Username:");
        //        REMOVE PASSWORD........................\/  USED TO FILL IN TEXT FIELD
        JTextField usernameText = new JTextField("admin", 15);
        //      Password
        JLabel passwordLabel = new JLabel("Password:");
        //        REMOVE PASSWORD...................\/  USED TO FILL IN TEXT FIELD
        passwordText = new JPasswordField("password", 15); // Hides the Password from the screen


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
                try {
                    if(controller.authenticateUserLogin(user, pass)) {
//                        window.setVisible(false);
//                        listBillboards.listBillboards().setVisible(true);
                        controller.hideLoginScreen();
                    };
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (InvalidKeySpecException ex) {
                    ex.printStackTrace();
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return window;

    }

}


