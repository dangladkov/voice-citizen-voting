package voiceapp.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Custom internal error exception (status code 500)
 */
public class InternalErrorException extends WebApplicationException {
    public InternalErrorException() {
        super(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    public InternalErrorException(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).type("text/plain").build());
    }
}
