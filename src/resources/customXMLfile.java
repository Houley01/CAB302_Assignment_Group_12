package resources;

import java.util.LinkedHashSet;

public class customXMLfile{
    public String fileOutput;

    public customXMLfile(String messageText, String messageColour,
                         String informationText, String informationColour,
                         String billboardImage, String billboardBackgroundColour){
        fileOutput = CreateFileContents(messageText, messageColour,
                informationText, informationColour,
                billboardImage, billboardBackgroundColour);
    }

    public static String CreateFileContents(String messageText, String messageColour,
                                            String informationText, String informationColour,
                                            String billboardImage, String billboardBackgroundColour)
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

        return xmlFile;
    }
}
