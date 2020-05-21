package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.desktop.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class HelpPage {
    static JInternalFrame window = new JInternalFrame("Help Page", false, false,true);
    public static JInternalFrame HelpPage() {
        JPanel mainHeading = new JPanel();
        JPanel window2 = new JPanel();
        window.setSize(600,300);
        window2.setSize(300,150);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);

//  Heading - "Need Help?"
        JLabel  needHelp = new JLabel("Need Help?");
        needHelp.setVerticalTextPosition(JLabel.TOP);
        needHelp.setHorizontalTextPosition(JLabel.LEFT);
        needHelp.setFont(controlPanel.titleFont);

//  Button - Java Doc
        JButton javaDoc = new JButton("Java Doc", new ImageIcon("src\\controlpanel\\resources\\javadoc.png"));
        javaDoc.setBounds(100, 100, 140, 40);

        javaDoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File("billboardsExamples/16.png"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        //  Button - "System Overview"
        JButton systemOverview = new JButton("System Overview", new ImageIcon("src\\controlpanel\\resources\\systemoverview.png"));
        javaDoc.setBounds(100, 100, 140, 40);

        systemOverview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File("billboardsExamples/16.png"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        window.setLayout(new GridLayout(2,1));
        mainHeading.add(needHelp);
        window.add(mainHeading);
        window.add(window2);
        window2.setLayout(new GridLayout(1,2));
        window2.add(javaDoc);
        window2.add(systemOverview);

        return window;
    }
}
