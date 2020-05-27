package resources;

import java.io.Serializable;

/**
 * Class for billboards to inherit from.
 */
public class Billboard implements Serializable {
    private String title;
    private String messageText;
    private String messageColour;
    private String image;
    private int imageOrUrlOrNone; // -1 NULL, 0 = Image/MD5, 1 = URL
    private String informationText;
    private String informationColour;
    private String backgroundColour;

    /**
     * Method for defining the current instance of a billboard.
     *
     * @param title             Title of the billboard.
     * @param messageText       Message for the billboard.
     * @param messageColour     Message colour.
     * @param image             Image to display.
     * @param imageOrUrlOrNone  Bool relating to URL or image. -1 Null, 0 = Image/MD5, 1 = URL
     * @param informationText   Information about billboard.
     * @param informationColour Colour of the information text.
     * @param backgroundColour  Background colour.
     */
    public Billboard(String title, String messageText, String messageColour, String image, int imageOrUrlOrNone, String informationText, String informationColour, String backgroundColour) {
        this.title = title;
        this.messageText = messageText;
        this.messageColour = messageColour;
        this.image = image;
        this.imageOrUrlOrNone = imageOrUrlOrNone;
        this.informationText = informationText;
        this.informationColour = informationColour;
        this.backgroundColour = backgroundColour;
    }

    /**
     * Debugging tool to print all current billboard information into the console.
     */
    public void PrintBillboardInformation() {
        System.out.println(title);
        System.out.println(messageText);
        System.out.println(messageColour);
        System.out.println(image);
        System.out.println(imageOrUrlOrNone);
        System.out.println(informationText);
        System.out.println(informationColour);
        System.out.println(backgroundColour);
    }

    /**
     * Get the title of the billboard.
     * @return title        Current title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set or update the title of the current billboard.
     * @param title         Title to change too or set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the billboard message.
     * @return Current message text
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Set / update current message text.
     * @param messageText   Message to change too or set.
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * Get the current message colour.
     * @return message colour.
     */
    public String getMessageColour() {
        return messageColour;
    }

    /**
     * Set / update current message colour.
     * @param messageColour Hex colour to update too.
     */
    public void setMessageColour(String messageColour) {
        this.messageColour = messageColour;
    }

    /**
     * Get the current image on the billboard
     * @return image.
     */
    public String getImage() {
        return image;
    }
    /**
     * Set / update the current image.
     * @param image Image to change too.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Get if the supplied image was an image, url or nothing.
     * @return imageOrUrlOrNone
     */
    public int getImageOrUrl() {
        return imageOrUrlOrNone;
    }

    /**
     * Set / update the current imageOrUrl status.
     * Note: -1 = Null, 0 = Image/MD5, 1 URL.
     * @param imageOrUrl    int to update too.
     */
    public void setImageOrUrl(int imageOrUrl) {
        this.imageOrUrlOrNone = imageOrUrl;
    }

    /**
     * Get the current information text.
     * @return information text
     */
    public String getInformationText() {
        return informationText;
    }

    /**
     * Change / update information text.
     * @param informationText   Information text to update too.
     */
    public void setInformationText(String informationText) {
        this.informationText = informationText;
    }

    /**
     * Get the current information hex colour.
     * @return Hex colour.
     */
    public String getInformationColour() {
        return informationColour;
    }

    /**
     * Set / Update the information colour.
     * @param informationColour Hex colour value.
     */
    public void setInformationColour(String informationColour) {
        this.informationColour = informationColour;
    }

    /**
     * Get the current background colour.
     * @return background colour.
     */
    public String getBackgroundColour() {
        return backgroundColour;
    }
}
