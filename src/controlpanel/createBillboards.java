package controlpanel;

import javax.swing.*;
import java.awt.*;

public class createBillboards {
    static JInternalFrame window = new JInternalFrame("Create Billboard", false, false, true);
    public static JInternalFrame createBillboards() {
        JPanel mainHeading = new JPanel();
        JPanel window2 = new JPanel();
        window.setSize(600, 300);
        window2.setSize(300, 150);
        window.setLocation((controlPanel.WINDOWWIDTH/2) - 300, (controlPanel.WINDOWHEIGHT/2) - 200);
//        window.setLayout(new GridLayout(3,2));

//        Heading - "Requirements"
        JLabel requirements = new JLabel("Requirements");
        requirements.setVerticalTextPosition(JLabel.TOP);
        requirements.setHorizontalTextPosition(JLabel.LEFT);
        requirements.setFont(new Font("Ariel", Font.BOLD, 20));

//        Heading - "Title"
        JLabel title = new JLabel("Billboard Title");
        title.setVerticalTextPosition(JLabel.CENTER);
        title.setHorizontalTextPosition(JLabel.LEFT);
//        Text Box
        JTextField input1 = new JTextField();

        //        Heading - "Text"
        JLabel text = new JLabel("Billboard Text");
        text.setVerticalTextPosition(JLabel.CENTER);
        text.setHorizontalTextPosition(JLabel.LEFT);
//        Text Box
        JTextField input2 = new JTextField();

//        Heading - "Select Background Image"
        JLabel selectBG = new JLabel("Select Background Image");
        selectBG.setVerticalTextPosition(JLabel.BOTTOM);
        selectBG.setHorizontalTextPosition(JLabel.LEFT);
//        Button - Browse
        JButton imageSelect = new JButton("Browse", new ImageIcon("src\\controlpanel\\browseicon.png"));
        imageSelect.setBounds(100,100,140,40);
//
//        Buttons - Save and Preview
        JButton saveBB = new JButton("Save", new ImageIcon("src\\controlpanel\\save.png"));
        saveBB.setBounds(100,100,140,40);

        JButton previewBB = new JButton("Preview", new ImageIcon("src\\controlpanel\\preview.png"));
        previewBB.setBounds(100,100,140,40);

//        Add items to GUI
        window.setLayout(new GridLayout(2,1));
        mainHeading.add(requirements);
        window.add(mainHeading);
        window2.setLayout(new GridLayout(4,2));
        window.add(window2);
        window2.add(title);
        window2.add(input1);
        window2.add(text);
        window2.add(input2);
        window2.add(selectBG);
        window2.add(imageSelect);
        window2.add(saveBB);
        window2.add(previewBB);
        



//        Preview window - viewer.java



        return window;
    }
}
