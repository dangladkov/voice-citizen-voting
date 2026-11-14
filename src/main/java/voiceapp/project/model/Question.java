package voiceapp.project.model;

/**
 * Represents the data structure of a project question.
 */
public class Question {

    private int questionID;

    private int projectID;

    private String title;

    private String answers;

    public Question(int questionID, int projectID, String title, String answers) {
        this.questionID = questionID;
        this.projectID = projectID;
        this.title = title;
        this.answers = answers;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}
