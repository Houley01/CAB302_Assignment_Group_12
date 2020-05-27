package viewer;

import controlpanel.DialogWindow;
import controlpanel.controller;
import org.xml.sax.SAXException;
import resources.Billboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import static controlpanel.controller.connectionToServer;
import static resources.CustomXMFile.ReadXMLFile;
import static server.Server.RequestBillboardList;


// this class stores all attributes of component on the billboard

/**
 *
 */
public class viewer extends JPanel{

    public static final Font titleFont = new Font("Ariel", Font.BOLD, 20);
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Getting screen resolution (Main monitor / primary monitor)
    public static int w = (int) Math.round(screenSize.getWidth());                    // Res Width
    public static int h = (int) Math.round(screenSize.getHeight());                   // Res Height


    /**
     * Connects to the server and retrieves the appropriate billboard via XML reader.
     * Todo: Update billboard viewer to the currently 'scheduled' billboard (Change query on Server side).
     *
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws NoSuchFieldException
     */
    public static Billboard getBillboardInfo(Socket client) throws IOException, SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, NoSuchFieldException {
        try
        {
            // Initializing connection
            OutputStream outputStream = client.getOutputStream();   // Server output to user
            InputStream inputStream = client.getInputStream();      // User input to server

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("ViewerRequest");
            send.flush();                                                         // Have to flush to be able to read data
            receiver.read();                                                      // Start reading? Java throws an error if this isn't here.
            List<String> output = (List<String>) receiver.readObject();           // Putting server data into list.

            // We need this information of the XML reader.
            // It requires:
            // A: Location to file
            // B: A title (filename)

            String fileLocation = output.get(0);                                  // Reading server response.
            String file = fileLocation.split("/")[1];                      // Grab XML file.
            String fileName = file.replace(".xml", "");        // Java didn't like split here.

            Billboard billboard = ReadXMLFile(new File(fileLocation), fileName);  // ReadXML returns a Billboard class

            System.out.println(billboard);

            // Closing streams and socket
            send.close();
            receiver.close();
            client.close();
            return billboard;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null; // If it some how escapes the try catch.
    }

    public static void errorMessage()
    {
//        Some JFrame shit for error message
    }

    private static <Int> String formatText(String colour, Int size, String text)
    {
        return "<html><h2 font=\""+ size + "px\" color="+colour+">"+text+"</h2></html>";
    }


    /**
     * Java Swing renderer (just renders out all the info supplied)
     * @see //getBillboardInfo
     */
    private static void renderer(Billboard currBill) throws IOException // Private because no point having it public
    {

        GridBagConstraints c = new GridBagConstraints();
        Color bgColour = Color.decode(currBill.getBackgroundColour());                      // AWT readable colour
        JFrame frame = new JFrame("Billboard viewer");                                 // Frame title
        JPanel panel = new JPanel();                                                        // Pane (supply with content later)

        listeners(frame);
        panel.setLayout(new GridBagLayout());

        ArrayList<JComponent> cps = new ArrayList<>();                                                                   // List to add JFrame components

        cps.add(new JLabel(formatText(currBill.getMessageColour(), w / 50, currBill.getMessageText())));            // Message
        cps.add(new JLabel(formatText(currBill.getInformationColour(), w / 80, currBill.getInformationText())));    // Info


        /**
         * Adding image
         * Todo make it scale and fit the center
         */
        if(currBill.getImageOrUrl() >= 0)
        {
            /**
             * If user supplied an Image file.
             */
            ImageIcon imageIcon = null;
            if(currBill.getImageOrUrl() == 0)
            {
                byte[] imgBytes = Base64.getDecoder().decode(currBill.getImage());
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgBytes));
                imageIcon = new ImageIcon(image);
            }
            else if (currBill.getImageOrUrl() == 1)
            {
                URL url = new URL(currBill.getImage());
                Image image = ImageIO.read(url);
                imageIcon = new ImageIcon(image);
            }
            JLabel imageRender = new JLabel(imageIcon);
            imageRender.setSize(w, h);
            frame.getContentPane().add(imageRender, "Center");
        }

        panel.setBackground(bgColour);                                      // Background colour

        for (JComponent wid: cps)
        {
            c.gridy++;          // place each component on its own line
            panel.add(wid, c);  // add all the widgets
        }

        System.out.println(cps);

        frame.setSize(w, h);                                                 // Update frame to match screen size.
        frame.getContentPane().add(panel, "Center");              // place the panel centred
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                // exit the app when closed
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);                       // true fullscreen it
        frame.setUndecorated(true);
        frame.setVisible(true);                                              // show gui


//        frame.pack();

    }

    /**
     * Listeners for the viewer including the escape event and mouse events.
     *
     * @param frame     JFrame window of the viewer
     */
    private static void listeners(JFrame frame)
    {
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.out.println("Escape key pressed");
                    System.exit(0);
                }
            }
        });
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK || e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK || e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK)
                {
                    System.exit(0);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK || e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK || e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK)
                {
                    System.exit(0);
                }
            }
            // Rest of this code is redundant but is required to run the listener.
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException, ParserConfigurationException, SAXException, NoSuchFieldException {
        Socket client = connectionToServer();                   // Socket connection to the server

        // This goes into a loop for every 15 seconds
        Billboard currBill = getBillboardInfo(client);          // Get billboard information
        renderer(currBill);                                     // JFrame renderer\
    }
}
