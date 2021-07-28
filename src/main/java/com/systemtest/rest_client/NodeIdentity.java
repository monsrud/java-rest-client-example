package com.systemtest.rest_client;

import com.google.gson.annotations.SerializedName;

public class NodeIdentity {

    String etherNetMac;
    String hardwareName;
    @SerializedName("HW rev")
    int hardwareRev;
    @SerializedName("HW type")
    int hardwareType;
    @SerializedName("Number RF Interfaces")
    int numberOfRFInterfaces;
    String partNumber;
    String serialNumber;
    String bootloaderVersion;
    String controller;
    String description;
    String location;
    String name;
    String protected_mode;
    String role;
    String software;
    String ssid;

}
