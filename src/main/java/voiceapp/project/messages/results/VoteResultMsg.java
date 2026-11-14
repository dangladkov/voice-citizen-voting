package voiceapp.project.messages.results;

import java.util.List;

/**
 *  Class containing data for a specific vote..
 */
public class VoteResultMsg {
    private List<VoteResultQuestionMsg> questions;

    public VoteResultMsg(List<VoteResultQuestionMsg> questions) {
        this.questions = questions;
    }

    public List<VoteResultQuestionMsg> getQuestions() {
        return questions;
    }

    public void setQuestions(List<VoteResultQuestionMsg> questions) {
        this.questions = questions;
    }
}
