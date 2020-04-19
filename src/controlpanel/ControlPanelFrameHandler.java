package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ControlPanelFrameHandler extends JFrame {
    public static final int WINDOWWIDTH = 900;
    public static final int WINDOWHEIGHT = 800;

    public JDesktopPane pane = new JDesktopPane();
    private JInternalFrame logWindow = login.loginScreen(); // The internal Windows

    private JInternalFrame listBillboardWindow = listBillboards.listBillboards();

    public ControlPanelFrameHandler() {
        super("Control Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
//       // JInternalFrame List

////        LOGIN Section \/
        logWindow.setVisible(true);
////        LOGIN Section /\

//        BILLBOARD LIST \/
        listBillboardWindow.setVisible(false);
//        BILLBOARD LIST /\

//        CREATE BILLBOARD \/

//        CREATE BILLBOARD /\

//        EDIT USER \/

//        EDIT USER /\

//        Schedule Billboards \/

//        Schedule Billboards /\


//        MASTER DISPLAY
//        Add to the master window
        pane.add(logWindow);
        pane.add(listBillboardWindow);

        getContentPane().add(pane);

        // Display the window.
        setPreferredSize(new Dimension(WINDOWWIDTH, WINDOWHEIGHT));
        setLocation(new Point(500, 0));
        pack();
        setVisible(true);
    }

}
