package resources;

import java.io.IOException;
import java.util.LinkedHashSet;

public class customXMLfile{
    public String fileOutput;

    /**
     *
     * todo find a more unique way of naming the file, current implementation is limited
     * @author              https://stackoverflow.com/questions/17853541/java-how-to-convert-a-xml-string-into-an-xml-file
     * @param xml           string in XML format to be written to an XML file
     * @throws IOException
     */
    public static void stringToDom(String xml) throws IOException {
        java.io.FileWriter fw = new java.io.FileWriter("temp.xml");
        fw.write(xml);
        fw.close();
    }

    /**
     *
     * @param messageText
     * @param messageColour
     * @param informationText
     * @param informationColour
     * @param billboardImage
     * @param billboardBackgroundColour
     * @throws IOException
     */
    public customXMLfile(String messageText, String messageColour,
                         String informationText, String informationColour,
                         String billboardImage, String billboardBackgroundColour)
            throws IOException
    {
        CreateFileContents(messageText, messageColour, informationText, informationColour, billboardImage, billboardBackgroundColour);
    }

    /**
     *
     * @param messageText
     * @param messageColour
     * @param informationText
     * @param informationColour
     * @param billboardImage
     * @param billboardBackgroundColour
     * @return
     * @throws IOException
     */
    public static void CreateFileContents(String messageText, String messageColour,
                                            String informationText, String informationColour,
                                            String billboardImage, String billboardBackgroundColour)
            throws IOException
    {
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

        stringToDom(xmlFile);
    }
}

