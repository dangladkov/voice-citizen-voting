package voiceapp.shop.messages;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Message containing data necessary for creating a benefit.
 */
@XmlRootElement
public class BenefitCreateMsg {
    private String title;
    private String description;
    private int points;
    private String category;
    private BenefitAttributesMsg[] attributes;

    public BenefitCreateMsg(String title, String description, int points, String category, BenefitAttributesMsg[] attributes) {
        this.title = title;
        this.description = description;
        this.points = points;
        this.category = category;
        this.attributes = attributes;
    }

    public BenefitCreateMsg() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BenefitAttributesMsg[] getAttributes() {
        return attributes;
    }

    public void setAttributes(BenefitAttributesMsg[] attributes) {
        this.attributes = attributes;
    }
}
