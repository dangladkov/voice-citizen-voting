package voiceapp.news.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.model.AuthData;
import voiceapp.exceptions.InternalErrorException;
import voiceapp.exceptions.UnauthorizedException;
import voiceapp.news.dao.NewsDao;
import voiceapp.news.messages.CompactNewsFetchMsg;
import voiceapp.news.messages.NewsCreateMsg;
import voiceapp.news.model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handling news fetching, adding and deleting.
 */
@Path("news")
public class NewsResource {
    /**
     * Fetches all available IDs of news.
     * @param authData  Cookie authentication data containing UID and token.
     * @return          Message containing all news IDs.
     */
    @GET
    @Path("fetch-all")
    @Produces(MediaType.APPLICATION_JSON)
    public CompactNewsFetchMsg fetchAll(@CookieParam("Authentication") String authData) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        List<News> newsList = NewsDao.instance.getNews();

        if (newsList == null || newsList.isEmpty()) {
            return null;
        }

        List<Integer> resultIDs = new ArrayList<>();

        for (News n : newsList) {
            resultIDs.add(n.getNewsID());
        }

        return new CompactNewsFetchMsg(resultIDs);
    }

    @GET
    @Path("{newsID}")
    public News fetchNews(@CookieParam("Authentication") String authData, @PathParam("newsID") int newsID) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        News result = NewsDao.instance.getNews(newsID);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }

    @POST
    @Path("submit")
    public Response submitNews(@CookieParam("Authentication") String authData, NewsCreateMsg ncm) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        if (!UserDao.instance.isAdmin(ad.getUserID())) {
            throw new ForbiddenException();
        }

        int resultID = NewsDao.instance.addNews(ncm.getTitle(), ncm.getDescription(), ad.getUserID());

        if (resultID != -1) {
            return Response.ok().build();
        } else {
            throw new NotFoundException();
        }
    }

    @DELETE
    @Path("{newsID}")
    public Response deleteNews(@CookieParam("Authentication") String authData, @PathParam("newsID") int newsID) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        if (!UserDao.instance.isAdmin(ad.getUserID())) {
            throw new ForbiddenException();
        }

        boolean result = NewsDao.instance.deleteNews(newsID);

        if (result) {
            return Response.ok().build();
        } else {
            throw new InternalErrorException("Failed to delete idea! Please report! DN01");
        }
    }
}
