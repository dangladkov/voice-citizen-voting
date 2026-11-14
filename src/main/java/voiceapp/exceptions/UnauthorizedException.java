package voiceapp.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Custom exception for status code 401 messages.
 * Used when the User has invalid session or has not been authenticated.
 */
public class UnauthorizedException extends WebApplicationException {
    public UnauthorizedException() {
        super(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    public UnauthorizedException(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(message).type("text/plain").build());
    }
}
