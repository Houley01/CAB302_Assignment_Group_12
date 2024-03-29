package controlpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scheduling billboards. Logic is similar to listing billboards as the billboard data
 * is contained in a 3D array as well.
 */
public class ScheduleBillboards {
    static JInternalFrame window = new JInternalFrame( "Schedule Billboards", false, false, true);
    /**
     * Table master, used for when we interact with the table e.g. clicking to grab info
     */
    private static JTable tableBillboard = new JTable();
    /**
     * Table model to build the table with. Calls buildTable();
     */
    public static DefaultTableModel tableCalendar;
    static {
        try {
            tableCalendar = buildTable();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * Seeing the JLabel create everywhere started to get a little annoying so this function
     * creates a Jlabel component so the code 'looks' cleaner. Doesn't really add any functionality
     * outside making the code less of a mess to look at.
     * @param input input to create a label from.
     * @return      JLabel.
     */
    private static JLabel createLabel(String input)
    {
        return new JLabel(input);
    }

    /**
     * Helper function to downsize the "look" of complexity in the main function.
     * @param comp  Component (we return this with the formatted text).
     * @param title If the text requires the Title font styling.
     * @return      Formatted text
     */
    private static JLabel formatText(JLabel comp, boolean title)
    {
        comp.setVerticalTextPosition(JLabel.TOP);
        comp.setHorizontalAlignment(JLabel.LEFT);
        if(title) comp.setFont(ControlPanel.titleFont);
        return comp;
    }

    /**
     * Setups the JFrame settings to be 600 pixel wide and 400 pixels high and sets the location
     * in the middle of the control panel.
     * @param frame Frame to apply settings too.
     */
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

//      Calendar setup

        tableBillboard.setModel(tableCalendar);
        tableBillboard.getTableHeader().setReorderingAllowed(false);
        JScrollPane calendar = new JScrollPane(tableBillboard);
        calendar.setSize(window.getWidth(), 400);
        calendar.setLocation(0, 30);

//      Button setup
        JPanel buttons = new JPanel();

        JButton createButton =  new JButton("Create new Scheduling");
        JButton refreshButton =  new JButton("Refresh Scheduling");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Controller.permission.GetUserPermission("ScheduleBillboard")) {
                    try {
                        CreateSchedule.Reload(CreateSchedule.models);
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }
                    Controller.ShowCreateSchedule();
                } else {
                    DialogWindow.NoAccessTo("Scheduling Billboard");
                }
            }
        });


        tableBillboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int row = tableBillboard.rowAtPoint(point);
                int column = tableBillboard.columnAtPoint(point);
                if(e.getClickCount() == 1 && row != -1 && column != -1)
                {
                    if(tableBillboard.getValueAt(row, column) != null && tableBillboard.getValueAt(row, column) != "")
                    {
                        int minCount = row-1 < 1 ? tableBillboard.getRowCount()-1 : row-1;          // Cell time - 1
                        String max = (String) tableBillboard.getValueAt(row, 0);            // max = current cell
                        String min = (String) tableBillboard.getValueAt(minCount, 0);       // min = cell - 1
                        String cell = (String) tableBillboard.getValueAt(row, column);              // Billboard name

                        try {
                            CellCreatedAt(cell, min,max);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    reload(tableCalendar);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Code was already here when I started working on it
        // but I can't be screwed moving it into arrays to
        // add dynamically. - Brendan
        buttons.add(createButton);
        buttons.add(refreshButton);

        window.add(titlePanel);
        window.add(calendar);
        window.add(buttons);
        return window;
    }

    /**
     * Reads the time from the table 01pm, 01am 12pm and converts it into
     * 24 hour time format with SQL readable time: 24:00:00.
     *
     * @param time  Time string to format.
     * @return      String in HH:MM:SS format.
     */
    private static String FormatReadableTime(String time)
    {
        Pattern p = Pattern.compile("am");
        Pattern p2 = Pattern.compile("pm");
        Matcher am = p.matcher(time);
        Matcher pm = p2.matcher(time);
        if(am.find())
        {
            time = time.replace("^0", "");
            time = time.replace("am", ":00:00");
            time = time.replaceAll("12:00", "24:00");
        }
        if(pm.find())
        {
            time = time.replace("pm", "");
            time = time.replace("^0", "");
            if(Integer.parseInt(time) == 12) time = "0";
            time = String.valueOf(Integer.parseInt(time)+12)+":00:00";
        }
        return time;
    }

    /**
     * Shows some basic information in a dialog pane.
     * @param info  Data from DB query.
     */
    private static void ShowInfoPane(ArrayList<String> info) {

        String message =    info.get(0)+" was created by "+
                            info.get(3)+" is to start displaying at " +
                            info.get(1) + " on " +
                            info.get(2) + " for " + info.get(4) + " Minutes";

        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog("Information on:"+info.get(0));
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    /**
     * Function that grabs the available information from the current cell (Table name and time) and attempts to
     * search in the allotted time - an hour to see if there's a database entry.
     *
     * @param table                     String for the table name.
     * @param min                       String of the minimum amount of time to search from
     * @param max                       String of the maximum amount of time to search from.
     * @throws ParseException           Signals that an error has been reached unexpectedly while parsing.
     * @throws IOException              Signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException   Signals that a particular class is not found at runtime.
     */
    private static void CellCreatedAt(String table, String min, String max) throws ParseException, IOException, ClassNotFoundException {
        min = FormatReadableTime(min);
        max = FormatReadableTime(max);
        ArrayList<String> temp = Controller.GetBillBoardFromTimes(min, max, table);
        ShowInfoPane(temp);
    }

    /**
     * Since the database stores the days as Strings of the actual day
     * we can modify
     * @return  Hash map of days.
     */
    private static HashMap<String, Integer> daysInWeek()
    {
        HashMap<String, Integer> days = new HashMap<String, Integer>();
        days.put("Monday", 1);
        days.put("Tuesday", 2);
        days.put("Wednesday", 3);
        days.put("Thursday", 4);
        days.put("Friday", 5);
        days.put("Saturday", 6);
        days.put("Sunday", 7);
        return days;
    }

    /**
     * This is called 2 for loops (24 * 8) so no loops please. I have broke this rule I have set myself and I
     * have nothing to gain from it other than sanity. - Brendan
     * @param outer     Outer for loop.
     * @param inner     Inner for loop.
     * @param master    Data to parse.
     * @return          Billboard name of the current position or empty.
     */
    private static String PositionData(int outer, int inner, ArrayList<String[]> master) throws IOException, ClassNotFoundException {
        for(String[] data : master)
        {
            int hour = Integer.parseInt(data[3].split(":")[0]);
            int day = daysInWeek().get(data[1]);

            int calculatedDuration = hour + (Integer.parseInt(data[2]) / 60);

            if((hour <= outer && calculatedDuration >= outer))
            {
                if(day == inner)
                {
                    if(!(data[6].isEmpty()))
                    {
                        return Controller.GetBillboardFromID(data[6]);
                    }
                }
            }
        }
        return "";
    }


    /**
     * The code builds a 24 * 7 table (168 cells) calculate and renders which cell should contain what data.
     * Unfortunately I did not have the foresight for days that had extended durations e.g. days that render
     * a time table at 23H and extend for 2 hours which would make it 1H the following day. This is because
     * the way the table generates is ;eft to right going down. This makes sense in a logical sense since the
     * program would have to create a cell at 1x1 then 2x1, 3x1, 4x1 and so forth. The problem arises when
     * this roll over happens and we're doing our calculations and rendering at the same time. For instance
     * 1x23 wants to roll over to 2x1. This is for all intents and purposes very dumb to implement as we'll
     * have 4 or 5 for loops running at the same time. Having 3 nested for loops is bad enough and if we try
     * to linearly calculate the position of each cell and then render each cell separately it it'll take a
     * noticeable amount of time. Too much time.
     *
     * So for the sake of myself, my Sunday 1am commits and my sanity. I am not doing this. God why did I agree
     * to build this system in less than 3 days. - Brendan
     * @return Table model to construct a table from.
     * @throws IOException              Signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException   Signals that a particular class is not found at runtime.
     */
    private static DefaultTableModel buildTable() throws IOException, ClassNotFoundException {
        /**
         * Table heading for the 3D matrix
         */
        String[] columnHeading = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[][] rowData = new String[24][columnHeading.length];

        ArrayList<String[]> temp = Controller.RequestScheduleBillboards();

        /**
         * ROWS
         */
        for(int outer = 0; outer < 24; outer++)                                 // TIME 1 am ... 12am (24 hour format)
        {
            /**
             * COLUMNS
             */
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
                else
                {
                    if(temp != null)
                    {
                        rowData[outer][inner] = PositionData(outer, inner, temp);
                    }
                }
            }
        }


        DefaultTableModel tableCalendar = new DefaultTableModel(rowData, columnHeading);
        return tableCalendar;
    }

    /**
     * Reloads the table by deleting the entire table and re-rendering it.
     * @param model Table model
     * @throws IOException              Signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException   Signals that a particular class is not found at runtime.
     */
    public static void reload(DefaultTableModel model) throws IOException, ClassNotFoundException {
        model.setRowCount(0);
        tableBillboard.setModel(buildTable());
    }

}
