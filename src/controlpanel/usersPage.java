package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class usersPage {
    static JInternalFrame window = new JInternalFrame( "Edit User Information", false, false, true);
    public static JInternalFrame userPage() {
        JPanel mainHeading = new JPanel();
        JPanel window2 = new JPanel();
        window.setSize(600, 300);
        window2.setSize(300, 150);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);

//        Heading - "Edit Account Information"
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
    public static JInternalFrame AdminEditUserWindow() {
        adminWindow.setSize(300,400);
        // INSERT CONTENT HERE

        //        Heading - "Administrative User Settings"
        JLabel editAdmin = new JLabel("Administrative User Settings");
        editAdmin.setVerticalTextPosition(JLabel.TOP);
        editAdmin.setHorizontalTextPosition(JLabel.LEFT);
        editAdmin.setFont(controlPanel.titleFont);

//        Heading - "Change Password"
        JLabel adminPassword = new JLabel("Change Password");
        adminPassword.setVerticalTextPosition(JLabel.TOP);
        adminPassword.setHorizontalTextPosition(JLabel.LEFT);

//        Heading - "User Permissions"
        JLabel userPermission = new JLabel("User Permissions");
        userPermission.setVerticalTextPosition(JLabel.TOP);
        userPermission.setHorizontalTextPosition(JLabel.LEFT);

//        Heading - "Edit User Details"
        JLabel editUserDetails = new JLabel("Edit User Details");
        editUserDetails.setVerticalTextPosition(JLabel.TOP);
        editUserDetails.setHorizontalTextPosition(JLabel.LEFT);

//        Heading - "Create User"
        JLabel createUser = new JLabel("Create New User");
        createUser.setVerticalTextPosition(JLabel.TOP);
        createUser.setHorizontalTextPosition(JLabel.LEFT);

//        Heading - "Delete User"
        JLabel deleteUser = new JLabel("Delete a User");
        deleteUser.setVerticalTextPosition(JLabel.TOP);
        deleteUser.setHorizontalTextPosition(JLabel.LEFT);

        adminWindow.setLayout(new GridLayout(6,2));
        adminWindow.add(editAdmin);
        adminWindow.add(adminPassword);
        adminWindow.add(userPermission);
        adminWindow.add(editUserDetails);
        adminWindow.add(createUser);
        adminWindow.add(deleteUser);

        return adminWindow;
    }

    static JInternalFrame userWindow = new JInternalFrame( "User Preferences", false, false, true);

    public static JInternalFrame UserEditUserWindow() {
        userWindow.setSize(300,400);
        // INSERT CONTENT HERE

        //        Heading - "User Settings"
        JLabel editUser = new JLabel("User Settings");
        editUser.setVerticalTextPosition(JLabel.TOP);
        editUser.setHorizontalTextPosition(JLabel.LEFT);
        editUser.setFont(controlPanel.titleFont);

        //        Heading - "Change Password"
        JLabel userPassword = new JLabel("Change Password");
        userPassword.setVerticalTextPosition(JLabel.TOP);
        userPassword.setHorizontalTextPosition(JLabel.LEFT);

        userWindow.setLayout(new GridLayout(2, 2));
        userWindow.add(editUser);
        userWindow.add(userPassword);

        return userWindow;
    }


}
