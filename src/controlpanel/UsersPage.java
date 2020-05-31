package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UsersPage {
    static JInternalFrame window = new JInternalFrame( "Edit User Information", false, false, true);

    /**
     * Popup window for when the user clicks on <i>Edit User Settings</i>.
     * This includes the handlers for when a user clicks on the buttons as well as
     * containing 2 buttons and icons accompanying the following options:
     * <ul>
     *     <li>Administrative User</li>
     *     <li>Regular User</li>
     * </ul>
     *
     * @return window       JFrame window object with configuration settings
     */
    public static JInternalFrame userPage() {
        JPanel mainHeading = new JPanel();
        JPanel buttonPanel = new JPanel();
        window.setSize(600, 300);
        buttonPanel.setSize(300, 150);
        window.setLocation((ControlPanel.WINDOWWIDTH/2) - 300, (ControlPanel.WINDOWHEIGHT/2) - 200);

//      Main Heading - "Edit Account Information"
        JLabel editAccount = new JLabel("Edit Account Information");
        editAccount.setVerticalTextPosition(JLabel.TOP);
        editAccount.setHorizontalTextPosition(JLabel.LEFT);
        editAccount.setFont(ControlPanel.titleFont);

//      Button - Administrative User
        JButton userAdmin = new JButton("Administrative User", new ImageIcon("src/controlpanel/resources/admin.png"));
        userAdmin.setBounds(100,100,140,40);

        userAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.ShowAdminPreferences();
            }
        });

//      Button - Regular User
        JButton userRegular = new JButton("Regular User", new ImageIcon("src/controlpanel/resources/user.png"));
        userRegular.setBounds(100,100,140,40);

        userRegular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DialogWindow.ShowPasswordSettingsRegularUser();
                } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

//        Add items to GUI
        window.setLayout(new GridLayout(2,1));
        mainHeading.add(editAccount);
        window.add(mainHeading);
        window.add(buttonPanel);
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.add(userAdmin);
        buttonPanel.add(userRegular);

        return window;
    }

    static JInternalFrame adminWindow = new JInternalFrame( "Admin Preferences", false, false, true);

    /**
     * Admin user settings.
     * Allows the admin user to modify a large amount of data that's not
     * limited to: Password settings, user permissions, user details,
     * creating a new user and deleting a user.
     * The following settings have buttons correlating to handlers which open
     * windows for user input.
     *
     * @return window       JFrame window object with configuration settings
     */
    public static JInternalFrame AdminEditUserWindow() {
        adminWindow.setSize(300,400);
        // INSERT CONTENT HERE

        //        Heading - "Administrative User Settings"
        JPanel mainHeading = new JPanel();
        JLabel editAdmin = new JLabel("Administrative User Settings");
        editAdmin.setVerticalTextPosition(JLabel.TOP);
        editAdmin.setHorizontalTextPosition(JLabel.LEFT);
        editAdmin.setFont(ControlPanel.titleFont);

        mainHeading.add(editAdmin);

        JPanel adminSettings = new JPanel();

//        Heading - "Change Password"
        JButton adminPassword = new JButton("Change Password");
        adminPassword.setBounds(100,100,140,40);
        adminPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DialogWindow.ShowPasswordSettings();
                } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

//        Heading - "User Permissions"
        JButton userPermission = new JButton("User Permissions");
        userPermission.setBounds(500,100,140,40);
        userPermission.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DialogWindow.ShowUserPermissions();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

//        Heading - "Edit User Details"
        JButton editUserDetails = new JButton("Edit User Details");
        editUserDetails.setBounds(100,100,140,40);
        editUserDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DialogWindow.ShowUserDetails();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

//        Heading - "Create User"
        JButton createUser = new JButton("Create New User");
        createUser.setBounds(100,100,140,40);
        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DialogWindow.ShowCreateUser();
                } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

//        Heading - "Delete User"
        JButton deleteUser = new JButton("Delete a User");
        deleteUser.setBounds(100,100,140,40);
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DialogWindow.ShowRemoveUser();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        adminWindow.setLayout(new GridLayout(2,1));
        adminWindow.add(editAdmin);
        adminWindow.add(adminSettings);
        adminSettings.setLayout(new GridLayout(6,2));
        adminSettings.add(adminPassword);
        adminSettings.add(userPermission);
        adminSettings.add(editUserDetails);
        adminSettings.add(createUser);
        adminSettings.add(deleteUser);

        return adminWindow;
    }
}
