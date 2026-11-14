package voiceapp.leaderboard.resources;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.model.AuthData;
import voiceapp.exceptions.UnauthorizedException;
import voiceapp.leaderboard.dao.LeaderboardDao;
import voiceapp.leaderboard.messages.LBFetchMsg;
import voiceapp.leaderboard.model.LeaderboardUser;

import java.util.List;

/**
 * Class responsible for handling the Jersey request for fetching the leaderboard.
 */
@Path("leaderboard")
public class LeaderboardResource {
    /**
     * Handler for fetching the leaderboard data.
     * @param authData  Cookie authentication data containing UID and token.
     * @return          Message containing the leaderboard data.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public LBFetchMsg fetchLeaderboard(@CookieParam("Authentication") String authData) {
        AuthData ad = new AuthData(authData);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            List<LeaderboardUser> users = LeaderboardDao.instance.getTopUsers();
            if (users == null || users.isEmpty()) {
                return null;
            } else {
                return new LBFetchMsg(users);
            }
        }
        else {
            throw new UnauthorizedException();
        }
    }
}
