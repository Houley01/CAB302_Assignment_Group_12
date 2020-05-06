package controlpanel;

import javax.swing.*;
import java.awt.*;

public class scheduleBillboards {
    static JInternalFrame window = new JInternalFrame( "Schedule Billboards", false, false, true);
    public static JInternalFrame scheduleBillboards() {
        window.setSize(600, 400);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 400, (controlPanel.WINDOWHEIGHT/2) - 200);
        window.setLayout(new GridLayout(3,1));
//        Heading
        JPanel titlePanel = new JPanel();
//        titlePanel.setSize(window.getWidth(),25);
        JLabel titleLabel = new JLabel("Schedule Billboards");
        titleLabel.setVerticalTextPosition(JLabel.TOP);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setFont(controlPanel.titleFont);
        titlePanel.add(titleLabel);


//      Calendar setup
        String[] columnHeading = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        String[][] rowData = {
                {"8:30am", "", "", "", "", ""},
                {"9:00am", "", "", "", "", ""},
                {"9:30am", "", "", "", "", ""},
                {"10:00am", "", "", "", "", ""},
                {"10:30am", "", "", "", "", ""},
                {"11:00am", "", "", "", "", ""},
                {"11:30am", "", "", "", "", ""},
                {"12:00pm", "", "", "", "", ""},
                {"12:30pm", "", "", "", "", ""},
                {"1:00pm", "", "", "", "", ""},
                {"1:30pm", "", "", "", "", ""},
                {"2:00pm", "", "", "", "", ""},
                {"2:30pm", "", "", "", "", ""},
                {"3:00pm", "", "", "", "", ""},
                {"3:30pm", "", "", "", "", ""},
                {"4:00pm", "", "", "", "", ""},
                {"4:30pm", "", "", "", "", ""},
                {"5:00pm", "", "", "", "", ""}
        };
//        JPanel tableCalendarPanel = new JPanel();
        JTable tableCalendar = new JTable(rowData, columnHeading);
//        Stops the columns from being reordered
        tableCalendar.getTableHeader().setReorderingAllowed(false);
        JScrollPane calendar = new JScrollPane(tableCalendar);
//        calendar.setSize(window.getWidth(), 400);
//        calendar.setLocation(0, 30);

//      Button setup
        JPanel buttons = new JPanel();
        JButton addButton = new JButton("Create Scheduling");
        JButton editButton = new JButton("Edit Scheduling");
        JButton deleteButton = new JButton("Delete Scheduling");
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);
//        buttons.setSize(window.getWidth(), 100);
//        buttons.setLocation(0, 500);

//        Window setup
        window.add(titlePanel);
        window.add(calendar);
        window.add(buttons);
        return window;
    }
}
