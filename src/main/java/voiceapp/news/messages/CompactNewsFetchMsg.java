package voiceapp.news.messages;

import java.util.List;

/**
 * Message containing all available news IDs.
 */
public class CompactNewsFetchMsg {
    private List<Integer> newsIDs;

    public CompactNewsFetchMsg(List<Integer> newsIDs) {
        this.newsIDs = newsIDs;
    }

    public CompactNewsFetchMsg(){}

    public List<Integer> getNewsIDs() {
        return newsIDs;
    }

    public void setNewsIDs(List<Integer> newsIDs) {
        this.newsIDs = newsIDs;
    }
}
