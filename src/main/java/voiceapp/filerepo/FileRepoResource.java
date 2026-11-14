package voiceapp.filerepo;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.model.AuthData;
import voiceapp.exceptions.UnauthorizedException;
import voiceapp.idea.dao.IdeaDao;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Handles Jersey file uploads
 */
@Path("filerepo")
public class FileRepoResource {
    /**
     * Function for uploading an image based on the resource type and its ID
     * @param cookie            Cookie authentication data containing UID and token.
     * @param fileInputStream   Incoming file input stream
     * @param resource          Resource type - shop/idea/project/news
     * @param elementID         Depending on the resource type it's either the benefit/idea/project ID.
     * @return                  Status of the request - whether it succeeded or not.
     */
    @POST
    @Path("upload-image/{resource}/{elementID}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response uploadImage(@CookieParam("Authentication") String cookie, @FormDataParam("image") InputStream fileInputStream,
                                @FormDataParam("image") FormDataContentDisposition fileMetaData, @PathParam("resource") String resource, @PathParam("elementID") int elementID) {
        AuthData ad = new AuthData(cookie);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        if (requiresAdmin(resource) && !UserDao.instance.isAdmin(ad.getUserID())) {
            throw new ForbiddenException();
        }

        if (!isValidResource(resource)) {
            return Response.status(404).build();
        }

        if (Objects.equals(resource, "idea") && !IdeaDao.instance.isOwner(elementID, ad.getUserID())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        FileRepo.instance.storeFile(fileInputStream, resource + elementID);

        return Response.ok().build();
    }

    /**
     * Function for downloading an image based on the resource type, its ID and the corresponding filename.
     * @param resource      Resource type - shop/idea/project/news
     * @param elementID     Depending on the resource type it's either the benefit/idea/project ID.
     * @param filename      File name - for example image0, image1, etc.
     * @return              Status of the request - whether it succeeded or not.
     */
    @GET
    @Path("fetch-image/{resource}/{elementID}/{filename}")
    @Produces("application/octet-stream")
    public Response serveImage(@PathParam("resource") String resource, @PathParam("elementID") int elementID, @PathParam("filename") String filename) {
        String folder;
        if (FileRepo.IS_DEPLOYED) {
            folder = FileRepo.DEPLOYED_PATH;
        } else {
            folder = FileRepo.DEV_DEST_PATH;
        }

        java.nio.file.Path outputFile = new File(folder + resource + elementID, filename).toPath();

        if (!outputFile.toFile().exists() || !isValidResource(resource)) {
            return Response.status(404).build();
        }

        StreamingOutput so = os -> {
            Files.copy(outputFile, os);
            os.flush();
        };

        return Response.ok(so).build();
    }

    private boolean isValidResource(String resource) {
        if (!Objects.equals(resource, "shop") && !Objects.equals(resource, "idea") && !Objects.equals(resource, "project") && !Objects.equals(resource, "news")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean requiresAdmin(String resource) {
        if (Objects.equals(resource, "shop") || Objects.equals(resource, "project") || Objects.equals(resource, "news")) {
            return true;
        } else {
            return false;
        }
    }
}
