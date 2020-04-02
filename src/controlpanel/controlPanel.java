package controlpanel;

import javax.swing.*;
import java.awt.*;


<<<<<<< Updated upstream
public class controlPanel {
    JFrame mainControlPanelScreen = new JFrame(); // creating instance of JFrame
    controlPanel() {
        JButton createBillboardsButton = new JButton("Create Billboards");
        JButton listBillboardsButton = new JButton("List Billboards");
        JButton scheduleBillboardsButton = new JButton("Schedule Billboards");
        JButton editUserButton = new JButton("Edit User's");
        JButton logOutButton = new JButton("Logout");

        mainControlPanelScreen.add(createBillboardsButton);
        mainControlPanelScreen.add(listBillboardsButton);
        mainControlPanelScreen.add(scheduleBillboardsButton);
        mainControlPanelScreen.add(editUserButton);
        mainControlPanelScreen.add(logOutButton);


//      Grid Layout
        mainControlPanelScreen.setLayout(new GridLayout(3, 2));
        mainControlPanelScreen.setSize(500,500);
        mainControlPanelScreen.setVisible(true);

        // Exit program cleanly
        mainControlPanelScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

=======
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




>>>>>>> Stashed changes
    public static void main(String[] args) {
        controlPanel program = new controlPanel();
        program.setVisible(true);

        login loginScreen = new login();
        loginScreen.setSize(300,100);
        loginScreen.setVisible(true);
        loginScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}