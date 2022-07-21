package homeprime.agent.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.agent.config.pojos.Thing;
import homeprime.file.FileUtils;
import homeprime.file.pojos.DownloadData;
import homeprime.file.pojos.UploadData;
import homeprime.image.pojos.Image;
import homeprime.image.pojos.Images;
import homeprime.system.ThingSystemInfo;

/**
 * REST API client for communication with HomePrime Agent.
 *
 * @author Milan Ramljak
 *
 */
public class AgentRestApiClient {

    private static Logger logger = LoggerFactory.getLogger(AgentRestApiClient.class);

    /**
     * Check REST is available, meaning that Thing is live.
     *
     * @return true if thing is live
     */
    public static Boolean isThingAlive(Thing thing) {
        final String index = "/";
        final WebTarget webTarget = buildTargetUrl(thing, index);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to perform root REST request " + agentInfo(thing) + " " + e.getMessage());
        }
        return false;
    }

    /**
     * Check Thing is in maintenance mode.
     *
     * @return true if thing has maintenance mode enabled
     */
    public static boolean readMaintenanceState(Thing thing) {
        final String thingMaintenanceReadCmd = "/Thing/Maintenance/read";
        final WebTarget webTarget = buildTargetUrl(thing, thingMaintenanceReadCmd);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200 && response.readEntity(String.class).contains("true")) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to perform maintenance mode read request " + agentInfo(thing));
        }
        return false;
    }

    /**
     * Check REST is available, meaning that Thing is live.
     *
     * @return true if thing is live
     */
    public static Boolean setMaintenanceMode(Thing thing, boolean state) {
        String operation = "off";

        if (state) {
            operation = "on";
        }
        final String thingMaintenanceModeSetCmd = "/Thing/Maintenance/" + operation;

        final WebTarget webTarget = buildTargetUrl(thing, thingMaintenanceModeSetCmd);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to perform maintenance mode set request " + agentInfo(thing));
        }
        return false;
    }

    /**
     * Get thing configuration.
     *
     * @return {@link Thing} if valid.
     */
    public static Thing getThingConfiguration(Thing thing) {
        final String index = "/Thing";

        final WebTarget webTarget = buildTargetUrl(thing, index);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                final String output = response.readEntity(String.class);
                final ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(output, Thing.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to perform agent get configuration request " + agentInfo(thing));
        }
        return null;
    }

    /**
     * Get thing system info.
     *
     * @return {@link ThingSystemInfo} if valid.
     */
    public static String getThingSystemInfo(Thing thing) {
        final String index = "/Thing/System";

        final WebTarget webTarget = buildTargetUrl(thing, index);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                final String output = response.readEntity(String.class);
                return output;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to perform agent get configuration request " + agentInfo(thing));
        }
        return null;
    }

    /**
     * Perform homeprime service restart.
     *
     * @return true if service restart initiated successfully
     */
    public static Boolean performServiceRestart(Thing thing) {
        final String thingServiceRestartSetCmd = "/Thing/restart";

        final WebTarget webTarget = buildTargetUrl(thing, thingServiceRestartSetCmd);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200 || !isThingAlive(thing)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to perform agent service restart request " + agentInfo(thing));
        }
        return false;
    }

    /**
     * Perform homeprime system reboot.
     *
     * @return true if reboot initiated successfully
     */
    public static Boolean performSystemReboot(Thing thing) {
        final String thingServiceRestartSetCmd = "/Thing/reboot";

        final WebTarget webTarget = buildTargetUrl(thing, thingServiceRestartSetCmd);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to perform agent system reboot request " + agentInfo(thing));
        }
        return false;
    }

    /**
     * Perform homeprime service restart.
     *
     * @return true if service restart initiated successfully
     */
    public static Boolean performAppTermination(Thing thing) {
        final String thingServiceRestartSetCmd = "/Thing/terminate";

        final WebTarget webTarget = buildTargetUrl(thing, thingServiceRestartSetCmd);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to perform agent applciation terminate request " + agentInfo(thing));
        }
        return false;
    }

    /**
     * Upload image to agent.
     *
     * @return true if upload was successful.
     */
    public static Boolean upload(Thing thing, File file, UploadData uploadData) {
        boolean result = false;
        final String thingUploadCmd = "/Images/upload";
        final WebTarget webTarget = buildTargetMultipartFeatureUrl(thing, thingUploadCmd);
        final Invocation.Builder invocationBuilder = webTarget.request();
        final FileDataBodyPart filePart = new FileDataBodyPart("file", file);
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        FormDataMultiPart multipart;
        if (uploadData == null) {
            multipart = (FormDataMultiPart) formDataMultiPart.bodyPart(filePart);
        } else {
            final ObjectMapper mapper = new ObjectMapper();
            String uploadDataString = "{}";
            try {
                uploadDataString = mapper.writeValueAsString(uploadData);
            } catch (JsonProcessingException e1) {
            }
            multipart = (FormDataMultiPart) formDataMultiPart
                    .field("uploadData", uploadDataString, MediaType.APPLICATION_JSON_TYPE).bodyPart(filePart);
        }
        try {
            final Response response = invocationBuilder.post(Entity.entity(multipart, multipart.getMediaType()));
            if (response.getStatus() == 200) {
                result = true;
            }
            formDataMultiPart.close();
            multipart.close();

        } catch (Exception e) {
            logger.error("Failed to perform agent image upload request " + agentInfo(thing));
        }
        return result;
    }

    public static boolean download(Thing thing, DownloadData downloadData) {
        boolean downloadResult = false;
        final String thingConfigBackupCmd = "/Configs/backup";
        final WebTarget webTarget = buildTargetUrl(thing, thingConfigBackupCmd);
        // expected format is ZIP
        Response resp = webTarget.request("application/zip").get();

        if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            if (FileUtils.checkCreateDirectory(downloadData.getDestinationPath())) {
                InputStream is = resp.readEntity(InputStream.class);
                downloadResult = saveFileFromStream(is, downloadData);
                IOUtils.closeQuietly(is);
            }
        } else {
            throw new WebApplicationException("Http Call failed. response code is" + resp.getStatus()
                    + ". Error reported is" + resp.getStatusInfo());
        }
        return downloadResult;
    }

    public static boolean downloadActiveImage(Thing thing, DownloadData downloadData) {
        boolean downloadResult = false;
        final String thingImageDownloadCmd = "/Images/download";
        final WebTarget webTarget = buildTargetUrl(thing, thingImageDownloadCmd);
        // expected format is ZIP
        Response resp = webTarget.request("application/zip").get();

        if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            if (FileUtils.checkCreateDirectory(downloadData.getDestinationPath())) {
                InputStream is = resp.readEntity(InputStream.class);
                downloadResult = saveFileFromStream(is, downloadData);
                IOUtils.closeQuietly(is);
            }
        } else {
            throw new WebApplicationException("Http Call failed. response code is" + resp.getStatus()
                    + ". Error reported is" + resp.getStatusInfo());
        }
        return downloadResult;
    }

    /**
     * Set active image.
     *
     * @return true if image was set to active successfully.
     */
    public static Boolean setActiveImage(Thing thing, Image image) {
        final String setActiveCmd = "/Images/set-active";
        final WebTarget webTarget = buildTargetUrl(thing, setActiveCmd);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.post(Entity.entity(image, MediaType.APPLICATION_JSON));
            if (response.getStatus() == 200) {
                return true;
            } else if (response.getStatus() == 404) {
                // indicates that image does not exist
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to perform agent set active image request " + agentInfo(thing));
        }
        return false;
    }

    /**
     * Get active image.
     *
     * @return {@link Image} if valid.
     */
    public static Image getActiveImage(Thing thing) {
        final String index = "/Images";

        final WebTarget webTarget = buildTargetUrl(thing, index);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                final String output = response.readEntity(String.class);
                final ObjectMapper mapper = new ObjectMapper();
                final Images images = mapper.readValue(output, Images.class);
                for (Image image : images.getImages()) {
                    if (image.getActive()) {
                        return image;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to perform agent get active image request " + agentInfo(thing));
        }
        return null;
    }

    /**
     * Check is HTTP or HTTPS used on certain thing. Maybe in future can be removed to only use HTTPS.
     *
     * @param thing
     * @return {@value http} or {@value https}
     */
    private static String getHttpProtocol(Thing thing) {
        if (thing.getAgent().getSsl() != null) {
            return "https";
        }
        return "http";
    }

    /**
     * Build URL string to thing REST base URL.
     *
     * @param thing
     * @return
     */
    private static String buildThingBaseUrl(Thing thing) {
        return getHttpProtocol(thing) + "://" + thing.getAgent().getIp() + ":" + thing.getAgent().getPort();
    }

    private static WebTarget buildTargetUrl(Thing thing, String cmdPath) {
        Client client = trustAllCertificates();
        WebTarget webTarget = client.target(buildThingBaseUrl(thing) + cmdPath);
        return webTarget;
    }

    private static WebTarget buildTargetMultipartFeatureUrl(Thing thing, String cmdPath) {
        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        WebTarget webTarget = client.target(buildThingBaseUrl(thing) + cmdPath);
        return webTarget;
    }

    /**
     * Instantiate Jersey REST client object which trusts all end points, regardless of validity of certificate.
     *
     * @return {@link Client}
     */
    private static Client trustAllCertificates() {
        Client client;
        try {
            client = new ClientWithSSL().initClient(null);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            client = ClientBuilder.newClient();
        }
        return client;

    }

    /**
     * Store contents of file from stream to file. If file with same name already exists, replace it.
     *
     * @param inputStream
     * @param downloadData
     * @return {@code true} if file is saved successfully otherwise {@code false}
     */
    private static boolean saveFileFromStream(InputStream inputStream, DownloadData downloadData) {
        final File downloadfile = new File(downloadData.getAbsoluteFilePath());
        try {
            Files.copy(inputStream, downloadfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Helper method that build string describing thing.
     *
     * @param thing
     * @return
     */
    private static String agentInfo(Thing thing) {
        return thing.getUuid() + " (" + thing.getAgent().getIp() + ":" + thing.getAgent().getPort() + ")";
    }

}
