package controlpanel;

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
    public static void ShowErrorPane(String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    /**
     * Developer defined successes
     *
     * @param message   Success message to inform user of a successful request to the server.
     * @param title     Title of window pane.
     */
    static protected void showSuccessPane(String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
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
    static void ShowInformationPane(String message, String title) {
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
     * @return int      1 Means No, 0 Means Yes
     */
    static int ShowYesNoPane(String message, String title) {
        return JOptionPane.showConfirmDialog(null,message, title, JOptionPane.YES_NO_OPTION);
    }

    static void ShowPasswordSettingsRegularUser() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        JFrame editPassword = new JFrame();
        String selectedUser = Controller.loggedInUser;
        Object changePassword = JOptionPane.showInputDialog(editPassword, "Enter new password:");
        Object changePasswordConfirm = JOptionPane.showInputDialog(editPassword, "Confirm new password:");

        String newPassword = (String) changePassword;
        String newPasswordConfirm = (String) changePasswordConfirm;

        // Checking that the new password and it's confirmed entry match
        if (changePassword.equals(changePasswordConfirm) && newPassword != null && !newPassword.isEmpty() && newPasswordConfirm != null && !newPasswordConfirm.isEmpty()) {
            Controller.ChangePassword(selectedUser, (String) changePasswordConfirm);
        } else {
            DialogWindow.ShowErrorPane("Passwords don't match or field(s) were empty. Please try again.", "Error");
        }
    }

    // Edit Admin Settings
    // Admin Password

    /**
     *  Edit password window. Changes the users current password
     *  to the newly entered password.
     */
    static void ShowPasswordSettings() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        JFrame editPassword = new JFrame();
        String selectedUser = GetListOfUsers();
        Object changePassword = JOptionPane.showInputDialog(editPassword, "Enter new password:");
        Object changePasswordConfirm = JOptionPane.showInputDialog(editPassword, "Confirm new password:");

        String newPassword = (String) changePassword;
        String newPasswordConfirm = (String) changePasswordConfirm;

        // Checking that the new password and it's confirmed entry match
        if (changePassword.equals(changePasswordConfirm) && newPassword != null && !newPassword.isEmpty() && newPasswordConfirm != null && !newPasswordConfirm.isEmpty()) {
            Controller.ChangePassword(selectedUser, (String) changePasswordConfirm);
        } else {
            DialogWindow.ShowErrorPane("Passwords don't match or field(s) were empty. Please try again.", "Error");
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
     */
    static void ShowUserPermissions() throws IOException, ClassNotFoundException {

        String selectedUser = GetListOfUsers();
        boolean[] userPermissions = Controller.GetUserPermissions(selectedUser);
        if (!Controller.loggedInUser.equals("")) {
            JOptionPane pane = new JOptionPane("Select User Permissions", JOptionPane.PLAIN_MESSAGE);
            JPanel window2 = new JPanel();
            JDialog dialog = pane.createDialog("User Permissions");
            dialog.setSize(new Dimension(400, 200));
            JCheckBox billboardPermissions = new JCheckBox("Create Billboard", userPermissions[0]);
            JCheckBox editBillboardPermissions = new JCheckBox("Edit Billboards", userPermissions[1]);
            JCheckBox schedulePermissions = new JCheckBox("Schedule Billboards", userPermissions[2]);
            JCheckBox editUserPermissions = new JCheckBox("Edit User Permissions", userPermissions[3]);

            pane.setLayout(new GridLayout(3,1));
            pane.add(window2);
            window2.setLayout(new GridLayout(2,2));
            window2.add(billboardPermissions);
            window2.add(editBillboardPermissions);
            window2.add(schedulePermissions);
            window2.add(editUserPermissions);

            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            boolean[] updatedPermissions = {billboardPermissions.isSelected(), editBillboardPermissions.isSelected(), schedulePermissions.isSelected(), editUserPermissions.isSelected()};

            Controller.UpdateUserPermissionsToDB(userPermissions, updatedPermissions, selectedUser);
        }
    }


    // User Details

    /**
     *  User details window pane.
     *  Opens two other windows which the user inputs their first and last names accordingly.
     */
    static void ShowUserDetails() throws IOException, ClassNotFoundException {
        String selectedUser = GetListOfUsers();

        String[] userInfo = Controller.GetUserInfo(selectedUser);

        if (!Controller.loggedInUser.equals("")) {
            JFrame editFirstName = new JFrame();
            Object changeFirstName = JOptionPane.showInputDialog(editFirstName, "Update First Name:", userInfo[0]);
            JFrame editLastName = new JFrame();
            Object changeLastName = JOptionPane.showInputDialog(editLastName, "Update Last Name:", userInfo[1]);
            Controller.UpdateUserDetails(selectedUser, (String) changeFirstName, (String) changeLastName);
        }


    }
    /**
     * Developer defined errors (e.g. inability to connect to server).
     *
     * @param accessing   For letting the user know what task they can not access
     */

    static protected void NoAccessTo(String accessing) {
        JOptionPane pane = new JOptionPane("You do not have access to the " + accessing + ".\nPlease contact your IT Service Team", JOptionPane.ERROR_MESSAGE);
        JDialog dialog = pane.createDialog("Permission Error");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
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
    static void ShowCreateUser() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        JFrame enterFirstName = new JFrame();
        Object inputFirstName = JOptionPane.showInputDialog(enterFirstName, "Enter First Name:");
        JFrame enterLastName = new JFrame();
        Object inputLastName = JOptionPane.showInputDialog(enterLastName, "Enter Last Name:");
        JFrame enterUserName = new JFrame();
        Object inputUserName = JOptionPane.showInputDialog(enterUserName, "Enter UserName");
        JFrame enterPassword = new JFrame();
        Object inputPassword = JOptionPane.showInputDialog(enterPassword, "Enter Password");

        JOptionPane pane = new JOptionPane("Select User Permissions", JOptionPane.PLAIN_MESSAGE);
        JPanel window2 = new JPanel();
        JDialog dialog = pane.createDialog("User Permissions");
        dialog.setSize(new Dimension(400, 200));
        JCheckBox billboardPermissions = new JCheckBox("Create Billboard");
        JCheckBox editBillboardPermissions = new JCheckBox("Edit Billboards");
        JCheckBox schedulePermissions = new JCheckBox("Schedule Billboards");
        JCheckBox editUserPermissions = new JCheckBox("Edit User Permissions");

        pane.setLayout(new GridLayout(3,1));
        pane.add(window2);
        window2.setLayout(new GridLayout(2,2));
        window2.add(billboardPermissions);
        window2.add(editBillboardPermissions);
        window2.add(schedulePermissions);
        window2.add(editUserPermissions);

        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        // Holds the information on the user to be passed to the Server
        String[] userData = {(String) inputUserName, (String) inputFirstName,
                            (String) inputLastName, (String) inputPassword};

        // Holds the permissions for the user
        boolean[] userPermissions = {billboardPermissions.isSelected(), editBillboardPermissions.isSelected(),
                                        schedulePermissions.isSelected(), editUserPermissions.isSelected()};

        Controller.CreateNewUserToDB(userData, userPermissions);
    }

    // Delete User

    /**
     * <h1>Delete user from system.</h1>
     * User inputs a <i>username</i> to remove from the billboard system.
     */
    static void ShowRemoveUser() throws IOException, ClassNotFoundException {
        String selectedUser = GetListOfUsers();
        Controller.DeleteUserFromDB(Controller.GetListOfUsers(), selectedUser);
    }

    static String GetListOfUsers() throws IOException, ClassNotFoundException {
        String[] listOfUsers = Controller.GetListOfUsers().toArray(new String[0]);
        String  selectedUser = (String) JOptionPane.showInputDialog(null, "Select User",
                "Select User", JOptionPane.QUESTION_MESSAGE, null,
                listOfUsers, listOfUsers[0]);

        return selectedUser;
    }
}
