package voiceapp.auth.resources;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.messages.incoming.UpdateAccountDataMsg;
import voiceapp.auth.messages.incoming.UserLoginMsg;
import voiceapp.auth.messages.incoming.UserRegisterMsg;
import voiceapp.auth.messages.outgoing.OnUserLoginMsg;
import voiceapp.auth.messages.outgoing.UserDataMsg;
import voiceapp.auth.model.AuthData;
import voiceapp.auth.model.User;
import voiceapp.exceptions.InternalErrorException;
import voiceapp.exceptions.UnauthorizedException;
import voiceapp.leaderboard.dao.LeaderboardDao;

import java.security.SecureRandom;
import java.util.Base64;

@Path("user")
public class UserResource {
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    /**
     * Register a user on the platform.
     * @param msg   Message with data required to create the user.
     * @return      Data needed for the client to identify themselves afterward.
     */
    @Path("sign-up")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OnUserLoginMsg registerUser(UserRegisterMsg msg) {
        try {
            if (UserDao.instance.userExists(msg.getPhoneNumber()) || UserDao.instance.userExists(msg.getEmail())) {
                throw new InternalErrorException("User exists! (RU01)");
            }

            String hash = BCrypt.withDefaults().hashToString(12, msg.getPwd().toCharArray());
            System.out.println(hash);
            User user = new User(-1, msg.getName(), msg.getSurname(), msg.getPhoneNumber(), msg.getEmail(), 0, 0, false, hash);

            // If the users table is empty, then the first user should be an admin
            boolean createAdmin = UserDao.instance.isUserTableEmpty();

            if (!UserDao.instance.addUser(user)) {
                throw new InternalErrorException("Internal error while registering a user! (RU02)");
            }

            int userID = UserDao.instance.getUserIDSecure(msg.getPhoneNumber()); // either phone number or email should work
            if (userID == -1) { // verify that the user was actually added properly
                throw new InternalErrorException("Internal error while registering a user! (RU03)");
            }

            // Generate a new user session
            String token = generateNewToken();
            boolean success = UserDao.instance.renewSession(userID, token);
            if (!success) {
                throw new InternalErrorException("Internal error while registering a user! (RU04)!");
            }

            // Promote to admin if this is the first user
            if (createAdmin) {
                boolean promoteResult = UserDao.instance.modifyUserPermission(userID, UserDao.AccessLevel.ADMIN);
                if (!promoteResult) {
                    throw new InternalErrorException("Internal error while registering a user! (RU05)");
                }
            }

            return new OnUserLoginMsg(userID, token);

        } catch (RuntimeException e) {
            System.out.println(e);
            throw new InternalErrorException("Internal error while registering a user! (RU06)");
        }
    }


    /**
     * Authenticates a user who logs into the platform.
     * @param msg   Message with data required to log in the user.
     * @return      Data needed for the client to identify themselves afterward.
     */
    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OnUserLoginMsg loginUser(UserLoginMsg msg) {
        try {
            boolean authSuccess = UserDao.instance.isValidPassword(msg.getUserIdentifier(), msg.getPwd());
            int userId = UserDao.instance.getUserIDSecure(msg.getUserIdentifier());

            if (!authSuccess) {
                System.out.println("Failed to find/authenticate user!");
                throw new UnauthorizedException();
            } else {
                User dbUser = UserDao.instance.getUserSecure(userId);
                if (dbUser != null) {
                    String token = generateNewToken();
                    boolean success = UserDao.instance.renewSession(userId, token);
                    if (success) {
                        return new OnUserLoginMsg(dbUser.getUid(), token);
                    } else {
                        throw new InternalErrorException("Internal error while logging in! Please report! (LU01)");
                    }
                } else {
                    throw new InternalErrorException("Internal error while logging in! Please report! (LU02)");
                }
            }
        }
        catch (RuntimeException e) {
            System.out.println(e);
            throw new InternalErrorException("Internal error while logging in! Please report! (LU03)");
        }
    }

    /**
     * Retrieves data about the current user (from the cookie data).
     * @param authData  Cookie authentication data containing UID and token.
     * @return          Data to return about the user.
     */
    @Path("fetch")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserDataMsg fetchUserData(@CookieParam("Authentication") String authData) {
        try {
            AuthData ad = new AuthData(authData);

            if (!ad.isValid() || !UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
                throw new UnauthorizedException();
            }

            User user = UserDao.instance.getUserSecure(ad.getUserID());
            boolean isAdmin = UserDao.instance.isAdmin(ad.getUserID());
            boolean isAnonymous = LeaderboardDao.instance.isAnonymous(ad.getUserID());
            int accessLevel = isAdmin ? 1 : 0;
            if (user == null) {
                System.out.println("Internal DB error while fetching user data!");
                throw new InternalErrorException("Internal server error (FUD01). Please report!");
            }

            return new UserDataMsg(user.getUrm().getName(), user.getUrm().getSurname(),
                    user.getUrm().getPhoneNumber(), user.getUrm().getEmail(), accessLevel, user.getPoints(), isAnonymous);
        } catch (RuntimeException e) {
            System.out.println(e);
            throw new InternalErrorException("Internal server error (FUD02). Please report!");
        }
    }

    /**
     * Promote or demote a user.
     * @param authData      Cookie authentication data containing UID and token.
     * @param userID        userID to promote
     * @param accessLevel   Access level to set for that userID.
     * @return              Status of the request - whether it succeeded or not.
     */
    @Path("{userID}/set-access-level{accessLevel}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateAccessLevel(@CookieParam("Authentication") String authData, @PathParam("userID") int userID, @PathParam("accessLevel") int accessLevel) {
        try {
            AuthData ad = new AuthData(authData);

            if (!ad.isValid() || !UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
                throw new UnauthorizedException();
            }

            if (!UserDao.instance.isAdmin(ad.getUserID())) {
                throw new ForbiddenException();
            }

            if (accessLevel == 0) {
                UserDao.instance.modifyUserPermission(userID, UserDao.AccessLevel.REGULAR);
                return Response.status(Response.Status.OK).build();
            } else if (accessLevel == 1) {
                UserDao.instance.modifyUserPermission(userID, UserDao.AccessLevel.ADMIN);
                return Response.status(Response.Status.OK).build();
            } else {
                throw new InternalErrorException("Invalid access level (PU01)");
            }
        } catch (RuntimeException e) {
            System.out.println(e);
            throw new InternalErrorException("Internal error while updating access level (PB02). Please report!");
        }
    }

    /**
     * Update account data of the current user.
     * @param authData  Cookie authentication data containing UID and token.
     * @param uadm      Message containing account data to update.
     * @return          Status of the request - whether it succeeded or not.
     */
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateAccountData(@CookieParam("Authentication") String authData, UpdateAccountDataMsg uadm) {
        try {
            AuthData ad = new AuthData(authData);

            if (!ad.isValid() || !UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
                throw new UnauthorizedException();
            }

            if (UserDao.instance.updateUser(ad.getUserID(), uadm.getName(), uadm.getSurname(), uadm.getPhoneNumber(), uadm.getEmail(), uadm.isAnonymous())) {
                return Response.status(Response.Status.OK).build();
            } else {
                throw new InternalErrorException("Internal error while updating user! Please report! UAD01");
            }
        } catch (RuntimeException e) {
            System.out.println(e);
            throw new InternalErrorException("Internal error while updating user! Please report! UAD02");
        }
    }

    /**
     * Log out a user from his account (invalidate session)
     * @param authData  Cookie authentication data containing UID and token.
     * @return          Status of the request - whether it succeeded or not.
     */
    @Path("logout")
    @POST
    public Response logoutUser(@CookieParam("Authentication") String authData) {
        try {
            AuthData ad = new AuthData(authData);

            if (!ad.isValid() || !UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
                throw new UnauthorizedException();
            }

            if (UserDao.instance.logOut(ad.getUserID())) {
                return Response.ok().build();
            } else {
                throw new InternalErrorException("Failed to properly log out user. Please report LU01");
            }

        } catch (RuntimeException e) {
            System.out.println(e);
            throw new InternalErrorException("Internal error while logging out user! Please report! LU02");
        }
    }

    // Below are helper functions used for generating a session token.

    private static final SecureRandom secureRandom = new SecureRandom();

    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private static String generateNewToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
