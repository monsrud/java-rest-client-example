package com.systemtest.rest_client;

import com.google.gson.annotations.SerializedName;

public class LanStatus {
    String name;
    @SerializedName("interface number")
    int interfaceNumber;
    String interfaceType;
    String adminState;
    String runState;
    @SerializedName("speed")
    int speed;
    String duplex;
    String PoE;

}
