package homeprime.agent.client;

/**
 * Holder for HTTP request responses.
 *
 * @author Milan Ramljak
 *
 */
public class HttpResponse {

    private int responseCode = 0;
    private String responseBody = null;

    public HttpResponse(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

}
