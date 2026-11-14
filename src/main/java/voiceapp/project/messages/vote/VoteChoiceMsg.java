package voiceapp.project.messages.vote;

/**
 * Class containing data necessary for a selected choice of a vote.
 */
public class VoteChoiceMsg {
    private int votingQuestionID;
    private String choice;

    public VoteChoiceMsg(int votingQuestionID, String choice) {
        this.votingQuestionID = votingQuestionID;
        this.choice = choice;
    }

    public VoteChoiceMsg() {

    }

    public int getVotingQuestionID() {
        return votingQuestionID;
    }

    public void setVotingQuestionID(int votingQuestionID) {
        this.votingQuestionID = votingQuestionID;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
