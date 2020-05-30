package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class ControlPanelFrameHandler extends JFrame {
    public static final int WINDOWWIDTH = 900;
    public static final int WINDOWHEIGHT = 800;
    static JMenuBar bar = new JMenuBar();

    public static JDesktopPane pane = new JDesktopPane();
    private static JInternalFrame logWindow = login.loginScreen(); // The internal Windows
    private static JInternalFrame listBillboardWindow;
//    List Billboard JInternalFrame
    static {
        try {
            listBillboardWindow = listBillboards.listBillboards();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static JInternalFrame createBillboardWindow = createBillboards.createBillboards();
    private static JInternalFrame scheduleBillboardWindow;
//    ScheduleBillboard JInternalFrame
    static {
        try {
            scheduleBillboardWindow = scheduleBillboards.scheduleBillboards();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static JInternalFrame createScheduleWindow;

    static {
        try {
            createScheduleWindow = createSchedule.createSchedule();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static JInternalFrame userWindow = usersPage.userPage();
    private static JInternalFrame adminEditUser = usersPage.AdminEditUserWindow();
    //private JInternalFrame userEditUser = usersPage.UserEditUserWindow();
    private JInternalFrame helpWindow = HelpPage.HelpPage();
    private static ArrayList<JInternalFrame> frames = new ArrayList<>();           // List to add JInteralFrames into so we don't need to


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

        /**
         * Restructured how setting internal frames work.
         * We just add them to a list and modify all of them with
         * the same settings (The code was doing this before but
         * it was hard coded)
         */
        frames.add(logWindow);
        frames.add(listBillboardWindow);
        frames.add(createBillboardWindow);
        frames.add(scheduleBillboardWindow);
        frames.add(createScheduleWindow);
        frames.add(userWindow);
        frames.add(adminEditUser);
        frames.add(helpWindow);

        for(JInternalFrame comp : frames)
        {
            comp.setVisible(false);
            pane.add(comp);
        }

        logWindow.setVisible(true); // We need the user to login first
        getContentPane().add(pane);

        // Display the window.
        setPreferredSize(new Dimension(WINDOWWIDTH, WINDOWHEIGHT));
        setLocation(new Point(500, 0));
        pack();
        setVisible(true);
    }

    public static void LogoutWindow() {
        for(JInternalFrame comp : frames) {
            comp.setVisible(false);
        }
        bar.setVisible(false);
        logWindow.setVisible(true);
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
            try {
                controller.Logout();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        }
    }
}
