package controlpanel;

import javax.swing.*;
import java.awt.*;


public class controlPanel extends JFrame {

//    private static final boolean CLOSABLE = true;
//    private static final boolean ICONIFIABLE = true;
//    private static final boolean MAXIMIZABLE = true;
//    private static final boolean RESIZABLE = true;
    public static final int WINDOWWIDTH = 900;
    public static final int WINDOWHIGHT = 800;

    public controlPanel() {
        super("Control Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JDesktopPane pane = new JDesktopPane();

        /*
         *
         * Calls the login window setup.
         * Height and Width are setup in the login class
         * Location set in login class.
         */
        JInternalFrame loginWindow = login.loginScreen();
        loginWindow.setVisible(true);

        pane.add(loginWindow);


        getContentPane().add(pane);

        // Display the window.
        setPreferredSize(new Dimension(WINDOWWIDTH, WINDOWHIGHT));
        setLocation(new Point(500, 0));
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
//        Create and instance of the program
        controlPanel program = new controlPanel();
//        Allow the program to be seen.
        program.setVisible(true);
    }
}