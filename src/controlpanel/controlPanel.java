package controlpanel;

//import resources.GetPropertyValues;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class controlPanel extends JFrame {

//    private static final boolean CLOSABLE = true;
//    private static final boolean ICONIFIABLE = true;
//    private static final boolean MAXIMIZABLE = true;
//    private static final boolean RESIZABLE = true;
    private static String token;
    public static final int WINDOWWIDTH = 900;
    public static final int WINDOWHEIGHT = 800;
    public JDesktopPane pane = new JDesktopPane();

    public controlPanel() {
        super("Control Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JInternalFrame controlPanelFrameHandler = ControlPanelFrameHandler.frameHandler();
        controlPanelFrameHandler.setVisible(true);
        pane.add(controlPanelFrameHandler);

        getContentPane().add(pane);

        // Display the window.
        setPreferredSize(new Dimension(WINDOWWIDTH, WINDOWHEIGHT));
        setLocation(new Point(500, 0));
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        controlPanel program = new controlPanel();
//        Create and instance of the program
        while (!controller.loginSuccessful) {
//        Allow the program to be seen.
            program.setVisible(true);
        }
        program.dispose();

        program = new controlPanel();
//        Allow the program to be seen.
        program.setVisible(true);
    }
}