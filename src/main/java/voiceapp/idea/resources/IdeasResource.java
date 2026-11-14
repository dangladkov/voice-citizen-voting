package voiceapp.idea.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.model.AuthData;
import voiceapp.exceptions.UnauthorizedException;
import voiceapp.idea.dao.IdeaDao;
import voiceapp.idea.messages.CompactIdeaMsg;
import voiceapp.idea.model.Idea;
import voiceapp.idea.model.IdeaArchiveType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for managing the citizen ideas.
 */
@Path("ideas")
public class IdeasResource {
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    /**
     * Submit a citizen idea
     * @param authData  Cookie authentication data containing UID and token.
     * @param idea      Idea data required to create an instance in the DB.
     * @return          The idea ID of the newly created idea (from DB).
     */
    @POST
    @Path("submit-idea")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    public int submitIdea(@CookieParam("Authentication") String authData, Idea idea) {
        AuthData ad = new AuthData(authData);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            int index = IdeaDao.instance.addIdea(idea);
            if (index != -1) {
                return index;
            } else {
                throw new InternalServerErrorException("Internal error while trying to submit an idea! Please report! SI01");
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * Fetches all idea of a given type.
     * @param authData  Cookie authentication data containing UID and token.
     * @param ideaType  Idea type to filter. Refer to class IdeaArchiveType for the different types of ideas.
     * @return          Message containing all ideas IDs of such type.
     */
    @GET
    @Path("fetch/{ideaType}")
    @Produces({MediaType.APPLICATION_JSON})
    public CompactIdeaMsg fetchArchiveData(@CookieParam("Authentication") String authData, @PathParam("ideaType") int ideaType) {
        AuthData ad = new AuthData(authData);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            List<Idea> ideas = IdeaDao.instance.getIdeas();
            if (ideas != null) {
                List<Integer> result = new ArrayList<>();
                for (Idea i : ideas) {
                    if (i.getIdeaStatus() == ideaType) {
                        result.add(i.getIdeaID());
                    }
                }

                return new CompactIdeaMsg(result);
            } else {
                throw new InternalServerErrorException("Internal error while fetching archived ideas! FAD01");
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    @GET
    @Path("fetch-all")
    @Produces(MediaType.APPLICATION_JSON)
    public CompactIdeaMsg fetchAll(@CookieParam("Authentication") String authData) {
        AuthData ad = new AuthData(authData);

        if (!ad.isValid() || !UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        List<Idea> ideas = IdeaDao.instance.getIdeas();
        if (ideas == null) {
            throw new NotFoundException();
        }

        List<Integer> result = new ArrayList<>();
        for (Idea i : ideas) {
            result.add(i.getIdeaID());
        }
        return new CompactIdeaMsg(result);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{ideaID}")
    public Idea fetchIdea(@CookieParam("Authentication") String authData, @PathParam("ideaID") int itemID) {
        AuthData ad = new AuthData(authData);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            Idea idea = IdeaDao.instance.getIdea(itemID);
            if (idea != null) {
                return idea;
            } else {
                throw new InternalServerErrorException("Failed to find idea! Please report! FI01");
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    @GET
    @Path("{ideaID}/archive/{ideaType}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response archiveIdea(@CookieParam("Authentication") String authData, @PathParam("ideaID") int ideaID, @PathParam("ideaType") int ideaType) {
        AuthData ad = new AuthData(authData);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            if (UserDao.instance.isAdmin(ad.getUserID())) {
                if (IdeaDao.instance.setStatus(ideaID, IdeaArchiveType.values()[ideaType])) {
                    return Response.status(Response.Status.OK).build();
                } else {
                    throw new InternalServerErrorException("Failed to change status of idea! Please report! AI01");
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
