package viewer;
import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.*;

class object{
    public String type, colour, content;

    String colourFinder(String tags){
        if (tags.contains("colour="))
        {
            return tags.substring((tags.indexOf("colour=") + 7), (tags.indexOf("colour=") + 16));
        }

        return null;
    }

    String typeFinder(String tags){
        String[] objectTypes = {"information", "message", "picture", "billboard"};

        for (String type: objectTypes){
            if (tags.contains("<" + type))
            {
                return type;
            }
        }

        return null;
    }

    String contentFinder(String tags){
        if (tags.contains("<information") || tags.contains("<message"))
        {
            return tags.substring(tags.indexOf(">") + 1, tags.indexOf("</"));
        }

        else if (tags.contains("<picture"))
        {
            return tags.substring(tags.indexOf('"'), tags.indexOf('"', tags.indexOf('"') + 1) + 1);
        }

        return null;
    }

    public object(String tags)
    {
        type = typeFinder(tags);
        colour = colourFinder(tags);
        content = contentFinder(tags);
    }
}

public class viewer{
    public static LinkedHashSet<object> xmlReader(String filePath) throws IOException {
        BufferedReader reader;
        LinkedHashSet<String> RawBillboardObjects = new LinkedHashSet<>();
        LinkedHashSet<object> BillboardObjects = new LinkedHashSet<>();

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            while (line != null) {
                if (!line.equals("</billboard>") && !line.contains("?xml version"))
                {
                    RawBillboardObjects.add(line);
                }

                line = reader.readLine();
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String tags: RawBillboardObjects) {
            BillboardObjects.add(new object(tags));
        }

        for(object stuff: BillboardObjects){
            System.out.println(stuff.colour + stuff.type + stuff.content);
        }

        return BillboardObjects;
    }

    public static void SetupScreen(LinkedHashSet<object> widgets){
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
        f.addKeyListener(kl);
        f.setLayout(new GridBagLayout());
        ArrayList<JComponent> cps = new ArrayList<>();

        f.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }


        });

        for (object decoration: widgets) {
            if (decoration.type == "message")
            {
                cps.add(new JLabel("<html><h2>"+ decoration.content +"</h2></html>"));
            }

            else if (decoration.type == "information")
            {
                cps.add(new JLabel("<html><p>"+ decoration.content +"</p></html>"));
            }

            else if (decoration.type == "picture")
            {
                //cps.add()
            }

            else if (decoration.type == "billboard")
            {
                if (decoration.colour != null)
                {
                    f.setBackground(Color.decode(decoration.colour));
                }
            }
        }

        for (JComponent wid: cps)
        {
            f.add(wid, new GridBagConstraints());
        }

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setUndecorated(true);
        f.setVisible(true);
    }

        public static void main(String[] args) throws IOException {
        SetupScreen(xmlReader("billboardsExamples/12.xml"));
    }
}
