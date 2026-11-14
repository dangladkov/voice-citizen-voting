package voiceapp.idea.messages;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Summarized message containing all idea IDs.
 */
@XmlRootElement
public class CompactIdeaMsg {
    private final List<Integer> ideasIDs;

    public CompactIdeaMsg(List<Integer> ideasIDs) {
        this.ideasIDs = ideasIDs;
    }

    public List<Integer> getIdeasID() {
        return ideasIDs;
    }
}
