package voiceapp.idea.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.sql.Timestamp;

/**
 * Class containing all properties of an idea.
 */
@XmlRootElement
public class Idea {
    private int ideaID;
    private String title;
    private String description;
    private String date;
    private String location;
    private int proposerUserID;
    private int ideaStatus;

    public Idea(int ideaID, String title, String description, String date, String location, int proposerUserID, int ideaStatus) {
        this.ideaID = ideaID;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.proposerUserID = proposerUserID;
        this.ideaStatus = ideaStatus;
    }

    public Idea() {
    }

    public int getIdeaID() {
        return ideaID;
    }

    public void setIdeaID(int ideaID) {
        this.ideaID = ideaID;
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getProposerUserID() {
        return proposerUserID;
    }

    public void setProposerUserID(int proposerUserID) {
        this.proposerUserID = proposerUserID;
    }

    public int getIdeaStatus() {
        return ideaStatus;
    }

    public void setIdeaStatus(int ideaStatus) {
        this.ideaStatus = ideaStatus;
    }

}
