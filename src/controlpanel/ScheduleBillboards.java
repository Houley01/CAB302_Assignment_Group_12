package controlpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Scheduling billboards. Logic is similar to listing billboards as the billboard data
 * is contained in a 3D array as well.
 */
public class ScheduleBillboards {
    static JInternalFrame window = new JInternalFrame( "Schedule Billboards", false, false, true);
    private static JTable tableBillboard = new JTable();


    /**
     * Seeing the JLabel create everywhere started to get a little annoying so this function
     * creates a Jlabel component so the code 'looks' cleaner. Doesn't really add any functionality
     * outside making the code less of a mess to look at.
     * @param input
     * @return
     */
    private static JLabel createLabel(String input)
    {
        return new JLabel(input);
    }

    /**
     * Helper function to downsize the "look" of complexity in the main function.
     * @param comp  Component (we return this with the formatted text).
     * @param title If the text requires the Title font styling.
     * @return
     */
    private static JLabel formatText(JLabel comp, boolean title)
    {
        comp.setVerticalTextPosition(JLabel.TOP);
        comp.setHorizontalAlignment(JLabel.LEFT);
        if(title) comp.setFont(ControlPanel.titleFont);
        return comp;
    }


    private static void windowSettings(JInternalFrame frame)
    {
        frame.setSize(600, 400);
        frame.setLocation((ControlPanel.WINDOWWIDTH/2) - 400, (ControlPanel.WINDOWHEIGHT/2) - 200);
        frame.setLayout(new GridLayout(3,1));
    }


    /**
     * Lists currently schedule billboards into a table and displays that to the user. The timing is listed top to bottom like so:
     * <table>
     *     <tr>
     *         <th>Time</th>
     *         <th>Monday</th>
     *         <th>Tuesday</th>
     *         <th>Wednesday</th>
     *         <th>Thursday</th>
     *         <th>Friday</th>
     *     </tr>
     *     <tr>
     *         <td>8:30am</td>
     *         <td>Some billboard data</td>
     *     </tr>
     *     <tr>
     *         <td> ... </td>
     *         <td> ... </td>
     *     </tr>
     *     <tr>
     *         <td>5:00pm</td>
     *     </tr>
     * </table>
     * <i>This also includes the creation of a new schedule, editing existing schedules and deletion</i>
     *
     * @return window       JFrame window object with configuration settings
     */
    public static JInternalFrame scheduleBillboards() throws IOException, ClassNotFoundException {

        windowSettings(window);
//        Heading
        JPanel titlePanel = new JPanel();
//        titlePanel.setSize(window.getWidth(),25);
        JLabel titleLabel = createLabel("Schedule Billboards");
        // Giant text that displays "Schedule billboards" (Not the window frame title data)
        formatText(titleLabel, true);
        titlePanel.add(titleLabel);

        String[][] Data = Controller.ListSchedule();
//      Calendar setup

        DefaultTableModel tableCalendar = buildTable();
        tableBillboard.setModel(tableCalendar);
        tableBillboard.getTableHeader().setReorderingAllowed(false);
        JScrollPane calendar = new JScrollPane(tableBillboard);
        calendar.setSize(window.getWidth(), 400);
        calendar.setLocation(0, 30);

//      Button setup
        JPanel buttons = new JPanel();

        JButton createButton =  new JButton("Create new Scheduling");
        JButton editButton =    new JButton("Edit Scheduling");      // We'll be "editing" the schedule dynamically
        JButton deleteButton =  new JButton("Delete Scheduling");
        JButton refreshButton =  new JButton("Refresh Scheduling");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.ShowCreateSchedule();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reload(tableCalendar);
            }
        });

        // Code was already here when I started working on it
        // but I can't be screwed moving it into arrays to
        // add dynamically. - Brendan
        buttons.add(createButton);
        buttons.add(editButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        window.add(titlePanel);
        window.add(calendar);
        window.add(buttons);
        return window;
    }

    /**
     * Builds table
     * @return table model
     */
    private static DefaultTableModel buildTable()
    {
        /**
         * Table heading for the 3D matrix
         */
        String[] columnHeading = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        /**
         * 3D matrix array set up? So, 0x0 = 8:30am but 1x1 would be Monday at 9:00am.
         * So the program needs to be able to grab the start date time / date and
         * end date / time and calculate how lon that would take so for example:
         * If a program started at Monday at 9am but ended on tuesday at 9am it would
         * look something like this
         * -Time- |-Mon-|-Tue-|  ...
         * "8am"  |  1  |  1  |  ...
         * "9am"  |  1  |  0  |  ...
         * "10am" |  1  |  0  |  ...
         * "11am" |  1  |  0  |  ...
         * "..."  |  1  |  0  |  ...
         * 1's would be where we replace the empty string with the table name. We'll
         * use the table name since it'll be easier for the end user to read but will
         * be a longer SQL query.
         *
         * Todo figure out how to implement the logic above. - Help
         */
        String[][] rowData = new String[24][columnHeading.length];

        for(int outer = 0; outer < 24; outer++)                                 // TIME 1 am ... 12am (24 hour format)
        {
            for(int inner = 0; inner < columnHeading.length; inner++)           // Column position (0 to 6) per day
            {
                // If inner is 0 it has to be the time column
                if(inner == 0)
                {
                    String am_or_pm = "am";
                    int time = outer+1 >= 13 ? outer+1 - 12 : outer+1;                              // 12 hour conversion
                    if(outer+1 >= 12 && outer+1 != 24) am_or_pm = "pm";                             // PM / AM formatter
                    String hours = time < 10 ? "0"+String.valueOf(time) : String.valueOf(time);     // String conversion + formatting
                    rowData[outer][inner] = hours + am_or_pm;                                       // Appending to array
                }
                // Empty data for now
                else
                {
                    rowData[outer][inner] = "";
                }
            }
        }


        DefaultTableModel tableCalendar = new DefaultTableModel(rowData, columnHeading);
        return tableCalendar;
    }
    private static void reload(DefaultTableModel model)
    {
        model.setRowCount(0);
        tableBillboard.setModel(buildTable());
    }

}
