package voiceapp.project.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.awt.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Represent the data structure of a project.
 */
@XmlRootElement
public class Project {
    private int projectID;

    private int ideaID;

    private String title;

    private String description;

    private Date date;

    private String location;

    private int proposedBy;

    private Timestamp deadline;

    private int pointsToAward;

    private int projectStatus;

    private String categories;

    private List<Question> questions;


    public Project(int projectID, int ideaID, String title, String description, Date date, String location, int proposedBy, Timestamp deadline, int pointsToAward, int projectStatus, String categories, List<Question> questions) {
        this.projectID = projectID;
        this.ideaID = ideaID;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.proposedBy = proposedBy;
        this.deadline = deadline;
        this.pointsToAward = pointsToAward;
        this.projectStatus = projectStatus;
        this.categories = categories;
        this.questions = questions;

    }


    public int getProjectID() {
        return projectID;
    }
    public void setProjectID(int projectID) {
        this.projectID = projectID;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getProposedBy() {
        return proposedBy;
    }

    public void setProposedBy(int proposedBy) {
        this.proposedBy = proposedBy;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public int getPointsToAward() {
        return pointsToAward;
    }

    public void setPointsToAward(int pointsToAward) {
        this.pointsToAward = pointsToAward;
    }

    public int getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(int projectStatus) {
        this.projectStatus = projectStatus;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}

