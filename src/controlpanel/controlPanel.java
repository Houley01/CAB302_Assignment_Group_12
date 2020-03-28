package controlpanel;

import javax.swing.*;
import java.awt.*;


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

    public static void main(String[] args) {

        login loginScreen = new login();
        loginScreen.setSize(300,100);
        loginScreen.setVisible(true);
        loginScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
