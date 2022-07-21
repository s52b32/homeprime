package homeprime.agent.client;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;

/**
 * Wrapper around client to trust all certificates. Security risk!
 *
 * @author Milan Ramljak
 *
 */
public class ClientWithSSL {

    public Client initClient(Configuration config) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null, certs, new SecureRandom());
        if (config != null) {
            return ClientBuilder.newBuilder().withConfig(config).hostnameVerifier(new TrustAllHostNameVerifier())
                    .sslContext(ctx).build();
        }
        return ClientBuilder.newBuilder().hostnameVerifier(new TrustAllHostNameVerifier()).sslContext(ctx).build();
    }

    TrustManager[] certs = new TrustManager[] { new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    } };

    public static class TrustAllHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            System.out.println(hostname);
            return true;
        }

    }
}
