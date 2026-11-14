package voiceapp.project.model;

/**
 * Represents the data structure of a vote.
 */
public class Vote {
    private int voteID;

    private int userID;

    private int questionID;

    private String choice;

    public Vote(int voteID, int userID, int questionID, String choice) {
        this.voteID = voteID;
        this.userID = userID;
        this.questionID = questionID;
        this.choice = choice;
    }

    public int getVoteID() {
        return voteID;
    }

    public void setVoteID(int voteID) {
        this.voteID = voteID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
