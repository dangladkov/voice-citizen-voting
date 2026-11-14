package voiceapp.news.messages;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Message containing data necessary for creating a news.
 */
@XmlRootElement
public class NewsCreateMsg {
    private String title;
    private String description;

    public NewsCreateMsg(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public NewsCreateMsg(){}
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
}
