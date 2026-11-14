package voiceapp.project.messages.create;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Message containing data necessary for creating a project.
 */
@XmlRootElement
public class CreateProjectMsg {
    private int ideaID;
    private String title;
    private String description;
    private String location;
    private String date;
    private String categories;
    private String votingDeadline;
    private CreateQuestionMsg[] questions;
    private int pointsToAward;

    public CreateProjectMsg(int ideaID, String title, String description, String location, String date, String categories, String votingDeadline, CreateQuestionMsg[] questions, int pointsToAward) {
        this.ideaID = ideaID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.categories = categories;
        this.votingDeadline = votingDeadline;
        this.questions = questions;
        this.pointsToAward = pointsToAward;
    }

    public CreateProjectMsg(){}

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getVotingDeadline() {
        return votingDeadline;
    }

    public void setVotingDeadline(String votingDeadline) {
        this.votingDeadline = votingDeadline;
    }

    public CreateQuestionMsg[] getQuestions() {
        return questions;
    }

    public void setQuestions(CreateQuestionMsg[] questions) {
        this.questions = questions;
    }

    public int getPointsToAward() {
        return pointsToAward;
    }

    public void setPointsToAward(int pointsToAward) {
        this.pointsToAward = pointsToAward;
    }
}
