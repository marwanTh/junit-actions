package com.pixelogicmedia.delivery.api.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pixelogicmedia.delivery.api.v1.models.ConnectionResource;
import com.pixelogicmedia.delivery.api.v1.models.ErrorResource;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.execution.ConnectionConfig;
import com.pixelogicmedia.delivery.utils.json.JsonMapper;
import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration(proxyBeanMethods = false)
public abstract class AbstractControllerTest {

    @Value(value = "${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenManager tokenManager;

    @Bean
    @ServiceConnection
    @RestartScope
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("delivery_manager")
                .withUsername("delivery_manager")
                .withPassword("sherlock");
    }

    @PostConstruct
    void init() {
        this.restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            protected void handleError(final ClientHttpResponse response, final HttpStatusCode statusCode) throws IOException {
                if (statusCode.is4xxClientError()) {
                    final var error = JsonMapper.readValue(response.getBody(), ErrorResource.class);
                    throw BusinessException.of(HttpStatus.resolve(statusCode.value()), error.getMessage());
                }

                super.handleError(response, statusCode);
            }
        });
    }

    public <T> T post(final String uri, final Object body, final Class<T> responseType) {
        final var entity = this.restTemplate.exchange("http://localhost:%d/api/v1/%s".formatted(this.port, uri),
                HttpMethod.POST, new HttpEntity<>(body, this.headers()), responseType);

        return entity.getBody();
    }

    public <T> T put(final String uri, final Object body, final Class<T> responseType) {
        final var entity = this.restTemplate.exchange("http://localhost:%d/api/v1/%s".formatted(this.port, uri),
                HttpMethod.PUT, new HttpEntity<>(body, this.headers()), responseType);

        return entity.getBody();
    }

    public <T> T get(final String uri, final Class<T> responseType) {
        final var entity = this.restTemplate.exchange("http://localhost:%d/api/v1/%s".formatted(this.port, uri),
                HttpMethod.GET, new HttpEntity<>(null, this.headers()), responseType);

        return entity.getBody();
    }

    public <T> T get(final String uri, final ParameterizedTypeReference<T> responseType) {
        final var entity = this.restTemplate.exchange("http://localhost:%d/api/v1/%s".formatted(this.port, uri),
                HttpMethod.GET, new HttpEntity<>(null, this.headers()), responseType);

        return entity.getBody();
    }

    protected Map<String, Object> createConnection(final String name, final String type, final ConnectionConfig config) {
        final var connection = new ConnectionResource()
                .type(type)
                .name(name);

        final var connectionMap = JsonMapper.convertValue(connection, new TypeReference<Map<String, Object>>() {
        });

        connectionMap.put("config", JsonMapper.convertValueIgnoringEncryption(config, new TypeReference<Map<String, Object>>() {
        }));

        return connectionMap;
    }

    private HttpHeaders headers() {
        final var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer %s".formatted(this.tokenManager.getAccessTokenString()));
        return headers;
    }
}
