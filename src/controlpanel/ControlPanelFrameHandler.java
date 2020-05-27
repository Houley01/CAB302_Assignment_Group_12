package controlpanel;

import javax.swing.*;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

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

    public ControlPanelFrameHandler() throws IOException, ClassNotFoundException {
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

//    Action Listeners
    private class menuCreateButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (controller.permission.GetUserPermission("CreateBillboard") == true ) {
                controller.showCreateBillboard();
            } else {
                DialogWindow.NoAccessTo("Creating Billboard");
            }
        }
    }

    private class menuItemListBillboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.showListBillboard();
        }
    }

    private class menuScheduleButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                controller.showSchedule();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static class menuEditUserButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.showEditUser();
        }
    }

    private class menuHelpButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.showHelpScreen();
        }
    }

    private class menuLogoutButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.logout();
        }
    }
}
