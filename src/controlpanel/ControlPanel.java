package controlpanel;

//import resources.GetPropertyValues;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 *  The main renderer for the Control Panel, encompasses all other windows
 *  that relate to modifying and creation of billboards and users.
 *  @see Controller for majority of logic
 *  @see ControlPanel for JFrame rendering configuration and intialization
 *
 *  - Todo add all members to an authors list and copy and paste in all documents
 *
 * @version 	%I%, %G%
 * @since       JDK13
 */
public class ControlPanel extends JFrame {

//    private static final boolean CLOSABLE = true;
//    private static final boolean ICONIFIABLE = true;
//    private static final boolean MAXIMIZABLE = true;
//    private static final boolean RESIZABLE = true;
    public static final int WINDOWWIDTH = 1000;
    public static final int WINDOWHEIGHT = 800;
    public static final Font titleFont = new Font("Ariel", Font.BOLD, 20);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new ControlPanelFrameHandler();
    }
}