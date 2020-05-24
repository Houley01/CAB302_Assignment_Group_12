package controlpanel;

import com.sun.source.tree.PackageTree;

import javax.swing.*;
import javax.xml.stream.FactoryConfigurationError;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class usersPage {
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
        JPanel window2 = new JPanel();
        window.setSize(600, 300);
        window2.setSize(300, 150);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);

//      Main Heading - "Edit Account Information"
        JLabel editAccount = new JLabel("Edit Account Information");
        editAccount.setVerticalTextPosition(JLabel.TOP);
        editAccount.setHorizontalTextPosition(JLabel.LEFT);
        editAccount.setFont(controlPanel.titleFont);

//      Button - Administrative User
        JButton userAdmin = new JButton("Administrative User", new ImageIcon("src\\controlpanel\\resources\\admin.png"));
        userAdmin.setBounds(100,100,140,40);

        userAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAdminPreferences();
            }
        });

//      Button - Regular User
        JButton userRegular = new JButton("Regular User", new ImageIcon("src\\controlpanel\\resources\\user.png"));
        userRegular.setBounds(100,100,140,40);

        userRegular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.ShowUserPreferences();
            }
        });

//        Add items to GUI
        window.setLayout(new GridLayout(2,1));
        mainHeading.add(editAccount);
        window.add(mainHeading);
        window.add(window2);
        window2.setLayout(new GridLayout(1,2));
        window2.add(userAdmin);
        window2.add(userRegular);

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
        editAdmin.setFont(controlPanel.titleFont);

        mainHeading.add(editAdmin);

        JPanel adminSettings = new JPanel();

//        Heading - "Change Password"
        JButton adminPassword = new JButton("Change Password");
        adminPassword.setBounds(100,100,140,40);
        adminPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogWindow.showPasswordSettings();
            }
        });

//        Heading - "User Permissions"
        JButton userPermission = new JButton("User Permissions");
        userPermission.setBounds(100,100,140,40);
        userPermission.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogWindow.showUserPermissions();
            }
        });

//        Heading - "Edit User Details"
        JButton editUserDetails = new JButton("Edit User Details");
        editUserDetails.setBounds(100,100,140,40);
        editUserDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogWindow.showUserDetails();
            }
        });

//        Heading - "Create User"
        JButton createUser = new JButton("Create New User");
        createUser.setBounds(100,100,140,40);
        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogWindow.showCreateUser();
            }
        });

//        Heading - "Delete User"
        JButton deleteUser = new JButton("Delete a User");
        deleteUser.setBounds(100,100,140,40);
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogWindow.showRemoveUser();
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

    static JInternalFrame userWindow = new JInternalFrame( "User Preferences", false, false, true);

    /**
     * User settings.
     * Boils down to a change password page. Lets the user save their new password
     * or cancel and retain their <i>old</i> password
     *
     * @return window       JFrame window object with configuration settings
     */
    public static JInternalFrame UserEditUserWindow() {
        userWindow.setSize(400,125);
        // INSERT CONTENT HERE

        //        Heading - "User Settings"
        JPanel mainHeading = new JPanel();
        JLabel editUser = new JLabel("User Settings");
        editUser.setVerticalTextPosition(JLabel.TOP);
        editUser.setHorizontalTextPosition(JLabel.LEFT);
        editUser.setFont(controlPanel.titleFont);

        mainHeading.add(editUser);

        JPanel userSettings = new JPanel();

        //        Heading - "Change Password"
        JLabel userPassword = new JLabel("Change Password");
        userPassword.setVerticalTextPosition(JLabel.TOP);
        userPassword.setHorizontalTextPosition(JLabel.LEFT);
        JTextField newPassword = new JTextField();
        JButton saveChanges = new JButton("Save");
        JButton cancelChanges = new JButton("Cancel");

        userWindow.setLayout(new GridLayout(2, 2));
        userWindow.add(editUser);
        userWindow.add(userSettings);
        userSettings.setLayout(new GridLayout(2,2));
        userSettings.add(userPassword);
        userSettings.add(newPassword);
        userSettings.add(saveChanges);
        userSettings.add(cancelChanges);

        return userWindow;
    }


}
