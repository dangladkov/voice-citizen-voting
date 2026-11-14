package voiceapp.project.messages.create;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Class containing data necessary for each question of a project. Parent class is CreateProjectMsg.
 */
@XmlRootElement
public class CreateQuestionMsg {
    private String text;
    private CreateQuestionChoiceMsg[] choices;

    public CreateQuestionMsg(String text, CreateQuestionChoiceMsg[] choices) {
        this.text = text;
        this.choices = choices;
    }

    public CreateQuestionMsg() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CreateQuestionChoiceMsg[] getChoices() {
        return choices;
    }

    public void setChoices(CreateQuestionChoiceMsg[] choices) {
        this.choices = choices;
    }
}
