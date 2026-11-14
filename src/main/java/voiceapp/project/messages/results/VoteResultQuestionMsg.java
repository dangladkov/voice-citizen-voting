package voiceapp.project.messages.results;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class containing data for a specific vote of a question. Parent class is VoteResultMsg.
 */
public class VoteResultQuestionMsg {
    private String text;
    private List<VoteResultChoiceMsg> choices;

    public VoteResultQuestionMsg(String text) {
        this.text = text;
        this.choices = new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<VoteResultChoiceMsg> getChoices() {
        return choices;
    }

    public void setChoices(List<VoteResultChoiceMsg> choices) {
        this.choices = choices;
    }

    public void addChoice(VoteResultChoiceMsg vrcm) {
        this.choices.add(vrcm);
    }
}
