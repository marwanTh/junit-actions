package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.v1.models.ConnectionResource;
import com.pixelogicmedia.delivery.api.v1.models.ContactResource;
import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.execution.aspera.AsperaConnectionConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ConnectionsControllerTests extends AbstractControllerTest {

    @Test
    void createConnection() {

        final var connectionRequest = this.createConnection("%s-connection".formatted(UUID.randomUUID()),
                Connection.Type.ASPERA.toString(),
                new AsperaConnectionConfig());

        final var connectionResponse = this.post("connections", connectionRequest, ConnectionResource.class);

        Assertions.assertNotNull(connectionResponse);
        Assertions.assertNotNull(connectionResponse.getId());
        Assertions.assertEquals(connectionRequest.get("name"), connectionResponse.getName());
        Assertions.assertEquals(connectionRequest.get("type"), connectionResponse.getType().toString());
    }


    @Test
    void createConnectionReqName() {
        final var connectionRequest = this.createConnection(null,
                Connection.Type.ASPERA.toString(),
                new AsperaConnectionConfig());

        assertThrows(BusinessException.class,
                () -> {
                    this.post("connections", connectionRequest, ConnectionResource.class);
                });
    }

    @Disabled
    @Test
    void createConnectionReqType() {
        final var connectionRequest = this.createConnection("%s-connection".formatted(UUID.randomUUID()),
                null,
                new AsperaConnectionConfig());
        assertThrows(BusinessException.class,
                () -> {
                   ConnectionResource response =  this.post("connections", connectionRequest, ConnectionResource.class);
                });
    }

    @Test
    void createConnectionReqConfig() {
        final var connectionRequest = this.createConnection("%s-connection".formatted(UUID.randomUUID()),
                Connection.Type.ASPERA.toString(),
                null);
        assertThrows(BusinessException.class,
                () -> {
                    this.post("connections", connectionRequest, ConnectionResource.class);
                });
    }
    @Test
    void updateConnections() {
        final var connectionCreateRequest = this.createConnection("%s-connection".formatted(UUID.randomUUID()),
                Connection.Type.ASPERA.toString(),
                new AsperaConnectionConfig());

        final var connectionCreateResponse = this.post("connections", connectionCreateRequest, ConnectionResource.class);


        final var connectionUpdateRequest = this.createConnection("%s-updated-connection".formatted(UUID.randomUUID()),
                Connection.Type.ASPERA.toString(),
                new AsperaConnectionConfig());

        final var connectionUpdateResponse = this.put("connections/%s".formatted(connectionCreateResponse.getId()),
                connectionUpdateRequest, ConnectionResource.class);

        Assertions.assertEquals(connectionUpdateRequest.get("name"), connectionUpdateResponse.getName());
        Assertions.assertEquals(connectionUpdateRequest.get("description"), connectionUpdateResponse.getDescription());
        Assertions.assertEquals(connectionUpdateRequest.get("type"), connectionUpdateResponse.getType().toString());

    }

    @Test
    void getConnections() {
        final var initialConnections = this.get("connections",
                new ParameterizedTypeReference<List<ConnectionResource>>() {});
        final var connectionRequest = this.createConnection("%s-connection".formatted(UUID.randomUUID()),
                Connection.Type.ASPERA.toString(),
                new AsperaConnectionConfig());

        this.post("connections", connectionRequest, ConnectionResource.class);
        final var finalConnections = this.get("connections",
                new ParameterizedTypeReference<List<ConnectionResource>>() {});
        Assertions.assertEquals(initialConnections.size()+1, finalConnections.size());
    }
}
