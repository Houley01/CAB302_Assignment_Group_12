package controlpanel;

import javax.naming.spi.ObjectFactoryBuilder;
import javax.swing.*;
import java.awt.*;

public class DialogWindow {
    //    Used to create dialog windows without causing a bug which stops from reading some text fields
    static protected void showErrorPane(String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    static void showInformationPane(String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    static void showYesNoCancelPane(String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.YES_NO_CANCEL_OPTION);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    // Edit Admin Settings
    // Admin Password
    static void showPasswordSettings() {
        JFrame editPassword = new JFrame();
        Object changePassword = JOptionPane.showInputDialog(editPassword, "Change your password:");
        System.out.println(changePassword);
    }

    // User Permissions
    static void showUserPermissions() {
        JOptionPane pane = new JOptionPane("Select User Permissions", JOptionPane.PLAIN_MESSAGE);
        JPanel window2 = new JPanel();
        JDialog dialog = pane.createDialog("User Permissions");
        JCheckBox billboardPermissions = new JCheckBox("Create Billboard");
        JCheckBox editBillboardPermissions = new JCheckBox("Edit Billboards");
        JCheckBox schedulePermissions = new JCheckBox("Schedule Billboards");
        JCheckBox editUserPermissions = new JCheckBox("Edit User Permissions");

        pane.setLayout(new GridLayout(1,1));
        pane.add(window2);
        window2.setLayout(new GridLayout(2,2));
        window2.add(billboardPermissions);
        window2.add(editBillboardPermissions);
        window2.add(schedulePermissions);
        window2.add(editUserPermissions);

        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    // User Details
    static void showUserDetails() {
        JFrame editFirstName = new JFrame();
        Object changeFirstName = JOptionPane.showInputDialog(editFirstName, "Update First Name:");
        JFrame editLastName = new JFrame();
        Object changeLastName = JOptionPane.showInputDialog(editLastName, "Update Last Name:");
        System.out.println(changeFirstName);
        System.out.println(changeLastName);
    }

    // Create User
    static void showCreateUser() {
        JFrame enterFirstName = new JFrame();
        Object inputFirstName = JOptionPane.showInputDialog(enterFirstName, "Enter First Name:");
        JFrame enterLastName = new JFrame();
        Object inputLastName = JOptionPane.showInputDialog(enterLastName, "Enter Last Name:");
        JFrame enterUserName = new JFrame();
        Object inputUserName = JOptionPane.showInputDialog(enterUserName, "Enter UserName");
        JFrame enterPassword = new JFrame();
        Object inputPassword = JOptionPane.showInputDialog(enterPassword, "Enter Password");
        String[] userPermissions = {"Create Billboards", "Edit Billboards", "Schedule Billboards", "Edit Users"};
        String  permissions = (String) JOptionPane.showInputDialog(null, "Select User Permissions:",
                "User Permissions", JOptionPane.QUESTION_MESSAGE, null,
                userPermissions, userPermissions[1]);

        System.out.println(inputFirstName);
        System.out.println(inputLastName);
        System.out.println(inputUserName);
        System.out.println(inputPassword);
        System.out.println(permissions);
    }

    // Delete User
    static void showRemoveUser() {
        JFrame deleteUser = new JFrame();
        String removeUser = JOptionPane.showInputDialog(deleteUser, "Delete a User:");
        System.out.println(removeUser);
    }

}
