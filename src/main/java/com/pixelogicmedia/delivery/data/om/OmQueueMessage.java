package com.pixelogicmedia.delivery.data.om;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmQueueMessage {
    String requestId;
    int statusCode;
    String message;
    Integer progress;

    public OmQueueMessage(String requestId, int statusCode, String message, Integer progress) {
        this.requestId = requestId;
        this.statusCode = statusCode;
        this.message = message;
        this.progress = progress;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
