package controlpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class listBillboards extends JFrame {
    static JInternalFrame window = new JInternalFrame( "List Billboards", false, false, true);
    static JTable tableBillboard = new JTable();
    static DefaultTableModel model;
    public static JInternalFrame listBillboards() throws IOException, ClassNotFoundException {
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

        DefaultTableModel model = BuildTable();

        tableBillboard.setModel(model);
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
        JButton refreshButton = new JButton("Update list");
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(refreshButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBillboards.window.setVisible(true);
                createBillboards.window.toFront();
                Reload(model);
            }
        });


        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println();
//                Check to see if a row is selected
//                if table returns back -1 means no row has been selected
                if (tableBillboard.getSelectedRow() != -1 ) {
                    String billboardSelected = (String) tableBillboard.getValueAt(tableBillboard.getSelectedRow(), 0);
//                    if the data in the column is equal to empty string "" then don't do the function
                    if (billboardSelected.equals("") == false) {
                        try {
                            controller.EditSelectedBillboard(billboardSelected);
                        } catch (IOException | ClassNotFoundException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    Reload(model);
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Check 1 if billboard is schedule
//                Check 2 if User is creator
//                Check 2 else if user has edit all permission

                if (tableBillboard.getSelectedRow() != -1 ) {
                    String billboardSelected = (String) tableBillboard.getValueAt(tableBillboard.getSelectedRow(), 0);
//                    if the data in the column is equal to empty string "" then don't do the function
                    if (billboardSelected.equals("") == false) {
                        try {
                            controller.DeleteBillboard(billboardSelected);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    Reload(model);
                }

            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    Reload(model);
            }
        });

        window.add(titlePanel);
        window.add(billboardPane);
        window.add(selectedBillboardPanel);
        window.add(buttonsPanel);
        return window;
    }

    public static DefaultTableModel BuildTable() throws IOException, ClassNotFoundException {
        String[][] data = controller.ListBillboards(); // Get data from
        String[] columnHeading = {"ID", "Billboard Name", "Creator", "Date Made", "Date Modified", "File Location"};
       return new DefaultTableModel(data, columnHeading);
    }

    public static void Reload(DefaultTableModel model) {
        try {
            model.setRowCount(0); // Removes all the row
            tableBillboard.setModel(BuildTable()); // Recreate the table model
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

}
