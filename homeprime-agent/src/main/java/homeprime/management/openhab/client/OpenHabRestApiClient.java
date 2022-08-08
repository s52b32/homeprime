package homeprime.management.openhab.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import homeprime.agent.config.pojos.Management;
import homeprime.core.logger.IoTLogger;
import homeprime.system.ThingSystemInfo;

/**
 * REST API client for communication with OpenHAB.
 *
 * @author Milan Ramljak
 *
 */
public class OpenHabRestApiClient {

    /**
     * Check REST is available, meaning that OpenHAB is running.
     *
     * @return true if openHAB is live
     */
    public static Boolean isAlive(Management management) {
        final String index = "/";

        try {
            if (performGet(management, index) == 200) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance()
                    .error("Failed to perform root REST request " + serverInfo(management) + " " + e.getMessage());
        }
        return false;
    }

    /**
     * Get OpenHAB item by it's id.
     *
     * @return {@code true} if exists.
     */
    public static boolean checkItemExists(Management management, String itemId) {
        final String getItemByName = "/items/" + itemId.trim();

        try {
            if (performGet(management, getItemByName) == 200) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to get openHAB item by name: " + itemId);
        }
        return false;
    }

    /**
     * Get thing system info.
     *
     * @return {@link ThingSystemInfo} if valid.
     */
    public static boolean updateItemState(Management management, String itemId, String itemState) {
        final String updateItemState = "/items/" + itemId.trim() + "/state";

        try {
            if (performPut(management, updateItemState, itemState.toUpperCase()) == 202) {
                return true;
            }
        } catch (Exception e) {
            IoTLogger.getInstance().error("Failed to update openHAB item by name: " + itemId);
        }
        return false;
    }

    /**
     * Check is HTTP or HTTPS used on openHAB. Maybe in future can be removed to only use HTTPS.
     *
     * @param management
     * @return {@value http} or {@value https}
     */
    private static String getHttpProtocol(Management management) {
        return "http";
    }

    /**
     * Build URL string to thing REST base URL.
     *
     * @param thing
     * @return
     */
    private static String buildBaseUrl(Management management) {
        return getHttpProtocol(management) + "://" + management.getIp() + ":" + management.getPort() + "/rest";
    }

    private static int performGet(Management management, String cmdPath) {
        int responseCode = 0;
        try {

            final URL url = new URL(buildBaseUrl(management) + cmdPath);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            responseCode = conn.getResponseCode();
            IoTLogger.getInstance().debug("GET path:" + cmdPath + " response: " + responseCode);
            conn.disconnect();
        } catch (MalformedURLException e) {
            IoTLogger.getInstance().error("Failed to perform HTTP GET request: " + e.getMessage());
        } catch (IOException e) {
            IoTLogger.getInstance().error("Failed to perform HTTP GET request: " + e.getMessage());
        }
        return responseCode;
    }

    private static int performPut(Management management, String cmdPath, String input) {
        int responseCode = 0;
        try {

            final URL url = new URL(buildBaseUrl(management) + cmdPath);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");

            final OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            responseCode = conn.getResponseCode();
            IoTLogger.getInstance().debug("PUT path:" + cmdPath + " input: " + input + " response: " + responseCode);
            conn.disconnect();

        } catch (MalformedURLException e) {
            IoTLogger.getInstance().error("Failed to perform HTTP GET request: " + e.getMessage());
        } catch (IOException e) {
            IoTLogger.getInstance().error("Failed to perform HTTP GET request: " + e.getMessage());
        }
        return responseCode;
    }

    /**
     * Helper method that build string describing thing.
     *
     * @param management
     * @return
     */
    private static String serverInfo(Management management) {
        return management.getName() + " (" + management.getIp() + ":" + management.getPort() + ")";
    }

}
