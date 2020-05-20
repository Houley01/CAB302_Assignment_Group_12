package controlpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

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
        requirements.setFont(controlPanel.titleFont);

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
        JButton imageSelect = new JButton("Browse", new ImageIcon("src\\controlpanel\\resources\\browseicon.png"));
        imageSelect.setBounds(100,100,140,40);

        //  Buttons/Label - Colour Picker text and background
        JButton textColourPickerButton = new JButton("Text Colour");
        JTextField textDisplayColour = new JTextField();
        JButton backgroundColourPickerButton = new JButton("Background Colour");
        JTextField backgroundDisplayColour = new JTextField();

//        Buttons - Save and Preview
        JButton saveBB = new JButton("Save", new ImageIcon("src\\controlpanel\\resources\\save.png"));
        saveBB.setBounds(100,100,140,40);

        JButton previewBB = new JButton("Preview", new ImageIcon("src\\controlpanel\\resources\\preview.png"));
        previewBB.setBounds(100,100,140,40);

//        Add items to GUI
        window.setLayout(new GridLayout(2,1));
        mainHeading.add(requirements);
        window.add(mainHeading);
        window.add(window2);
        window2.setLayout(new GridLayout(6,2));
        window2.add(title);
        window2.add(input1);
        window2.add(text);
        window2.add(input2);

        window2.add(textColourPickerButton);
        window2.add(textDisplayColour);
        window2.add(backgroundColourPickerButton);
        window2.add(backgroundDisplayColour);

        window2.add(selectBG);
        window2.add(imageSelect);
        window2.add(saveBB);
        window2.add(previewBB);

//        Preview window - viewer.java

        textColourPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColourPicker(textDisplayColour, Color.BLACK);
            }
        });

        backgroundColourPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColourPicker(backgroundDisplayColour, Color.WHITE);
            }
        });

        saveBB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String billboardTitle = input1.getText();
                String billboardText = input2.getText();
//                String textColour1 = String.valueOf(textDisplayColour.getBackground().getGreen());
//                String textColour = String.valueOf(textDisplayColour.getBackground());
//                System.out.println(textColour);
//                System.out.println(textColour1);

                System.out.println(RGBToHex(textDisplayColour.getBackground()));

//                String backgroundColour = textDisplayColour.getBackground();
//                if ((urlTextfield.getText() == null) && (fileLocation == null) ) {
//
//                }
                int urlOrImage = -1;
//                if (urlTextfield.getText() == null) {
//                    String image = urlTextfield.getText();
//                } else if (fileChoosen != null ) {
//                    String image = controller.createMD5(fileLocation);
//                }

//                try {
//                    controller.createBillboard(billboardTitle, billboardText, textColour, backgroundColour, urlOrImage, "");
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }

            }
        });

        imageSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser browseImage = new JFileChooser((FileSystemView.getFileSystemView().getHomeDirectory()));

                int returnInt = browseImage.showOpenDialog(null);
                // int returnInt = browseImage.showSaveDialog(null);

                if (returnInt == JFileChooser.APPROVE_OPTION) {
                    File fileChosen = browseImage.getSelectedFile();
                    System.out.println(fileChosen.getAbsolutePath());
                }
            }
        });

        return window;
    }

    private static void ColourPicker(JComponent component, Color initialColour) {
        // Colour chooser Pop up window
        Color color = JColorChooser.showDialog(component,
                "Select a colour", initialColour);

        // set Background color of the JComponent
        component.setBackground(color);
//        System.out.println(component.getBackground()); // Used for getting colour code TEMP CODE
    }

    private static String RGBToHex(Color RGB) {
        String hex = "";
        String temp, red, green, blue;

//        Red
        temp = Integer.toHexString(RGB.getRed());
        if (temp.length() < 2) {
            red = "0" + temp;
        } else {
            red = temp;
        }

//        Green
        temp = Integer.toHexString(RGB.getGreen());
        if (temp.length() < 2) {
            green = "0" + temp;
        } else {
            green = temp;
        }

//        Blue
        temp = Integer.toHexString(RGB.getBlue());
        if (temp.length() < 2) {
            blue = "0" + temp;
        } else {
            blue = temp;
        }
        return red+green+blue;
    }

}
