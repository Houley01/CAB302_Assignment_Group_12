package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Scheduling billboards. Logic is similar to listing billboards as the billboard data
 * is contained in a 3D array as well.
 */
public class scheduleBillboards {
    static JInternalFrame window = new JInternalFrame( "Schedule Billboards", false, false, true);

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
        if(title) comp.setFont(controlPanel.titleFont);
        return comp;
    }


    private static void windowSettings(JInternalFrame frame)
    {
        frame.setSize(600, 400);
        frame.setLocation((controlPanel.WINDOWWIDTH/2) - 400, (controlPanel.WINDOWHEIGHT/2) - 200);
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
//        controller.listSchedule();
        windowSettings(window);
//        Heading
        JPanel titlePanel = new JPanel();
//        titlePanel.setSize(window.getWidth(),25);
        JLabel titleLabel = createLabel("Schedule Billboards");
        // Giant text that displays "Schedule billboards" (Not the window frame title data)
        formatText(titleLabel, true);
        titlePanel.add(titleLabel);

        String[][] Data = controller.listSchedule();
        System.out.println(Arrays.toString(Data));
        System.out.println("Grabbed schedule");
//      Calendar setup

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

        JTable tableCalendar = new JTable(rowData, columnHeading);
        tableCalendar.getTableHeader().setReorderingAllowed(false);
        JScrollPane calendar = new JScrollPane(tableCalendar);
        calendar.setSize(window.getWidth(), 400);
        calendar.setLocation(0, 30);

//      Button setup
        JPanel buttons = new JPanel();

        JButton createButton = new JButton("Create new Scheduling");
        JButton editButton = new JButton("Update Scheduling");      // We'll be "editing" the schedule dynamically
        JButton deleteButton = new JButton("Delete Scheduling");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Nice");
                controller.showCreateSchedule();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this);
            }
        });

        // Code was already here when I started working on it
        // but I can't be screwed moving it into arrays to
        // add dynamically. - Brendan
        buttons.add(createButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        window.add(titlePanel);
        window.add(calendar);
        window.add(buttons);
        return window;
    }
}
