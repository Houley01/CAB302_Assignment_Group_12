package resources;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.io.IOException;
import java.util.LinkedHashSet;

import javax.xml.parsers.*;


public class CustomXMFile {

    /**
     *  Creates an XML file from a string format.
     *  Input has to be in a valid XML format for this conversation to work because
     *  there's no check to see if the XML input is valid XML.
     *  This is a huge security flaw. Too bad!
     *
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
     * Formats billboard content into readable XML string.
     * @see StringToDom
     *
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
            xmlLines.add("<billboard background = \"#"+ billboardBackgroundColour +"\">");
        }

        else{
            xmlLines.add("<billboard>");
        }

        if (messageText!=null){
            if (messageColour!=null)
            {
                xmlLines.add("<message colour = \"#" + messageColour +"\">"+ messageText + "</message>");
            }

            else
            {
                xmlLines.add("<message>"+ messageText + "</message>");
            }
        }

        if (billboardImage!=null)
        {
            if (billboardImage.contains("http") || billboardImage.contains("www"))
            {
                xmlLines.add("<picture url = \"" + billboardImage + "\"/>");
            }

            else
            {
                xmlLines.add("<picture data = \"" + billboardImage + "\"/>");
            }
        }

        if (informationText!=null)
        {
            if (informationColour!=null)
            {
                xmlLines.add("<information colour = \"#" + informationColour +"\">"+ informationText + "</information>");
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

//    NOTES:: Returns a billboard object
//    NOTES:: takes Object File
//    NOTES:: Takes String Title - Title of the billboard
    public static Billboard ReadXMLFile(File file, String title) throws IOException, ParserConfigurationException, SAXException {
        String messageText = "";
        String messageColour = "";
        String image = "";
        int imageOrUrlOrNone = -1;
        String informationText = "";
        String informationColour = "";
        String backgroundColour = "";


        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        document.normalize();

//        Billboard Tag
//        Check to see if the tag is in the XML File
        if (document.getElementsByTagName("billboard").getLength() > 0) {
            NodeList nodeList = document.getElementsByTagName("billboard");
            Node node = nodeList.item(0);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
//                System.out.println(element.getAttribute("background"));
                backgroundColour = element.getAttribute("background");
            }
        }

//        Message Tag
//        Check to see if the tag is in the XML File

        if (document.getElementsByTagName("message").getLength() > 0) {
            NodeList nodeList = document.getElementsByTagName("message");
            Node node = nodeList.item(0);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
//                System.out.println(element.getTextContent());
                messageText = element.getTextContent();
//                System.out.println(element.getAttributes().getNamedItem("colour").getNodeValue());
                Node temp = element.getAttributes().getNamedItem("colour");
                if(temp != null)
                {
                    messageColour = element.getAttributes().getNamedItem("colour").getNodeValue() ;
                }
                else
                {
                    messageColour = "#000000";
                }
            }
        }

//        Picture Tag
//        Check to see if the tag is in the XML File
        if (document.getElementsByTagName("picture").getLength() > 0) {
            NodeList nodeList = document.getElementsByTagName("picture");
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
//                System.out.println(element.getAttributes().getNamedItem("data").getNodeValue());

                if (element.hasAttribute("data")) {
//                    System.out.println("has data");
                    image = element.getAttributes().getNamedItem("data").getNodeValue();
                    imageOrUrlOrNone = 0;
                }

                if (element.hasAttribute("url")) {
                    image = element.getAttributes().getNamedItem("url").getNodeValue();
                    imageOrUrlOrNone = 0;
//                    System.out.println("has url");
                }
            }
        }

//        Information Tag
//        Check to see if the tag is in the XML File
        if (document.getElementsByTagName("information").getLength() > 0) {
            NodeList nodeList = document.getElementsByTagName("information");
            Node node = nodeList.item(0);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
//                System.out.println(element.getTextContent());
                informationText = element.getTextContent();
//                System.out.println(element.getAttributes().getNamedItem("colour").getNodeValue());
                Node temp = element.getAttributes().getNamedItem("colour");
                if(temp != null)
                {
                    informationColour = element.getAttributes().getNamedItem("colour").getNodeValue();
                }
                else
                {
                    informationColour = "#ffffff";
                }
            }
        }

        return new Billboard(title, messageText, messageColour, image, imageOrUrlOrNone, informationText, informationColour, backgroundColour);
    }
}

