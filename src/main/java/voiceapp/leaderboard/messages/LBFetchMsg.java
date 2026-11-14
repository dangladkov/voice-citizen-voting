package voiceapp.leaderboard.messages;

import jakarta.xml.bind.annotation.XmlRootElement;
import voiceapp.leaderboard.model.LeaderboardUser;

import java.util.List;

/**
 * Message for fetching the leaderboard users.
 */
@XmlRootElement
public class LBFetchMsg {
    private List<LeaderboardUser> users;

    public LBFetchMsg(List<LeaderboardUser> users) {
        this.users = users;
    }

    public List<LeaderboardUser> getUsers() {
        return users;
    }
}
