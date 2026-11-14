package voiceapp.project.messages.create;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Class containing data necessary for each choice of each question of a project. Parent class is CreateQuestionMsg.
 */
@XmlRootElement
public class CreateQuestionChoiceMsg {
    private String text;

    public CreateQuestionChoiceMsg(String text) {
        this.text = text;
    }
    
    public CreateQuestionChoiceMsg() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
