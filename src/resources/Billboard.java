package resources;

import java.io.Serializable;

public class Billboard implements Serializable {
    private String title;
    private String messageText;
    private String messageColour;
    private String image;
    private int imageOrUrlOrNone; // -1 NULL, 0 = Image/MD5, 1 = URL
    private String informationText;
    private String informationColour;
    private String backgroundColour;

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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageColour() {
        return messageColour;
    }

    public void setMessageColour(String messageColour) {
        this.messageColour = messageColour;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImageOrUrl() {
        return imageOrUrlOrNone;
    }

    public void setImageOrUrl(int imageOrUrl) {
        this.imageOrUrlOrNone = imageOrUrl;
    }

    public String getInformationText() {
        return informationText;
    }

    public void setInformationText(String informationText) {
        this.informationText = informationText;
    }

    public String getInformationColour() {
        return informationColour;
    }

    public void setInformationColour(String informationColour) {
        this.informationColour = informationColour;
    }

    public String getBackgroundColour() {
        return backgroundColour;
    }
}
