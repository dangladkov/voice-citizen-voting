package voiceapp.filerepo;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Script responsible for handling file uploads.
 */
public enum FileRepo {
    instance;

    public static final boolean IS_DEPLOYED = false;
    public static final String DEV_DEST_PATH = "/opt/voice-files/";
    public static final String DEPLOYED_PATH = "/var/www/uploads/";

    /**
     * Store a file on the machine.
     * @param file              Input Stream (incoming Jersey stream)
     * @param resourcePrefix    Path resource prefix (shop,idea, etc.)
     * @return                  Path of the stored file
     */
    public String storeFile(InputStream file, String resourcePrefix) {
        try {
            String dir;
            if (IS_DEPLOYED) {
                dir = DEPLOYED_PATH + resourcePrefix + "/" + "image0";
            } else {
                dir = DEV_DEST_PATH + resourcePrefix + "/" + "image0";
            }

            System.out.println("Trying path " + dir);
            File outputfile = new File(dir);

            int suffixIndex = 0;
            while(outputfile.exists()) {
                String subDir;
                if (IS_DEPLOYED) {
                    subDir = DEPLOYED_PATH + resourcePrefix + "/" + "image" + (suffixIndex++);
                } else {
                    subDir = DEV_DEST_PATH + resourcePrefix + "/" + "image" + (suffixIndex++);
                }

                System.out.println("Found matching file, trying path " + subDir);
                outputfile = new File(subDir);
            }

            File parent = outputfile.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }

            OutputStream out = new FileOutputStream(outputfile);

            IOUtils.copy(file, out);

            out.flush();
            out.close();

            //ImageIO.write(bf, "png", outputfile);
            return outputfile.getAbsolutePath();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
