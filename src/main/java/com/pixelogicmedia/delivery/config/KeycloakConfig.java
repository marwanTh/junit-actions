package com.pixelogicmedia.delivery.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {


    @Bean
    Keycloak keycloak(
            @Value("${keycloak.auth-server-url}") final String serverUrl,
            @Value("${keycloak.realm}") final String realm,
            @Value("${keycloak.resource}") final String clientId,
            @Value("${keycloak.credentials.secret}") final String clientSecret
    ) {
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    @Bean
    TokenManager tokenManager(final Keycloak keycloak) {
        return keycloak.tokenManager();
    }
}
