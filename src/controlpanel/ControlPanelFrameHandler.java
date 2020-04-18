package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ControlPanelFrameHandler extends JFrame {

    // Main frame handler for displaying the frames in the program
    public static JInternalFrame frameHandler() {
        JInternalFrame currentWindow;

        if (controller.loginSuccessful) {
            currentWindow = billboardFrame(true);
            loginFrame(false);
        } else {
            currentWindow = loginFrame(true);
        }
        return currentWindow;
    }

    public static JInternalFrame loginFrame(boolean status) {
        JInternalFrame loginWindow = login.loginScreen();
        loginWindow.setVisible(status);
        return loginWindow;
    }

    public static JInternalFrame billboardFrame(boolean status) {
        JInternalFrame billboardWindow = listBillboards.getBillboards();
        billboardWindow.setVisible(status);
        return billboardWindow;
    }
}
