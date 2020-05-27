package controlpanel;

import javax.naming.spi.ObjectFactoryBuilder;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *  Window to create dialog windows without causing bugs that cause non-readable text fields
 */
public class DialogWindow {
    /**
     * Developer defined errors (e.g. inability to connect to server).
     *
     * @param message   Error message to inform user of an error occurring.
     * @param title     Title of window pane.
     */
    static protected void showErrorPane(String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    /**
     *  Information pane for showing user basic information.
     *
     * @param message   Message to inform user(s) of.
     * @param title     Title of window pane.
     */
    static void showInformationPane(String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    /**
     *  Accept or decline window pane for licence agreements, ect.
     *
     * @param message   Message to detail what the user is to accept or decline.
     * @param title     Title of window pane.
     */
    static void showYesNoCancelPane(String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.YES_NO_CANCEL_OPTION);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    // Edit Admin Settings
    // Admin Password

    /**
     *  Edit password window. Changes the users current password
     *  to the newly entered password.
     */
    static void showPasswordSettings() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        JFrame editPassword = new JFrame();
        Object changePassword = JOptionPane.showInputDialog(editPassword, "Enter new password:");
        Object changePasswordConfirm = JOptionPane.showInputDialog(editPassword, "Confirm new password:");

        System.out.println(changePassword);
        System.out.println(changePasswordConfirm);

        if (changePassword.equals(changePasswordConfirm)) {
            controller.changePassword((String) changePasswordConfirm);
        } else {
            DialogWindow.showErrorPane("Passwords don't match. Please try again.", "Error");
            System.out.println("Passwords didn't match");
        }
    }

    // User Permissions

    /**
     *  Show and edit current users permissions.
     *  Shows 4 checkboxes relating to:
     *  <ul>
     *      <li>Create billboards</li>
     *      <li>Edit billboards</li>
     *      <li>Schedule billboards</li>
     *      <li>Edit user permissions</li>
     *  </ul>
     * @see Administrative user settings panel
     */
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

    /**
     *  User details window pane.
     *  Opens two other windows which the user inputs their first and last names accordingly.
     */
    static void showUserDetails() {
        JFrame editFirstName = new JFrame();
        Object changeFirstName = JOptionPane.showInputDialog(editFirstName, "Update First Name:");
        JFrame editLastName = new JFrame();
        Object changeLastName = JOptionPane.showInputDialog(editLastName, "Update Last Name:");
        System.out.println(changeFirstName);
        System.out.println(changeLastName);
    }

    // Create User

    /**
     *  Creating a new user for the system. Opens multiple of <i>small</i> dialog windows
     *  for user to input information accordingly.
     * <h4>User options</h4>
     * <ul>
     *     <li>First name</li>
     *     <li>Last name</li>
     *     <li>Username</li>
     *     <li>Password</li>
     *     <li>User permissions</li>
     *     <ul>
     *         <li>Create billboards</li>
     *         <li>Edit billboards</li>
     *         <li>Schedule billboards</li>
     *         <li>Edit Users</li>
     *     </ul>
     * </ul>
     *
     *  <b>Note</b>: Why? Why is it like this? - Freeman
     */
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

    /**
     * <h1>Delete user from system.</h1>
     * User inputs a <i>username</i> to remove from the billboard system.
     */
    static void showRemoveUser() {
        JFrame deleteUser = new JFrame();
        String removeUser = JOptionPane.showInputDialog(deleteUser, "Delete a User:");
        System.out.println(removeUser);
    }

}
