package controlpanel;

import resources.Billboard;
import viewer.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 *  Window and button handling for creation of billboards.
 *  Contains the folllowing:
 *  <ul>
 *      <li>JFrame configuration for create billboard menu</li>
 *      <li>Colour Picker</li>
 *      <li>RGB to Hex converter</li>
 *  </ul>
 */

public class CreateBillboards {
    static JInternalFrame window = new JInternalFrame("Create Billboard", false, false, true);
    static File fileChosen;
    public static JTextField input1 = new JTextField(), input2 = new JTextField(),
            informationColourInput = new JTextField(), textDisplayColour = new JTextField(),
            informationTextColor = new JTextField(), backgroundDisplayColour = new JTextField(),
            imageURL = new JTextField();
    public static JInternalFrame createBillboards() {
        JPanel mainHeading = new JPanel();
        JPanel billboardTextPanel = new JPanel();
        JPanel billboardColorPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        window.setSize(600, 500);
//        billboardTextPanel.setSize(300, 200);
        window.setLocation((ControlPanel.WINDOWWIDTH/2) - 300, (ControlPanel.WINDOWHEIGHT/2) - 200);

//        Heading - "Requirements"
        JLabel requirements = new JLabel("Requirements");
        requirements.setVerticalTextPosition(JLabel.TOP);
        requirements.setHorizontalTextPosition(JLabel.LEFT);
        requirements.setFont(ControlPanel.titleFont);

//        Heading - "Title"
        JLabel title = new JLabel("Billboard Title");
        title.setVerticalTextPosition(JLabel.CENTER);
        title.setHorizontalTextPosition(JLabel.LEFT);

        //        Heading - "Text"
        JLabel text = new JLabel("Billboard Text");
        text.setVerticalTextPosition(JLabel.CENTER);
        text.setHorizontalTextPosition(JLabel.LEFT);

//        Information Tag
        JLabel informationTextLabel = new JLabel("Information Text");
        text.setVerticalTextPosition(JLabel.CENTER);
        text.setHorizontalTextPosition(JLabel.LEFT);
        JButton informationColourButton = new JButton("Information Text Colour");
        informationTextColor.setBackground(Color.BLACK);

//        Heading - "Select Background Image"
        JLabel selectBG = new JLabel("Select Background Image");
        selectBG.setVerticalTextPosition(JLabel.BOTTOM);
        selectBG.setHorizontalTextPosition(JLabel.LEFT);
//        Button - Browse
        JButton imageSelect = new JButton("Browse", new ImageIcon("src/controlpanel/resources/browseicon.png"));
        imageSelect.setBounds(100,100,140,40);

        //  Buttons/Label - Colour Picker text and background
        JButton textColourPickerButton = new JButton("Text Colour");
        textDisplayColour.setBackground(Color.BLACK);

        JButton backgroundColourPickerButton = new JButton("Background Colour");
        backgroundDisplayColour.setBackground(Color.WHITE);

        // URL/Text Box
        JLabel linkURL = new JLabel("Image URL");

//        Buttons - Save and Preview
        JButton saveBB = new JButton("Save", new ImageIcon("src/controlpanel/resources/save.png"));
        saveBB.setBounds(100,100,140,40);

        JButton previewBB = new JButton("Preview", new ImageIcon("src/controlpanel/resources/preview.png"));
        previewBB.setBounds(100,100,140,40);

//        Add items to GUI
        window.setLayout(new GridLayout(4,1));
        mainHeading.add(requirements);
        window.add(mainHeading);
        window.add(billboardTextPanel);
        window.add(billboardColorPanel);
        window.add(buttonPanel);

        billboardTextPanel.setLayout(new GridLayout(3,2));
        billboardTextPanel.add(title);
        billboardTextPanel.add(input1);
        billboardTextPanel.add(text);
        billboardTextPanel.add(input2);
        billboardTextPanel.add(informationTextLabel);
        billboardTextPanel.add(informationColourInput);

        billboardColorPanel.setLayout(new GridLayout(4,2));
        billboardColorPanel.add(textColourPickerButton);
        billboardColorPanel.add(textDisplayColour);
        billboardColorPanel.add(informationColourButton);
        billboardColorPanel.add(informationTextColor);
        billboardColorPanel.add(backgroundColourPickerButton);
        billboardColorPanel.add(backgroundDisplayColour);

        buttonPanel.setLayout(new GridLayout(3,2));
        buttonPanel.add(linkURL);
        buttonPanel.add(imageURL);
        buttonPanel.add(selectBG);
        buttonPanel.add(imageSelect);
        buttonPanel.add(saveBB);
        buttonPanel.add(previewBB);

//        Preview window - Viewer.java

        textColourPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColourPicker(textDisplayColour, Color.BLACK);
            }
        });

        informationColourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    ColourPicker(informationTextColor, Color.BLACK);
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
                String title = input1.getText();
                if (title != null && !title.isEmpty() && !title.equals("")) {
                    String messageText = input2.getText();
                    String messageColour = RGBToHex(textDisplayColour.getBackground());
                    String image = imageURL.getText();
                    String informationText = informationColourInput.getText();
                    String informationColour = RGBToHex(informationTextColor.getBackground());
                    String backgroundColour = RGBToHex(backgroundDisplayColour.getBackground());

                    int testImageOrURL = URLOrImageFileOrNone(fileChosen, image);
                    if (testImageOrURL == 1) {
                        image = Controller.CreateMD5(fileChosen);
                    }
                    try {
                        Billboard temp = new Billboard(title, messageText, messageColour, image, testImageOrURL, informationText, informationColour, backgroundColour);
                        Controller.CreateBillboard(temp);
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }

                } else {
                    DialogWindow.ShowErrorPane("Please put a title in the title box", "Error No Title");
                }
            }
        });

        imageSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser browseImage = new JFileChooser((FileSystemView.getFileSystemView().getHomeDirectory()));

                int returnInt = browseImage.showOpenDialog(null);
                if (returnInt == JFileChooser.APPROVE_OPTION) {
                    fileChosen = browseImage.getSelectedFile();
//                    System.out.println(fileChosen.getAbsolutePath());
                }
            }
        });
        previewBB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = input1.getText();
                String messageText = input2.getText();
                String messageColour = "#" + RGBToHex(textDisplayColour.getBackground());
                String image = imageURL.getText();
                String informationText = informationColourInput.getText();
                String informationColour = "#" + RGBToHex(informationTextColor.getBackground());
                String backgroundColour = "#" + RGBToHex(backgroundDisplayColour.getBackground());
                int testImageOrURL = URLOrImageFileOrNone(fileChosen, image);
                if (testImageOrURL == 1) {
                    image = Controller.CreateMD5(fileChosen);
                }
                try {
                    Viewer.Renderer(new Billboard(title, messageText, messageColour, image, testImageOrURL, informationText, informationColour, backgroundColour), true);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
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
    }

    /**
     *  RGBToHex takes the ATW.Color which contains:
     *  e.g. java.awt.Color[r=102,g=0,b=102] by using
     *  it converts each colour to hex.
     *
     * @param RGB       File to be used for billboard image
     * @return String   Returns the AWT.Color to Hex String
     */
    private static String RGBToHex(Color RGB) {
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

    /**
     *  URL / Image selector. Detects if either a URL, File or no
     *  input was used for the image section of the create billboard
     *  form.
     *
     * @param imageFile File to be used for billboard image
     * @param urlImage  URL to be used for billboard image
     * @return int      -1 No url or image is given or \n 0 URl was given or \n 1 File was given
     */
    private  static int URLOrImageFileOrNone(File imageFile, String urlImage) {
        if (imageFile != null || urlImage.equals("") == false) {
            if (urlImage.contains("www") || urlImage.contains("http") || urlImage.contains("/")) {
//                System.out.println(urlImage);
                return 0;
            }
            if (imageFile  != null) {
//                System.out.println(imageFile.getAbsolutePath());
                return 1;
            }
        }
        return -1;
    }
}
