package com.pixelogicmedia.delivery.services;

import com.pixelogicmedia.delivery.data.dto.mh.MessageRequestData;
import com.pixelogicmedia.delivery.data.dto.mh.MessageStatusData;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import kong.unirest.jackson.JacksonObjectMapper;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageHandlerClient {

    private final TokenManager tokenManager;

    private final String messageHandlerSendUri;
    private final String messageHandlerStatusUri;

    private final UnirestInstance unirest = Unirest.spawnInstance();

    @Autowired
    public MessageHandlerClient(final TokenManager tokenManager, @Value("${mh.send-uri}") final String messageHandlerSendUri, @Value("${mh.status-uri}") final String messageHandlerStatusUri) {
        this.tokenManager = tokenManager;
        this.messageHandlerSendUri = messageHandlerSendUri;
        this.messageHandlerStatusUri = messageHandlerStatusUri;
        this.unirest.config()
                .setObjectMapper(new JacksonObjectMapper());
    }

    public String sendEmail(final MessageRequestData messageRequestData) {
        return unirest.post("%s".formatted(this.messageHandlerSendUri))
                .body(messageRequestData)
                .header("Authorization", "Bearer %s".formatted(this.tokenManager.getAccessTokenString()))
                .asString().getBody();
    }

    public String getEmailStatus(String requestId) {
        var response =  unirest.get("%s/%s".formatted(this.messageHandlerStatusUri, requestId))
                .header("Authorization", "Bearer %s".formatted(this.tokenManager.getAccessTokenString()))
                .asObject(MessageStatusData.class).getBody();
        return response.getStatus();
    }


}
