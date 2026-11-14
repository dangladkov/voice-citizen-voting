package voiceapp.analytics.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import voiceapp.analytics.dao.AnalyticsDao;
import voiceapp.analytics.messages.FunalyticsMsg;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.model.AuthData;
import voiceapp.exceptions.UnauthorizedException;

@Path("analytics")
public class AnalyticsResource {
    @Path("fetch")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public FunalyticsMsg fetchAnalytics(@CookieParam("Authentication") String authData) {
        AuthData ad = new AuthData(authData);

        if (!ad.isValid() || !UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        return new FunalyticsMsg(AnalyticsDao.instance.getNumberOfProjects(),
                AnalyticsDao.instance.getTotalPoints(), AnalyticsDao.instance.getNumberOfUsers(),
                AnalyticsDao.instance.getNumberOfVotes());
    }
}
