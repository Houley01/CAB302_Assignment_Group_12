package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class usersPage {
    static JInternalFrame window = new JInternalFrame( "Edit User Information", false, false, true);
    public static JInternalFrame userPage() {
        JPanel mainHeading = new JPanel();
        JPanel window2 = new JPanel();
        window.setSize(600, 300);
        window2.setSize(300, 150);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);
//        window.setLayout(new GridLayout(3,2));

//        Heading - "Edit Account Information"
        JLabel editAccount = new JLabel("Edit Account Information");
        editAccount.setVerticalTextPosition(JLabel.TOP);
        editAccount.setHorizontalTextPosition(JLabel.LEFT);
        editAccount.setFont(controlPanel.titleFont);

//      Button - Administrative User
        JButton userAdmin = new JButton("Administrative User", new ImageIcon("src\\controlpanel\\resources\\admin.png"));
        userAdmin.setBounds(100,100,140,40);

        userAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAdminPreferences();
            }
        });

//      Button - Regular User
        JButton userRegular = new JButton("Regular User", new ImageIcon("src\\controlpanel\\resources\\user.png"));
        userRegular.setBounds(100,100,140,40);

        userRegular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.ShowUserPreferences();
            }
        });

//        Add items to GUI
        window.setLayout(new GridLayout(2,1));
        mainHeading.add(editAccount);
        window.add(mainHeading);
        window.add(window2);
        window2.setLayout(new GridLayout(1,2));
        window2.add(userAdmin);
        window2.add(userRegular);

        return window;
    }

    static JInternalFrame adminWindow = new JInternalFrame( "Admin Preferences", false, false, true);
    public static JInternalFrame AdminEditUserWindow() {
        adminWindow.setSize(300,400);
        // INSERT CONTENT HERE

        return adminWindow;
    }

    static JInternalFrame userWindow = new JInternalFrame( "user Preferences", false, false, true);

    public static JInternalFrame UserEditUserWindow() {
        userWindow.setSize(300,400);
        // INSERT CONTENT HERE

        return userWindow;
    }
}
