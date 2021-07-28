package com.systemtest.rest_client;

import com.google.gson.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/*

    Example Usage:

        ApiClient apiClient = new ApiClient("10.80.0.75");
        NodeIdentity nodeIdentity = apiClient.getNodeIdentity()
        List<LanStatus> lanStatusList = apiClient.getLanStatus();

 */

public class ApiClient {

    String hostnameoripaddress;
    String baseurl;
    Client client;


    public ApiClient(String hostnameoripaddress) {
        this.hostnameoripaddress = hostnameoripaddress;
        this.baseurl = "https://" + hostnameoripaddress + "/rest/v001";
        final ClientConfig config = new DefaultClientConfig();
        try {
            config.getProperties()
                    .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                            new HTTPSProperties(
                                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER,
                                    SSLUtil.getInsecureSSLContext()));

            this.client = Client.create(config);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private JsonElement genericRequest(String path) {

        WebResource webResource = client
                .resource(path);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }


        String output = response.getEntity(String.class);
        return new JsonParser().parse(output);

    }

    public List<LanStatus> getLanStatus() {
        List<LanStatus> lanStatusList = new ArrayList<>();
        JsonElement jsonElement = getLanStatusJson();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("Ethernet-interfaces");

        Gson gson = new Gson();

        for (JsonElement j : jsonArray) {
            LanStatus lanStatus = gson.fromJson(j, LanStatus.class);
            lanStatusList.add(lanStatus);
        }

        return lanStatusList;
    }

    public NodeIdentity getNodeIdentity() {
        JsonElement jsonElement = getNodeIdentityJson();
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Gson gson = new Gson();
        return gson.fromJson(jsonObject, NodeIdentity.class);

    }

    public JsonElement getNodeIdentityJson() {
        String path = this.baseurl + "/device/node_identity";
        return genericRequest(path);
    }

    public JsonElement getLanStatusJson() {
        String path = this.baseurl + "/device/lan_status";
        return genericRequest(path);
    }

    private static class SSLUtil {
        protected static SSLContext getInsecureSSLContext()
                throws KeyManagementException, NoSuchAlgorithmException {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                final java.security.cert.X509Certificate[] arg0, final String arg1)
                                 {
                            // do nothing and blindly accept the certificate
                        }

                        public void checkServerTrusted(
                                final java.security.cert.X509Certificate[] arg0, final String arg1)
                                {
                            // do nothing and blindly accept the server
                        }

                    }
            };

            final SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            return sslcontext;
        }
    }

}
