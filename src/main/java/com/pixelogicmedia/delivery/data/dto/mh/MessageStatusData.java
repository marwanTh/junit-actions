package com.pixelogicmedia.delivery.data.dto.mh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageStatusData {
    @JsonProperty("status")
    private String status;

    public String getStatus() {
        return status;
    }
}
