package controlpanel;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Create window for the Scheduling system. Should only display a couple of small windows to select options from.
 */
public class createSchedule {
    static JInternalFrame window = new JInternalFrame( "New schedule.", false, false, true);
    private static Integer currentFrame = 0;

    public static JPanel days()
    {
        //      Button setup
        JPanel buttonsPanel = new JPanel();
        String[] daysInWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for(String day : daysInWeek)
        {
            JRadioButton  button = new JRadioButton(day);
            buttonsPanel.add(button);
        }
        return buttonsPanel;
    }


    public static JInternalFrame createSchedule()
    {
        window.setSize(600, 300);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);
        window.setLayout(new GridLayout(5,1));

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
        JLabel dynamicLabel = new JLabel("Dynamic label");
        dynamicLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel footer = new JPanel();
        JButton next = new JButton("Next");

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame++;
            }
        });

        pane.add(dynamicLabel);
        footer.add(next);

        window.add(titlePanel);
        window.add(pane);
        window.add(footer);
        return window;
    }
}
