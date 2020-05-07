package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class listBillboards extends JFrame {
    static JInternalFrame window = new JInternalFrame( "List Billboards", false, false, true);
    public static JInternalFrame listBillboards() {
        window.setSize(600, 300);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);
        window.setLayout(new GridLayout(4,1));

//        Heading
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("List Billboards");
        titleLabel.setVerticalTextPosition(JLabel.TOP);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setFont(controlPanel.titleFont);
        titlePanel.add(titleLabel);

        //      Calendar setup
        String[] columnHeading = {"ID", "Billboard Name", "Creator", "Date Made", "Date Modified", "File Location"};

        String[][] rowData = {
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""},
                {"", "", "", "", "", ""}
        };

//        JPanel tableCalendarPanel = new JPanel();
        JTable tableBillboard = new JTable(rowData, columnHeading);
//        Stops the columns from being reordered
        tableBillboard.getTableHeader().setReorderingAllowed(false);
        JScrollPane billboardPane = new JScrollPane(tableBillboard);

//      Selected table information
        JPanel selectedBillboardPanel = new JPanel();
        selectedBillboardPanel.setLayout(new GridLayout(1,2));
        JLabel selectedBillboardLabel = new JLabel("Selected Billboard: ");
        selectedBillboardLabel.setHorizontalAlignment(JLabel.RIGHT);
        JLabel selectedBillboard = new JLabel();
        selectedBillboardPanel.add(selectedBillboardLabel);
        selectedBillboardPanel.add(selectedBillboard);

//      Button setup
        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Create Billboard");
        JButton editButton = new JButton("Edit Billboard");
        JButton deleteButton = new JButton("Delete Billboard");
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                createBillboards.window.setVisible(true);
                createBillboards.window.toFront();
            }
        });

//      NOTE:: ADD Listen Function for when a row is clicked (If Clicked change 'selectedBillboard' text
//      NOTE:: ADD ARE YOU SURE YOU WANT TO EDIT DIALOG BOX
//      NOTE:: ADD ARE YOU SURE YOU WANT TO REMOVE DIALOG BOX

        window.add(titlePanel);
        window.add(billboardPane);
        window.add(selectedBillboardPanel);
        window.add(buttonsPanel);


        return window;
    }

}
