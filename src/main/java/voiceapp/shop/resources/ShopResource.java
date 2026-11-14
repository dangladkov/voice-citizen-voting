package voiceapp.shop.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.model.AuthData;
import voiceapp.exceptions.InternalErrorException;
import voiceapp.exceptions.UnauthorizedException;
import voiceapp.shop.dao.BenefitDao;
import voiceapp.shop.dao.BenefitTransactionDao;
import voiceapp.shop.dao.TransactionDao;
import voiceapp.shop.messages.BenefitCreateMsg;
import voiceapp.shop.messages.CompactBenefitsMsg;
import voiceapp.shop.model.Benefit;

import java.util.List;
import java.util.Objects;

/**
 * Class handling Jersey functions for adding, editing, removing and buying benefits.
 */
@Path("shop")
public class ShopResource {
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    /**
     * Creates a new benefit.
     * @param cookie    Cookie authentication data containing UID and token.
     * @param benefit   Data necessary for creating a benefit.
     * @return          Benefit ID of the newly created benefit.
     */
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public int addBenefit (@CookieParam("Authentication") String cookie, BenefitCreateMsg benefit) {
        AuthData ad = new AuthData(cookie);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            ObjectMapper ow = new ObjectMapper();
            ow.getFactory().setCharacterEscapes(null);
            ow.getFactory().setRootValueSeparator(null);
            String attributes;
            try {
                attributes = ow.writeValueAsString(benefit.getAttributes());
            } catch (JsonProcessingException e) {
                throw new InternalErrorException("Invalid Benefit Attributes! (AB02_2)");
            }

            if (Objects.equals(attributes, "")) {
                throw new InternalErrorException("Invalid Benefit Attributes! (AB02_1)");
            }

            int result = BenefitDao.instance.addBenefit(benefit.getTitle(), benefit.getDescription(), benefit.getPoints(), benefit.getCategory(), attributes);
            if (result != -1) {
                return result;
            } else {
                throw new InternalErrorException("Failed to insert benefit! Please report! (AB01)");
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * Retrieves a specific benefit based on the benefitID.
     * @param cookie        Cookie authentication data containing UID and token.
     * @param benefitID     Benefit ID to fetch.
     * @return              ALl data regarding a benefit.
     */
    @Path("{benefitID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Benefit getBenefit (@CookieParam("Authentication") String cookie, @PathParam("benefitID") int benefitID) {
        AuthData ad = new AuthData(cookie);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            return BenefitDao.instance.getBenefit(benefitID);
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * Removes a specific benefit based on the benefit ID.
     * @param cookie        Cookie authentication data containing UID and token.
     * @param benefitID     Benefit ID to delete.
     * @return              Response from the operation (status code)
     */
    @Path("{benefitID}/remove")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeBenefit(@CookieParam("Authentication") String cookie, @PathParam("benefitID") int benefitID) {
        AuthData ad = new AuthData(cookie);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            boolean result = BenefitDao.instance.deleteBenefit(benefitID);
            if (result) {
                return Response.ok().build();
            } else {
                throw new InternalErrorException("Failed to delete benefit! Please report! (RB01)");
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * Edit a benefit.
     * @param cookie        Cookie authentication data containing UID and token.
     * @param benefitID     Benefit ID to update.
     * @param newBenefit    New benefit data (to replace)
     * @return              Response from the operation (status code)
     */
    @Path("{benefitID}/edit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response editBenefit(@CookieParam("Authentication") String cookie, @PathParam("benefitID") int benefitID, BenefitCreateMsg newBenefit) {
        AuthData ad = new AuthData(cookie);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            ObjectMapper ow = new ObjectMapper();
            ow.getFactory().setCharacterEscapes(null);
            ow.getFactory().setRootValueSeparator(null);
            String attributes;
            try {
                attributes = ow.writeValueAsString(newBenefit.getAttributes());
            } catch (JsonProcessingException e) {
                throw new InternalErrorException("Invalid Benefit Attributes! (EB02_2)");
            }

            if (Objects.equals(attributes, "")) {
                throw new InternalErrorException("Invalid Benefit Attributes! (EB02_1)");
            }

            boolean result = BenefitDao.instance.updateBenefit(benefitID, newBenefit.getTitle(), newBenefit.getDescription(), newBenefit.getPoints(), newBenefit.getCategory(), attributes);
            if (result) {
                return Response.ok().build();
            } else {
                throw new InternalErrorException("Failed to edit benefit! Please report! (EB01)");
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * Function responsible for creating a message containing all benefit IDs.
     * @param cookie    Cookie authentication data containing UID and token.
     * @return          Message containing all benefit IDs.
     */
    @Path("preview")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompactBenefitsMsg previewBenefits(@CookieParam("Authentication") String cookie) {
        AuthData ad = new AuthData(cookie);

        if (UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            List<Integer> result = BenefitDao.instance.getAllBenefits();
            if (result == null || result.isEmpty()) {
                return null;
            } else {
                return new CompactBenefitsMsg(result);
            }
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * Creates a transaction when a user attempts to buy a benefit.
     * @param cookie        Cookie authentication data containing UID and token.
     * @param benefitID     Benefit ID of the benefit to buy.
     * @return              Transaction ID.
     */
    @Path("{benefitID}/buy")
    @POST
    public int buyBenefit(@CookieParam("Authentication") String cookie, @PathParam("benefitID") int benefitID) {
        AuthData ad = new AuthData(cookie);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        TransactionDao.instance.sendPoints(ad.getUserID(), BenefitDao.instance.getBenefit(benefitID).getPoints());
        return BenefitTransactionDao.instance.addTransaction(ad.getUserID(), benefitID);
    }
}
