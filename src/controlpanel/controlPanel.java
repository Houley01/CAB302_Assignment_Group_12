package controlpanel;

//import resources.GetPropertyValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class controlPanel extends JFrame {

//    private static final boolean CLOSABLE = true;
//    private static final boolean ICONIFIABLE = true;
//    private static final boolean MAXIMIZABLE = true;
//    private static final boolean RESIZABLE = true;
    public static final int WINDOWWIDTH = 900;
    public static final int WINDOWHEIGHT = 800;
//    public JDesktopPane pane = new JDesktopPane();
//    private static JPasswordField passwordText;
//    private static JTextField  usernameText;

    public static void main(String[] args) throws IOException {
        new ControlPanelFrameHandler();
    }
}