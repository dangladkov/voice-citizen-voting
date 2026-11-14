package voiceapp.shop.model;

public class Benefit {

    private int benefitID;
    private String title;
    private String description;
    private int points;
    private String category;
    private String attributes;



    public Benefit(int itemID, String title, String description, int points, String category, String attributes) {
        this.benefitID = itemID;
        this.title = title;
        this.description = description;
        this.points = points;
        this.category = category;
        this.attributes = attributes;

    }

    public Benefit() {}

    public int getBenefitID() {
        return benefitID;
    }

    public void setBenefitID(int benefitID) {
        this.benefitID = benefitID;
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }



}
