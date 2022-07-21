package homeprime.core.management.openhab;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST API client for communication with openHAB.
 *
 * @author Milan Ramljak
 *
 */
public class RestApiUtils {

    private static Logger logger = LoggerFactory.getLogger(RestApiUtils.class);

    /**
     * Check REST is available, meaning that openHAB is live (responds to get
     * requests).
     *
     * @return true if openHAB is live
     */
    public static Boolean isOperational(String ipAddress, int servicePort) {
        final String requestUrl = "/rest";
        final WebTarget webTarget = buildTargetUrl(ipAddress, servicePort, requestUrl);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            final Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to perform root REST request " + webTarget.getUri() + " " + e.getMessage());
        }
        return false;
    }

    /**
     * Check REST is available, meaning that openHAB is live (responds to get
     * requests).
     *
     * @return true if openHAB is live
     */
    public static Boolean setContactState(String ipAddress, int servicePort, String itemName, boolean state) {
        final String requestUrl = "/rest/items/" + itemName + "/state";
        final WebTarget webTarget = buildTargetUrl(ipAddress, servicePort, requestUrl);
        final Invocation.Builder invocationBuilder = webTarget.request();

        try {
            String stateString = "OPEN";
            if (state == true) {
                stateString = "CLOSED";
            }
            final Response response = invocationBuilder.put(Entity.entity(stateString, MediaType.TEXT_PLAIN_TYPE),
                    Response.class);
            if (response.getStatus() == 202) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to perform root REST request " + webTarget.getUri() + " " + e.getMessage());
        }
        return false;
    }

    private static WebTarget buildTargetUrl(String ipAddress, int servicePort, String requestUrl) {
        Client client = trustAllCertificates();
        String serviceUrl = "http://" + ipAddress;
        if (servicePort < 0 || servicePort > 65535) {
            logger.error("Provided REST port " + servicePort + " is not in valid range <0 - 65535]");
        } else {
            serviceUrl = serviceUrl + ":" + servicePort;
        }
        WebTarget webTarget = client.target(serviceUrl + requestUrl);
        return webTarget;
    }

    private static Client trustAllCertificates() {
        Client client = null;
        client = ClientBuilder.newClient();
        return client;

    }

}
