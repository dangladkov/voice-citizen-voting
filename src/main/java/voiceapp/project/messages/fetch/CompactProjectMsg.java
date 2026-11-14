package voiceapp.project.messages.fetch;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Class containing all available projectIDs.
 */
@XmlRootElement
public class CompactProjectMsg {
    public List<Integer> projectIDs;

    public CompactProjectMsg(List<Integer> projectIDs) {
        this.projectIDs = projectIDs;
    }

    public CompactProjectMsg() {}

    public List<Integer> getProjectIDs() {
        return projectIDs;
    }

    public void setProjectIDs(List<Integer> projectIDs) {
        this.projectIDs = projectIDs;
    }
}
