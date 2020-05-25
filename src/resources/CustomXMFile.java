package resources;

import java.io.IOException;
import java.util.LinkedHashSet;

public class CustomXMFile {

    /**
     *
     * todo find a more unique way of naming the file, current implementation is limited
     * @author              https://stackoverflow.com/questions/17853541/java-how-to-convert-a-xml-string-into-an-xml-file
     * @param xml           string in XML format to be written to an XML file
     * @param title          Use the billboard title as the file name
     * @return String       Returns File Location
     * @throws IOException
     */
    public static String StringToDom(String xml, String title) throws IOException {
        String fileLocation = "BillboardDesign/" + title + ".xml";
        java.io.FileWriter fw = new java.io.FileWriter(fileLocation);
        fw.write(xml);
        fw.close();
        return fileLocation;
    }

    /**
     *
     * @param billboard         Is a class object of Billboard
     * @return String           Return the File location
     * @throws IOException
     */
    public static String CreateFileContents(Billboard billboard) throws IOException {
        String billboardTitle = billboard.getTitle();
        String messageText = billboard.getMessageText();
        String messageColour = billboard.getMessageColour();
        String informationText = billboard.getInformationText();
        String informationColour = billboard.getInformationColour();
        String billboardImage = billboard.getImage();
        String billboardBackgroundColour = billboard.getBackgroundColour();
        String xmlFile = "";
        LinkedHashSet<String> xmlLines = new LinkedHashSet<>(); // make linkedhashset to store all the lines

        xmlLines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        if (billboardBackgroundColour != null){
            xmlLines.add("<billboard background = \""+ billboardBackgroundColour +"\">");
        }

        else{
            xmlLines.add("<billboard>");
        }

        if (messageText!=null){
            if (messageColour!=null)
            {
                xmlLines.add("<message colour =\"" + messageColour +"\">"+ messageText + "</message>");
            }

            else
            {
                xmlLines.add("<message>"+ messageText + "</message>");
            }
        }

        if (billboardImage!=null)
        {
            if (billboardImage.contains("http"))
            {
                xmlLines.add("<picture url \"" + billboardImage + "\"/>");
            }

            else
            {
                xmlLines.add("<picture data \"" + billboardImage + "\"/>");
            }
        }

        if (informationText!=null)
        {
            if (informationColour!=null)
            {
                xmlLines.add("<information colour =\"" + informationColour +"\">"+ informationText + "</information>");
            }

            else
            {
                xmlLines.add("<information>"+ informationText + "</information>");
            }
        }

        xmlLines.add("</billboard>");

        for (String line:xmlLines) {
            xmlFile = xmlFile + line + "\n";
        }

        return StringToDom(xmlFile, billboardTitle);
    }
}

