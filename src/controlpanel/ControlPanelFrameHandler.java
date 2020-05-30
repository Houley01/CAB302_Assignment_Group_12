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
    private static JInternalFrame logWindow = Login.loginScreen(); // The internal Windows
    private static JInternalFrame listBillboardWindow;
//    List Billboard JInternalFrame
    static {
        try {
            listBillboardWindow = ListBillboards.listBillboards();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
}
    private static JInternalFrame createBillboardWindow = CreateBillboards.createBillboards();
    private static JInternalFrame scheduleBillboardWindow;
//    ScheduleBillboard JInternalFrame
    static {
        try {
            scheduleBillboardWindow = ScheduleBillboards.scheduleBillboards();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static JInternalFrame createScheduleWindow;

    static {
        try {
            createScheduleWindow = CreateSchedule.createSchedule();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static JInternalFrame userWindow = UsersPage.userPage();
    private static JInternalFrame adminEditUser = UsersPage.AdminEditUserWindow();
    //private JInternalFrame userEditUser = UsersPage.UserEditUserWindow();
    private JInternalFrame helpWindow = HelpPage.HelpWindow();
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
        menuItemCreate.addActionListener(new MenuCreateButton());
        menuItemList.addActionListener(new MenuItemListBillboard());
        menuItemSchedule.addActionListener(new MenuScheduleButton());
        menuItemEditUser.addActionListener(new MenuEditUserButton());
        menuItemHelp.addActionListener(new MenuHelpButton());
        menuItemLogout.addActionListener(new MenuLogoutButton());

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

        logWindow.setVisible(true); // We need the user to Login first
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
    private class MenuCreateButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (Controller.permission.GetUserPermission("CreateBillboard")) {
                Controller.ShowCreateBillboard();
            } else {
                DialogWindow.NoAccessTo("Creating Billboard");
            }
        }
    }

    private class MenuItemListBillboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Controller.ShowListBillboard();
        }
    }

    private class MenuScheduleButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Controller.ShowSchedule();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static class MenuEditUserButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Controller.ShowEditUser();
        }
    }

    private class MenuHelpButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Controller.ShowHelpScreen();
        }
    }

    private class MenuLogoutButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Controller.Logout();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
