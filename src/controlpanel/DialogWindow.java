package controlpanel;

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
//        JOptionPane pane = new JOptionPane("Please Change Password", JOptionPane.WA);
//        JDialog dialog = pane.createDialog("Change Password");
//        dialog.setAlwaysOnTop(true);
//        dialog.setVisible(true);

    // User Permissions
    static void showUserPermissions() {
        JOptionPane pane = new JOptionPane("Select User Permissions", JOptionPane.PLAIN_MESSAGE);
        JPanel window2 = new JPanel();
        JDialog dialog = pane.createDialog("User Permissions");
        JCheckBox billboardPermissions = new JCheckBox("Create Billboard");
        JCheckBox editBillboardPermissions = new JCheckBox("Edit Billboards");
        JCheckBox schedulePermissions = new JCheckBox("Schedule Billboards");
        JCheckBox editUserPermissions = new JCheckBox("Edit User Permissions");


        pane.setLayout(new GridLayout(2,1));
        window2.setLayout(new GridLayout(4,1));

        window2.add(billboardPermissions);
        window2.add(editBillboardPermissions);
        window2.add(schedulePermissions);
        window2.add(editUserPermissions);

        pane.add(window2);

        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);


    }

    // User Details

    // Create User

    // Delete User




}
