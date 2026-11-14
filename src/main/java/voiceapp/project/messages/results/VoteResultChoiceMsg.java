package voiceapp.project.messages.results;

/**
 * Class containing data for a specific vote of a choice. Parent class is VoteResultQuestionMsg.
 */
public class VoteResultChoiceMsg {
    private String text;
    private int count;

    public VoteResultChoiceMsg(String text, int count) {
        this.text = text;
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
