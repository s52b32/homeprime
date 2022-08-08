package homeprime.agent.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import homeprime.agent.config.pojos.Thing;
import homeprime.core.logger.IoTLogger;
import homeprime.file.pojos.DownloadData;
import homeprime.file.pojos.UploadData;
import homeprime.image.pojos.Image;
import homeprime.image.pojos.Images;
import homeprime.system.ThingSystemInfo;

/**
 * REST API client for communication with HomePrime agent.
 *
 * @author Milan Ramljak
 *
 */
public class AgentRestApiClient {

    /**
     * Check REST is available, meaning that OpenHAB is running.
     *
     * @return true if openHAB is live
     */
    public static Boolean isThingAlive(Thing thing) {
        final String index = "/";

        try {
            if (performGet(thing, index).getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance()
                    .error("Failed to perform root REST request " + agentInfo(thing) + " " + e.getMessage());
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

        try {
            final HttpResponse response = performGet(thing, thingMaintenanceReadCmd);
            if (response.getResponseCode() == 200 && response.getResponseBody().contains("true")) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform maintenance mode read request " + agentInfo(thing));
        }
        return false;
    }

    /**
     * Set maintenance mode on thing.
     *
     * @return true if thing if operation was successful
     */
    public static Boolean setMaintenanceMode(Thing thing, boolean state) {
        String operation = "off";

        if (state) {
            operation = "on";
        }
        final String thingMaintenanceModeSetCmd = "/Thing/Maintenance/" + operation;

        try {
            final HttpResponse response = performGet(thing, thingMaintenanceModeSetCmd);
            if (response.getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform maintenance mode set request " + agentInfo(thing));
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

        try {
            final HttpResponse response = performGet(thing, index);
            if (response.getResponseCode() == 200) {
                final ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.getResponseBody(), Thing.class);
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform agent get configuration request " + agentInfo(thing));
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

        try {
            final HttpResponse response = performGet(thing, index);
            if (response.getResponseCode() == 200) {
                return response.getResponseBody();
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform agent get configuration request " + agentInfo(thing));
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

        try {
            final HttpResponse response = performGet(thing, thingServiceRestartSetCmd);
            if (response.getResponseCode() == 200 || !isThingAlive(thing)) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform agent service restart request " + agentInfo(thing));
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

        try {
            final HttpResponse response = performGet(thing, thingServiceRestartSetCmd);
            if (response.getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform agent system reboot request " + agentInfo(thing));
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

        try {
            final HttpResponse response = performGet(thing, thingServiceRestartSetCmd);
            if (response.getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform agent applciation terminate request " + agentInfo(thing));
        }
        return false;
    }

    /**
     * Set active image.
     *
     * @return true if image was set to active successfully.
     */
    public static Boolean setActiveImage(Thing thing, Image image) {
        final String setActiveCmd = "/Images/set-active";

        try {
            final ObjectMapper mapper = new ObjectMapper();
            final HttpResponse response = performPost(thing, setActiveCmd, mapper.writeValueAsString(image));
            if (response.getResponseCode() == 200) {
                return true;
            } else if (response.getResponseCode() == 404) {
                // indicates that image does not exist
                return null;
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform agent set active image request " + agentInfo(thing));
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
        int responseCode = 0;
        final String thingUploadCmd = "/Images/upload";
        String charset = "UTF-8";
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        try {
            URLConnection connection = new URL(buildBaseUrl(thing) + thingUploadCmd).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            // Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + file.getName() + "\"")
                    .append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            // copy binary to stream
            Files.copy(file.toPath(), output);
            // add body part
            final ObjectMapper mapper = new ObjectMapper();
            try {
                output.write(mapper.writeValueAsString(uploadData).getBytes());
            } catch (JsonProcessingException e1) {
                // empty data
                output.write("{}".getBytes());
            }
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
            // execute request
            responseCode = ((HttpURLConnection) connection).getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (responseCode == 200) {
            result = true;
        }
        return result;
    }

    public static boolean download(Thing thing, DownloadData downloadData) {
        boolean downloadResult = true;
        final String thingConfigBackupCmd = "/Configs/backup";
        try {
            URL url = new URL(buildBaseUrl(thing) + thingConfigBackupCmd);
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            FileOutputStream fis = new FileOutputStream(downloadData.getAbsoluteFilePath());
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = bis.read(buffer, 0, 1024)) != -1) {
                fis.write(buffer, 0, count);
            }
            fis.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
            downloadResult = false;
        }

        return downloadResult;
    }

    public static boolean downloadActiveImage(Thing thing, DownloadData downloadData) {
        boolean downloadResult = true;
        final String thingImageDownloadCmd = "/Images/download";
        try {
            URL url = new URL(buildBaseUrl(thing) + thingImageDownloadCmd);
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            FileOutputStream fis = new FileOutputStream(downloadData.getAbsoluteFilePath());
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = bis.read(buffer, 0, 1024)) != -1) {
                fis.write(buffer, 0, count);
            }
            fis.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
            downloadResult = false;
        }
        return downloadResult;
    }

    /**
     * Get active image.
     *
     * @return {@link Image} if valid.
     */
    public static Image getActiveImage(Thing thing) {
        final String imagesBase = "/Images";

        try {
            final HttpResponse response = performGet(thing, imagesBase);
            if (response.getResponseCode() == 200) {
                final ObjectMapper mapper = new ObjectMapper();
                final Images images = mapper.readValue(response.getResponseBody(), Images.class);
                for (Image image : images.getImages()) {
                    if (image.getActive()) {
                        return image;
                    }
                }
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to perform agent get active image request " + agentInfo(thing));
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
    private static String buildBaseUrl(Thing thing) {
        return getHttpProtocol(thing) + "://" + thing.getAgent().getIp() + ":" + thing.getAgent().getPort();
    }

    private static HttpResponse performGet(Thing thing, String cmdPath) {
        HttpResponse response = null;
        try {

            final URL url = new URL(buildBaseUrl(thing) + cmdPath);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            IoTLogger.getInstance().debug("GET path:" + cmdPath + " response: " + responseCode);
            conn.disconnect();
            response = new HttpResponse(responseCode, content.toString());
        } catch (MalformedURLException e) {
            IoTLogger.getInstance().error("Failed to perform HTTP GET request: " + e.getMessage());
        } catch (IOException e) {
            IoTLogger.getInstance().error("Failed to perform HTTP GET request: " + e.getMessage());
        }
        return response;
    }

    private static HttpResponse performPost(Thing thing, String cmdPath, String input) {
        HttpResponse response = null;
        try {

            final URL url = new URL(buildBaseUrl(thing) + cmdPath);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            final OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            int responseCode = conn.getResponseCode();
            IoTLogger.getInstance().debug("POST path:" + cmdPath + " input: " + input + " response: " + responseCode);
            conn.disconnect();
            response = new HttpResponse(responseCode, null);
        } catch (MalformedURLException e) {
            IoTLogger.getInstance().error("Failed to perform HTTP GET request: " + e.getMessage());
        } catch (IOException e) {
            IoTLogger.getInstance().error("Failed to perform HTTP GET request: " + e.getMessage());
        }
        return response;
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
