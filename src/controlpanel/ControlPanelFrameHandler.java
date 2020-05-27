package controlpanel;

import javax.swing.*;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 *  Configuration for JFrame and main window settings.
 *  This includes buttons for other windows and handlers for
 *  the following items:
 *  <ul>
 *      <li>Navbar</li>
 *      <li>Login screen</li>
 *      <li>Billboard listing</li>
 *      <li>Billboard creation</li>
 *      <li>Billboard schedules</li>
 *      <li>Users window <i>modal containing buttons containing:</i></li>
 *      <ul>
 *          <li>Modify admin user</li>
 *          <li>Modify normal user (change password)</li>
 *      </ul>
 *      <li>Help window</li>
 *  </ul>
 *
 */

public class ControlPanelFrameHandler extends JFrame {
    public static final int WINDOWWIDTH = 900;
    public static final int WINDOWHEIGHT = 800;
    static JMenuBar bar = new JMenuBar();

    public JDesktopPane pane = new JDesktopPane();
    private JInternalFrame logWindow = login.loginScreen(); // The internal Windows
    private JInternalFrame listBillboardWindow = listBillboards.listBillboards();
    private JInternalFrame createBillboardWindow = createBillboards.createBillboards();
    private JInternalFrame scheduleBillboardWindow = scheduleBillboards.scheduleBillboards();
    private JInternalFrame userWindow = usersPage.userPage();
    private JInternalFrame adminEditUser = usersPage.AdminEditUserWindow();
    private JInternalFrame userEditUser = usersPage.UserEditUserWindow();
    private JInternalFrame helpWindow = HelpPage.HelpPage();

    /**
     * Master configuration file for control panel. Contains action events
     * and main objects for rendering.
     *
     * @throws IOException
     */
    public ControlPanelFrameHandler() throws IOException {
        super("Control Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

//      Master Frame Content
        JButton menuItemCreate = new JButton("Create Billboards");
        JButton menuItemList = new JButton("List Billboards");
        JButton menuItemSchedule = new JButton("Schedule Billboards");
        JButton menuItemEditUser = new JButton("Edit User Settings");
        JButton menuItemHelp = new JButton("Help");
        JButton menuItemLogout = new JButton("Logout");

//      Menu Item Action Listener Caller
        menuItemCreate.addActionListener(new menuCreateButton());
        menuItemList.addActionListener(new menuItemListBillboard());
        menuItemSchedule.addActionListener(new menuScheduleButton());
        menuItemEditUser.addActionListener(new menuEditUserButton());
        menuItemHelp.addActionListener(new menuHelpButton());
        menuItemLogout.addActionListener(new menuLogoutButton());

        bar.add(menuItemCreate);
        bar.add(menuItemList);
        bar.add(menuItemSchedule);
        bar.add(menuItemEditUser);
        bar.add(menuItemHelp);
        bar.add(menuItemLogout);
        setJMenuBar(bar);
        bar.setVisible(false);

//       // JInternalFrame List

////        LOGIN Section \/
        logWindow.setVisible(true);
////        LOGIN Section /\

//        BILLBOARD LIST \/
        listBillboardWindow.setVisible(false);
//        BILLBOARD LIST /\

//        CREATE BILLBOARD \/
        createBillboardWindow.setVisible(false);
//        CREATE BILLBOARD /\

//        Schedule Billboards \/
        scheduleBillboardWindow.setVisible(false);
//        Schedule Billboards /\

//        EDIT USER \/
        userWindow.setVisible(false);
        adminEditUser.setVisible(false);
        userEditUser.setVisible(false);
//        EDIT USER /\

//        MASTER DISPLAY
//        Add to the master window
        pane.add(logWindow);
//        pane.add(helpWindow);
        pane.add(listBillboardWindow);
        pane.add(createBillboardWindow);
        pane.add(scheduleBillboardWindow);
        pane.add(userWindow);
        pane.add(adminEditUser);
        pane.add(userEditUser);
        pane.add(helpWindow);


        getContentPane().add(pane);

        // Display the window.
        setPreferredSize(new Dimension(WINDOWWIDTH, WINDOWHEIGHT));
        setLocation(new Point(500, 0));
        pack();
        setVisible(true);
    }

    /**
     *  Create billboard action listener. Listener for when the user
     *  clicks on create billboard button.
     */
//    Action Listeners
    private class menuCreateButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.showCreateBillboard();
        }
    }

    /**
     *  List billboard action listener. Listener for when the user
     *  clicks on list billboard button.
     */
    private class menuItemListBillboard implements ActionListener {
        public void actionPerformed(ActionEvent e)  {
            controller.showListBillboard();
        }
    }

    /**
     *  Schedule billboard action listener. Listener for when the user
     *  clicks on schedule billboard button.
     */
    private class menuScheduleButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                controller.showSchedule();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *  Edit user action listener. Listener for when the user
     *  clicks on edit user button.
     */
    public static class menuEditUserButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.showEditUser();
        }
    }

    /**
     *  Help button action listener. Listener for when the user
     *  clicks on help button button.
     */
    private class menuHelpButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.showHelpScreen();
        }
    }

    /**
     *  Logout button action listener. Listener for when the user
     *  clicks on logout button button.
     */
    private class menuLogoutButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.logout();
        }
    }
}
