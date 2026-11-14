package voiceapp.project.messages.vote;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Class containing data necessary for casting a vote for a project.
 */
@XmlRootElement
public class CastVoteMsg {
    private VoteChoiceMsg[] answers;

    public CastVoteMsg(VoteChoiceMsg[] answers) {
        this.answers = answers;
    }

    public CastVoteMsg() {

    }

    public VoteChoiceMsg[] getAnswers() {
        return answers;
    }

    public void setAnswers(VoteChoiceMsg[] answers) {
        this.answers = answers;
    }
}
