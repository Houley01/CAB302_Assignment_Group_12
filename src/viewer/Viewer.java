package viewer;

import resources.Billboard;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

import static resources.CustomXMFile.ReadXMLFile;



/**
 * Viewer class. Holds majority of the logic that runs the viewer application on the users end.
 * It makes calls to the server for billboard information and formats that information into GUI
 * for the user to read also includes error messages that correlate to the status of the server
 * e.g. no billboard data or no connection to the server.
 *
 * This class is called every 15 seconds.
 */
public class Viewer extends TimerTask {

    public static final Font titleFont = new Font("Ariel", Font.BOLD, 20);
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Getting screen resolution (Main monitor / primary monitor)
    public static int w = (int) Math.round(screenSize.getWidth());                    // Res Width
    public static int h = (int) Math.round(screenSize.getHeight());                   // Res Height
    public static boolean online = false;                                             // Online / offline detector (server)
    public static JFrame frame = new JFrame("Billboard viewer");
    private static long timerRefresh = 15*1000;                                       // Refresh rate
    private static boolean previewWindow = false;


    /**
     * Code mostly originated from the controller though is slightly modified to fit the viewer.
     * The main differences to note is that there is a try catch on the socket connection itself
     * and if it succeeds it modifies the online variable to be true and if it fails a debugger
     * message is sent to the console. (Default online value is false so it will just print
     * the offline screen)
     * @return Socket connection.
     * @throws IOException
     */
    public static Socket ConnectionToServer() throws IOException {
        System.out.println("Attempting connection.");
        try {
            // Gathers the information from server.config file
            resources.GetPropertyValues properties = new resources.GetPropertyValues();
            properties.ReadPropertyFile();

            Socket client = new Socket();                                                                               // Create empty socket connection variable
            try
            {
                client = new Socket(properties.serverName,  properties.port);                                           // Populate socket with information.
                online = true;                                                                                          // Update public bool (Tell billboard system we're online)
            }
            catch (Exception e)
            {                                                                                                           // We can expect the server to be offline.
//                System.out.println("Unable to connect to server.");                                                     // (we'll flood the console with error messages if we print stack trace)
            }

            // If the server connection was made
            return client;
        } catch (IOException e) { // If something goes horribly wrong
//            e.printStackTrace();
            // Developer message
//            System.out.println("Server connection doesn't exist.");
            Socket client = new Socket();
            return client;
        }
    }


    /**
     * We assume a connection has already been main via the run method and we are supplied the socket
     * from the parameter. From this, we check to see if the server is currently online. If not we return
     * no data but if we are online we ignore this step completely.
     *
     * Then the code opens two streams 1 for input and 1 for output. We request the server for some billboard
     * information, if there is no information on any billboard we return null but if the server does give
     * us billboard information we read the file XML file that was made when the billboard was created
     * and return that object so we can use it in other methods.
     * Todo: Update billboard viewer to the currently 'scheduled' billboard (Change query on Server side).
     *
     * @param client                        Socket connection to server.
     */
    public static Billboard GetBillboardInfo(Socket client)  {
        try
        {
            if(!online)         // If the server is offline don't return anything
            {                   // and exit the function.
                return null;    // (Null can be expected from billboard)
            }
            // Initializing connection
            OutputStream outputStream = client.getOutputStream();   // Server output to user
            InputStream inputStream = client.getInputStream();      // User input to server

            ObjectOutputStream send = new ObjectOutputStream(outputStream);
            ObjectInputStream receiver = new ObjectInputStream(inputStream);

            send.writeUTF("ViewerRequest");                                   // Let the server know what data we want.
            send.flush();                                                         // Have to flush to be able to read data.
            receiver.read();                                                      // Start reading? Java throws an error if this isn't here.
            List<String> output = null;                                           // Creating empty list to put data into.
            try
            {
                output = (List<String>) receiver.readObject();                    // Putting server data into list.
                for (String item : output) {
                    System.out.println(item);
                    System.out.println();
                }
            }
            catch(Exception e)                                                    // We assume the server didn't give us any usable information
            {
                // Debugging messages
//                System.out.println("There was no billboard data retrieved from the database.");
//                System.out.println("Check to see if the server is online, or if the billboards table has any populated data");
                return null; // Return nothing
            }

            // We need this information of the XML reader.
            // It requires:
            // A: Location to file
            // B: A title (Name of the file - the extension)

            String fileLocation = output.get(0);                                  // Reading server response.
            String file = fileLocation.split("/")[1];                      // Grab XML file. We can safely assume that '/'
                                                                                  // will be present when we split the string.
            String fileName = file.replace(".xml", "");        // Java didn't like split here so we just
                                                                                  // delete the extension.
            System.out.println(fileLocation);
            System.out.println(fileName);
            Billboard billboard = ReadXMLFile(new File(fileLocation), fileName);  // ReadXML returns a Billboard class

            // Closing streams and socket
            send.close();
            receiver.close();
            client.close();

            // Return the billboard object
            return billboard;
        }
        catch(Exception e)
        {
//            e.printStackTrace();
        }
        return null; // If it some how escapes the try catch.
    }

    /**
     * Helper function to simply creating new text fields.
     * Formats the text in a Java swing friendly manner.
     *
     * @param colour    Colour of the text
     * @param size      Font size
     * @param text      Text to be rendered
     * @param <Int>     (Required for size)
     * @return          Formatted Java label
     */
    private static <Int> JLabel FormatText(String colour, Int size, String text, Boolean title)
    {
        String html = "<html><body style='text-align: center;width: %1spx'>%1s";
        int width = title ? (int) (w / 1.25) : w/2;
        Color color = Color.decode(colour);                                     // Colour of the text.
        JLabel label = new JLabel(String.format(html, width, text));             // Creating the label.
        label.setForeground(color);                                             // Setting text colour.
        label.setFont(new Font("SansSerif", Font.BOLD, (Integer) size)); // Setting font and size.
        label.setBounds(0,0,w,h);
        return label;                                                           // Returning the label.
    }

    /**
     * Elegant solution to find out if the user selected a url or image file. Sort of a redundancy in
     * case the billboard imageOrUrl property fails. Returns a true or false statement based on a regex
     * check on the input. The checker is only really checking for valid https, ftp and file links.
     *
     * @param input     Input string to be tested for a URL
     * @return bool     URL: true
     */
    private static boolean UrlTester(String input) {
        return input.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }


    /**
     * Checks to see if the input was supplied an image or url, then proceeds to build
     * an image based off either the URL string or Base64 encoded via ImageIO. This image
     * object is then returned so it can be rendered by the renderer.
     *
     * @param currBill  Billboard object (currently pulled billboard)
     * @return Buffered image from database or URL.
     * @throws IOException
     */
    public static Image ImageCalc(Billboard currBill) throws IOException {
        BufferedImage image = null;
        int storedVal = currBill.getImageOrUrl();
        if(storedVal == -1) return null;                                        // Exit if no data is presented
        // Test to see if the stored value is incorrect or mismatched.
        boolean imgOrUrl = storedVal == 0 ? false : true;                       // Re-contextualize stored value into bool.
        if((storedVal == 0) != imgOrUrl || (storedVal == 1) != imgOrUrl)        // Test to see if the stored value is correct.
        {
            imgOrUrl = UrlTester(currBill.getImage());                          // If it fails use backup method.
        }

        // Modifying images based off the URL check / stored value.
        if(imgOrUrl)                                                            // URL
        {
             URL url = new URL(currBill.getImage());                             // Grabbing the url.
             image = ImageIO.read(url);                                          // Reading the image from url.
        }
        else                                                                     // File
        {
            byte[] imgBytes = Base64.getDecoder().decode(currBill.getImage());  // Decode base64 into readable bytes
            image = ImageIO.read(new ByteArrayInputStream(imgBytes));           // then send that to the image reader.
        }
        // We do not do anything if the value is lower than -1 since we expect the negative values to be null.
        return image;                                                               // Return the image.
    }

    /**
     * Removes the previous panel stored by JFrame.
     * Code checks to see how many instances of JPanel exist inside of the frame. If equals two or more it will
     * delete the original / root panel / index 0.
     */
    private static void RemoveFrameData()
    {
        int compCount = frame.getContentPane().getComponentCount();             // Count of panels in the frame
        if(compCount > 1)                                                       // Check to see if we have more 1
        {
                Component comp = frame.getContentPane().getComponent(0);    // Get the panel that was rendered first
                frame.getContentPane().remove(comp);                            // Delete that panel
        }
        frame.revalidate();                                                     // Re-validating the frame
        frame.repaint();                                                        // Repainting the frame
    }


    /**
     * Java Swing renderer. Renders all the information given to it from the server
     * and compiles it into a fullscreen application via the report instructions.
     * The renderer also checks the public variable <b>online</b> variable to see
     * which data to render. Depending on the online state we render different things
     * to the user for example:
     * If the server is offline we render:
     * <code>
     *     Server is offline please wait...
     * </code>
     * If there was no data given from the server:
     * <code>
     *     There are currently no billboards to display.
     * </code>
     * but if the server did supply data and we have a valid socket connection we format
     * the billboard data and send it to the users GUI.
     * @param preview determine if the request was send from control panel
     */
    public static void Renderer(Billboard currBill, boolean preview) throws IOException
    {
        Color bgColour = Color.decode("#333333");                                                                       // Default color
        RemoveFrameData();

        GridBagConstraints c = new GridBagConstraints();

        JPanel panel = new JPanel();                                                                                    // Panel init
        JPanel messagePanel = new JPanel();
        JPanel imagePanel = new JPanel();
        JPanel infoPanel = new JPanel();

        panel.setSize(w, h);

        panel.setLayout(new GridBagLayout());


        listeners(frame);                                                                                               // Init the listeners

        /*
        * If the preview is true pretend the sever is online
        * */
        if (preview) {
            previewWindow = true;
            online = true;
        }
        /**
         * If not online.
         */
        if(!online)                                                                                                     // If the server is not online
        {
            JLabel offlineMessage = FormatText("#fc0335", w/30, "Server is offline please wait...", true); // Non-interactable error message
            messagePanel.add(offlineMessage);

        }
        /**
         * If no data was given from the billboard.
         */
        else if(currBill == null)                                                                                       // If there is no billboard data.
        {
            JLabel noBillboard = FormatText("#fc0335", w/30, "There are currently no billboards to display.",true);
            messagePanel.add(noBillboard);
        }
        /**
         * Else we assume we have data and the server is online.
         */
        else // Online
        {
            bgColour = Color.decode(currBill.getBackgroundColour().isEmpty() ?
                    "#333333" : currBill.getBackgroundColour());                                                        // AWT readable colour
            System.out.println(currBill.getMessageText());
            JLabel message = !currBill.getMessageText().isEmpty() || currBill.getMessageText() != "" ?
                    FormatText(currBill.getMessageColour(), w / 40, currBill.getMessageText(),true)
                    : FormatText("#000000", w/40, "", false);                                    // Creating text for the message
            JLabel info = !currBill.getInformationText().isEmpty() || currBill.getInformationText() != "" ?
                    FormatText(currBill.getInformationColour(), w / 70, currBill.getInformationText(),false)
                    : FormatText("#000000", w/70, "", false);

            info.setVerticalTextPosition(JLabel.BOTTOM);

            messagePanel.add(message);
            infoPanel.add(info);

            Image image = ImageCalc(currBill);
            if(image != null)
            {
                if(image.getHeight(null) > h/5 && image.getWidth(null) > w)
                {
                    System.out.println("BIG");
                    image = image.getScaledInstance(w/5, -1, Image.SCALE_SMOOTH);                                  // By far the easiest solution to
                                                                                                                        // stretch an image to the screen while maintaining
                                                                                                                        // aspect ratio.
                }

                JLabel scaled = new JLabel(new ImageIcon(image));
                scaled.setAlignmentX(.5f);
                scaled.setAlignmentY(.5f);
                scaled.setHorizontalAlignment(JLabel.CENTER);
                scaled.setVerticalAlignment(JLabel.CENTER);
                imagePanel.add(scaled);
            }
        }

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.ipady = h/10;
        c.anchor = GridBagConstraints.CENTER;

        messagePanel.setBackground(bgColour);
        panel.add(messagePanel, c);

        c.ipady = h/5;
        c.gridy++;
        imagePanel.setBackground(bgColour);
        panel.add(imagePanel, c);

        c.ipady = h/10;
        c.gridy++;
        infoPanel.setBackground(bgColour);
        panel.add(infoPanel, c);

        panel.setBackground(bgColour);
        frame.getContentPane().setBackground(bgColour);                                       // Background colour
        frame.setSize(w, h);                                                 // Update frame to match screen size.
        frame.getContentPane().add(panel);                                   // place the panel centred
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                // exit the app when closed
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);                       // true fullscreen it
        frame.setVisible(true);                                              // show gui
    }

    /**
     * Listeners for the viewer including the escape event and mouse events.
     * Listens for any kind of mouse click / press event on:
     * <code>
     *     Mouse1: Left Click
     *     Mouse2: Middle Click
     *     Mouse3: Right click
     * </code>
     * and listens for the escape key.
     * The code exits via System status rather than JPanel.
     *
     * @param frame     JFrame window of the viewer
     */
    private static void listeners(JFrame frame)
    {
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && previewWindow) {
                    frame.dispose(); // This is done so the preview does not close the control panel
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !previewWindow) {
                    System.exit(0);
                }
            }
        });
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK
                        || e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK ||
                        e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK) && previewWindow)
                {
                    frame.dispose(); // This is done so the preview does not close the control panel
                } else if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK ||
                        e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK ||
                        e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK)
                {
                    System.exit(0);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if ((e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK
                        || e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK
                        || e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK ) && previewWindow)
                {
                    frame.dispose();// This is done so the preview does not close the control panel
                } else if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK
                        || e.getModifiersEx() == InputEvent.BUTTON2_DOWN_MASK
                        || e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK)
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

    /**
     * Main for this function is just to initiate the timer to call the run
     * method that was extended from timer.
     * @param args
     */
    public static void main(String[] args) {
        frame.setUndecorated(true);         /**We set undecorated here since updating it in the renderer will cause errors*/
        Timer time = new Timer(false);
        time.scheduleAtFixedRate(new Viewer(), 0, timerRefresh);
    }

    /**
     * Timer event that runs every 15 seconds (or what timerRefresh is set to).
     * The code will connect to the server, request some billboard information,
     * render out to JPanel and then close the connection. Until it's called
     * again by the timer event.
     *
     * Every operation needs to be in a try catch for the code to run.
     */
    public void run()
    {

        Socket client = null;                               // Socket connection to the server
        try {
            client = ConnectionToServer();
            // This goes into a loop for every 15 seconds
            try{
                Billboard currBill = GetBillboardInfo(client);   // Get billboard information
                Renderer(currBill, false);                              // JFrame renderer
            }
            catch(Exception e)
            {
//                e.printStackTrace();
            }
            client.close(); // Close connection
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

}
