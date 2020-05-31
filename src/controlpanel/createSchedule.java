package controlpanel;


import resources.Billboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Create window for the Scheduling system. Should only display a couple of small windows to select options from.
 */
public class createSchedule {
    static JInternalFrame window = new JInternalFrame( "New schedule.", false, false, true);
    private static Date currentTime = Calendar.getInstance().getTime();
    private static JPanel DurationMins = durationMinutes("Duration in minutes:");
    private static ArrayList<JComponent> comp = new ArrayList<>(); // Add all the panels into a list so we can put a listener

    public static JPanel inputField()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        return panel;
    }

    /**
     * Display billboards in a comboBox
     * @param label Input label
     * @return Jpanel component.
     * @throws IOException              Signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException   Signals that a particular class is not found at runtime.
     */
    private static JPanel billboard(String label) throws IOException, ClassNotFoundException {
        JPanel panel = inputField();
        String[][] billboard = controller.ListBillboards();
        String[] temp = new String[billboard.length];
        for(int outer = 0; outer < billboard.length; outer++)
        {
            temp[outer] = billboard[outer][1];
        }

        JComboBox billboards = new JComboBox(temp);


        panel.add(new JLabel(label));
        panel.add(billboards);
        return panel;
    }

    /**
     *
     * @param label Input label.
     * @return      JPanel component.
     */
    private static JPanel daySelect(String label)
    {
        //      Button setup
        JPanel panel = inputField();
        String[] daysInWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        JComboBox days = new JComboBox(daysInWeek);
        days.setSelectedIndex(currentTime.getDay()-1);      // Current day in Int (1 mon 2 tue ... 7 sunday)
        panel.add(new JLabel(label));                       // Add label
        panel.add(days);                                    // Add combo box with the days
        return panel;
    }

    /**
     * Time format for input was found
     * <a href="https://stackoverflow.com/questions/2234726/jformattedtextfield-input-time-duration-value">here</a>.
     * Creates a formatted text input to Hours and minutes so we force some user input.
     * @author  nanda
     * @return  input time field.
     */
    private static JPanel inputTime(String label, boolean start)
    {
        JFormattedTextField time = new JFormattedTextField();
        time.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("H'h' mm'm'"))));
        if(start) time.setValue(currentTime);

        JPanel panel = inputField();
        panel.add(new JLabel(label));
        panel.add(time);
        return panel;
    }

    /**
     * Creates a simple Java swing input text field.
     * @param label     Label for the input.
     * @return          Returns JPanel object.
     */
    private static JPanel simpleInput(String label)
    {
        JPanel panel = inputField();
        JLabel labelOut = new JLabel(label);
        JTextField input = new JTextField();
        input.setName(label);

        panel.add(labelOut);
        panel.add(input);
        return panel;
    }

    /**
     * Creates a recurring input. Includes a listener to check
     * to see if minutes was selected and setting visbility of
     * Duration Mins.
     * @param label     Label for the input.
     * @return          JPanel object.
     */
    private static JPanel recurring(String label)
    {
        JPanel panel = inputField();
        String[] recurOptions = {"Daily", "Hourly", "Minutes"};
        JComboBox options = new JComboBox(recurOptions);
        panel.add(new JLabel(label));
        panel.add(options);

        System.out.println(options.getSelectedIndex());

        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = options.getSelectedItem() == recurOptions[2];
                if(selected) DurationMins.setVisible(true);
                else DurationMins.setVisible(false);
            }
        });

        return panel;
    }

    /**
     * Input for how long the duration should last in minutes.
     * @param label Label for input.
     * @return      JPanel Object.
     */
    private static JPanel durationMinutes(String label)
    {
        JPanel panel = inputField();
        JTextField input = new JTextField();

        panel.add(new JLabel(label));
        panel.add(input);

        return panel;
    }


    /**
     * Create schedule contains the the settings and construction of the window.
     * Contains listeners to create a new schedule in the database.
     * @return                          JInternalFrame for the controlPanelFrameHandler
     * @throws IOException              Signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException   Signals that a particular class is not found at runtime.
     */
    public static JInternalFrame createSchedule() throws IOException, ClassNotFoundException {
        window.setSize(600, 500);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);
        window.setLayout(new GridLayout(10,1));
        DurationMins.setVisible(false);

//        Heading
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Create new schedule");
        titleLabel.setVerticalTextPosition(JLabel.TOP);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setFont(controlPanel.titleFont);
        titlePanel.add(titleLabel);


        /**
         * Plan is to dynamically update the pane when the user clicks 'next'
         */
//      Selected table information
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1,1));

        JPanel footer = inputField();
        JButton cancel = new JButton("Cancel");
        JButton create = new JButton("Create");

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
            }
        });

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean validation = validate();
                if(validation)
                {
                    try {
                        controller.createNewSchedule(getValues());
                        window.setVisible(false);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    System.out.println("No");
                }
            }
        });

//        pane.add(selectPanel());
        footer.add(cancel);
        footer.add(create);
        comp.add(billboard("Billboard"));
        comp.add(daySelect("Start day"));
        comp.add(inputTime("Time to start", true));
        comp.add(simpleInput("Duration (minutes)"));
        comp.add(recurring("How often to recur?"));
        comp.add(DurationMins);

        window.add(titlePanel);
        window.add(pane);
        for(JComponent item : comp)
        {
            window.add(item);
        }
        window.add(footer);
        return window;
    }

    /**
     * Gets the value of all listed inputs and compiles them into a array string
     * that we can access. (Is much easier than grabbing them manually
     * @return  Array string with input values.
     */
    private static  ArrayList<String> getValues(){
        ArrayList<String> values = new ArrayList<>();
        for(JComponent panels : comp)
        {
            for(int i = 0; i < panels.getComponentCount(); i++)
            {
                JComponent item = (JComponent) panels.getComponent(i);
                if(item instanceof JComboBox)
                {
                    values.add((String) ((JComboBox) item).getSelectedItem());
                }
                else if(item instanceof JTextField)
                {
                    values.add(((JTextField) item).getText());
                }
                else if(item instanceof JFormattedTextField)
                {
                    values.add(item.toString());
                }
            }
        }
        return values;
    }

    /**
     * Check to see if the inputs are valid. Checks to see
     * if inputs the user has to enter a value for are empty
     * and or invalid. E.g. negative duration.
     * @return  True if all inputs are valiid.
     *          False if an input is invalid.
     */
    private static boolean validate()
    {
        ArrayList<String> values = getValues();
        /**
         * [0]: Billboard
         * [1]: Day starting from
         * [2]: Time starting from
         * [3]: Duration
         * [4]: Recurring
         * [5]: Recurring minutes (if mins was selected)
         */
        if(values.get(3) == null || values.get(3).isEmpty())
        {
            DialogWindow.showErrorPane(
                    "You must enter a duration for the billboard.",
                    "Error"
            );
            return false;
        }

        int duration = Integer.parseInt(values.get(3));

        if(duration <= 0)
        {
            DialogWindow.showErrorPane(
                    "You cannot have a negative duration.",
                    "Error"
            );
            return false;
        }



        if(values.get(4) == "Minutes")
        {
            if(values.get(5) == null || values.get(5).isEmpty())
            {
                DialogWindow.showErrorPane(
                        "You cannot schedule the billboard to recur never again.",
                        "Error"
                );
            }

            int count = Integer.parseInt(values.get(5));

            if(count <= 0)
            {
                DialogWindow.showErrorPane(
                        "You cannot recur at negative or no intervals",
                        "Error"
                );
            }

            if(count > duration)
            {
                DialogWindow.showErrorPane(
                        "You cannot schedule the billboard more frequently than the time allotted.",
                        "Error"
                );
                return false;
            }
        }
        return true;
    }


}
