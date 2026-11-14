package voiceapp.project.messages.fetch;

import voiceapp.project.model.Question;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Class containing data necessary for fetching a project.
 */
public class ProjectFetchMsg {
    private String title;

    private String description;

    private String location;

    private Date date;

    private String categories;

    private Timestamp deadline;

    private int pointsToAward;

    private int projectStatus;

    private boolean hasVoted;

    private List<Question> questions;

    public ProjectFetchMsg(String title, String description, String location, Date date, String categories, Timestamp deadline, int pointsToAward, int projectStatus, boolean hasVoted, List<Question> questions) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.categories = categories;
        this.deadline = deadline;
        this.pointsToAward = pointsToAward;
        this.projectStatus = projectStatus;
        this.hasVoted = hasVoted;
        this.questions = questions;
    }

    public ProjectFetchMsg() {

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
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

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
