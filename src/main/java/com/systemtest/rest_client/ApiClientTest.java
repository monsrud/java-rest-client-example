package com.systemtest.rest_client;

import java.util.List;

// Try using the REST API CLient
public class ApiClientTest {
    public static void main(String[] args) {

        ApiClient apiClient = new ApiClient("10.80.0.75");
        NodeIdentity nodeIdentity = apiClient.getNodeIdentity();
        List<LanStatus> lanStatusList = apiClient.getLanStatus();

    }
}
