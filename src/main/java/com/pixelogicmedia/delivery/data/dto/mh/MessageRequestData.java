package com.pixelogicmedia.delivery.data.dto.mh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageRequestData {
    @JsonProperty("notificationKey")
    private String notificationKey;
    @JsonProperty("payload")
    private Payload payload;

    public MessageRequestData(final String notificationKey, final Payload payload) {
        this.notificationKey = notificationKey;
        this.payload = payload;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        @JsonProperty("recipients")
        private List<Recipient> recipients;
        @JsonProperty("subject")
        private String subject;
        @JsonProperty("emailBodyData")
        private Map<String, Object> emailBodyData;

        public Payload(final List<Recipient> recipients, final String subject, final Map<String, Object> emailBodyData) {
            this.recipients = recipients;
            this.subject = subject;
            this.emailBodyData = emailBodyData;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recipient {
        @JsonProperty("userSystem")
        private String userSystem;
        @JsonProperty("userType")
        private String userType;
        @JsonProperty("userId")
        private String userId;
        @JsonProperty("recipientType")
        private RecipientType recipientType;

        public enum RecipientType {
            To,
            CC,
            BCC,

        }

        public Recipient(final String userSystem, final String userType, final String userId, final RecipientType recipientType) {
            this.userSystem = userSystem;
            this.userType = userType;
            this.userId = userId;
            this.recipientType = recipientType;
        }
    }
}
