package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * <h1>Help page window</h1>
 * Small window that queries the user if they need help.
 * They have the option of either viewing the <i>Java Doc</i> or <i>System Overview</i>.
 *
 */
public class HelpPage {
    static JInternalFrame window = new JInternalFrame("Help Page", false, false,true);

    /**
     * Help page JFrame configuration and setup. Includes <code>action listeners</code>.
     *
     * @return window       Window help page contained in this variable.
     */
    public static JInternalFrame HelpWindow() {
        JPanel mainHeading = new JPanel();
        JPanel ButtonsPanel = new JPanel();
        window.setSize(600,300);
        ButtonsPanel.setSize(300,150);
        window.setLocation((ControlPanel.WINDOWWIDTH/2) - 300, (ControlPanel.WINDOWHEIGHT/2) - 200);

//  Heading - "Need Help?"
        JLabel  needHelp = new JLabel("Need Help?");
        needHelp.setVerticalTextPosition(JLabel.TOP);
        needHelp.setHorizontalTextPosition(JLabel.LEFT);
        needHelp.setFont(ControlPanel.titleFont);

//  Button - Java Doc
        JButton javaDoc = new JButton("Java Doc", new ImageIcon("src/controlpanel/resources/javadoc.png"));
        javaDoc.setBounds(100, 100, 140, 40);

        javaDoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File("JavaDoc/index.html"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        //  Button - "System Overview"
        JButton systemOverview = new JButton("System Overview", new ImageIcon("src/controlpanel/resources/systemoverview.png"));
        javaDoc.setBounds(100, 100, 140, 40);

        systemOverview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File("CAB302 Group 12 Report.pdf"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        window.setLayout(new GridLayout(2,1));
        mainHeading.add(needHelp);
        window.add(mainHeading);
        window.add(ButtonsPanel);
        ButtonsPanel.setLayout(new GridLayout(1,2));
        ButtonsPanel.add(javaDoc);
        ButtonsPanel.add(systemOverview);

        return window;
    }
}
