package viewer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

// this class stores all attributes of component on the billboard

/**
 * Class stores all attributes of component on the billboard
 */
class billboardDecor{
    public String type, colour, content; // the class has three attributes stored

    /**
     * Scrubs an XML file for tags relating to colour.
     *
     * @param tags  XML file tags.
     * @return      Value from XML.
     */
    String colourFinder(String tags){ // this function is used to find if there is a colour in the xml object
        if (tags.contains("background=")) // if there is a "background" tag
        {
            return tags.substring((tags.indexOf("background=") + 12), (tags.indexOf("background=") + 19)); // there will be a colour to find
        }

        if (tags.contains("colour=")) // if there is a "colour" tag
        {
            return tags.substring((tags.indexOf("colour=") + 8), (tags.indexOf("colour=") + 15)); // then there will be a colour to find
        }

        // otherwise return null
        return null;
    }

    /**
     * Detects the type of tag used out of 4 possible types.
     * then returns the type detected.
     * <ul>
     *     <li>information</li>
     *     <li>message</li>
     *     <li>picture</li>
     *     <li>billboard</li>
     * </ul>
     *
     *
     * @param tags  XML file tags.
     * @return type Value from XML.
     * @return null If no type was found.
     */
    String typeFinder(String tags){
        String[] objectTypes = {"information", "message", "picture", "billboard"}; // there are 4 possible types

        for (String type: objectTypes){ // check in the tags for each object type
            if (tags.contains("<" + type )) // if the tags include a tag with one of the types in it
            {
                return type; // return the object type it is
            }
        }

        return null; // return null if none of these are found
    }

    /**
     *
     * @param tags  XML file tags.
     * @return
     */
    String contentFinder(String tags){
        if (tags.contains("<information") || tags.contains("<message")) // if it is information or a message
        {
            return tags.substring(tags.indexOf(">") + 1, tags.indexOf("</")); // retrieve the content
        }

        else if (tags.contains("<picture")) // if it a picture
        {
            return tags.substring(tags.indexOf('"')+1, tags.indexOf('"', tags.indexOf('"') + 1)); // retrieve the url or base64 stored in the quotation marks
        }

        return null; // return null if no content is found
    }

    /**
     * Set objects as variables.
     * @param tags  XML tags.
     */
    public billboardDecor(String tags) // set the objects varaibles
    {
        type = typeFinder(tags); // generate the type
        colour = colourFinder(tags); // generate the colour
        content = contentFinder(tags); // generate the content
    }
}

/**
 *
 */
public class viewer{
    /**
     * Reads from the XML file to create a billboard object.
     * Billboard xml file looks like the following:
     * <code>
     *     <?xml version="1.0" encoding="UTF-8"?>
     *      <billboard background="#8996FF">
     *          <picture url="https://cloudstor.aarnet.edu.au/plus/s/5fhToroJL0nMKvB/download" />
     *      </billboard>
     * </code>
     * @param filePath          File path for XML file.
     * @return BillboardObjects
     * @throws IOException
     */
    public static LinkedHashSet<billboardDecor> xmlReader(String filePath) throws IOException {
        BufferedReader reader; // buffered reader to read the xml file line by line
        LinkedHashSet<String> RawBillboardObjects = new LinkedHashSet<>(); // make linkedhashset to store all the tags
        LinkedHashSet<billboardDecor> BillboardObjects = new LinkedHashSet<>(); // make linkedhashset to store all objects

        try { // try reading the xml file
            reader = new BufferedReader(new FileReader(filePath)); 
            String line = reader.readLine(); // store the individual line in a string 

            while (line != null) { // when there are more lines of the xml file
                if (!line.equals("</billboard>") && !line.contains("?xml version")) // check if the line stores object
                {
                    RawBillboardObjects.add(line); // if line important add it to linked hashset
                }

                line = reader.readLine(); // set the string to the next line
            }

            reader.close(); // close the reader when no more lines left

        } catch (IOException e) { // throw an exception if the xml can't be read
            e.printStackTrace();
        }

        for (String tags: RawBillboardObjects) { // for each valid line in the xml
            BillboardObjects.add(new billboardDecor(tags)); // convert it to an object for the billboard to display
        }
        
        return BillboardObjects; // return the set including all billboard objects
    }

    /**
     * JFrame setup for viewer. Adds <i>widgets</i> grabbed via the XML data for a specific
     * billboard and creates JFrame widgets accordingly.
     *
     * @param widgets       XML tags.
     * @see LinkedHashSet
     * @throws IOException
     */
    public static void SetupScreen(LinkedHashSet<billboardDecor> widgets) throws IOException {
        GridBagConstraints c = new GridBagConstraints(); // set up constraints 

        KeyListener kl=new KeyAdapter()
        {
            public void keyPressed(KeyEvent evt)
            {

                //If someone click Esc key, this program will exit
                if(evt.getKeyCode()==KeyEvent.VK_ESCAPE)
                {
                    System.exit(0);
                }

            }

        };

        JFrame f=new JFrame();//creating instance of JFrame
        JPanel p = new JPanel();//creating instance of jpanel
        f.addKeyListener(kl);//include esc listener
        p.setLayout(new GridBagLayout());//set panel layout to place objects above eachother in the centre 
        ArrayList<JComponent> cps = new ArrayList<>();//components to add to the billboard 

        f.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }


        }); // if the screen is clicked exit 

        for (billboardDecor decoration: widgets) { // instantiate all of the objects
            if (decoration.type == "message") 
            { // add large font label for a message
                cps.add(new JLabel("<html><h1><font size=\"45\" color = " + decoration.colour +">"+ decoration.content +"</font></h1></html>"));
            }

            else if (decoration.type == "information")
            { // add small font label for information
                cps.add(new JLabel("<html><h1><font color = " + decoration.colour +">"+ decoration.content +"</font></h1></html>"));
            }

            else if (decoration.type == "picture")
            {

                if (decoration.content.contains("http")) // if the picture is a url
                {
                    URL url; 
                    Image image = null;
                    try {
                        url = new URL(decoration.content); // convert string to url
                        image = ImageIO.read(url); // read the url to image
                    } catch (MalformedURLException ex) {
                        System.out.println("Malformed URL");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cps.add(new JLabel((new ImageIcon(image)))); // create image stored in label
                }

                else{
                    byte[] imgBytes = Base64.getDecoder().decode(decoration.content); //convert the string to base64
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgBytes)); // decode the base64 bytes into image
                    cps.add(new JLabel((new ImageIcon(image)))); // create image stored in label
                }
            }

            else if (decoration.type == "billboard") // if its a billboard
            {
                if (decoration.colour != null) // change the 
                {
                    p.setBackground(Color.decode(decoration.colour));
                }
            }
        }

        for (JComponent wid: cps)
        {
            c.gridy++; // place each component on its own line
            p.add(wid, c); // add all the widgets 
        }

        f.getContentPane().add(p, "Center"); // place the panel centred

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit the app when closed
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); // true fullscreen it 
        f.setUndecorated(true);
        f.setVisible(true); // show gui
    }

    public static void main(String[] args) throws IOException {
        SetupScreen(xmlReader("billboardsExamples/15.xml")); // call the xml reader and give return to setupscreen
    }
}
