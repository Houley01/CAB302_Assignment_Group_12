package controlpanel;

import javax.swing.*;

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
}
