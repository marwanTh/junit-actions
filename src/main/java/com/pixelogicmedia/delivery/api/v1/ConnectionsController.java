package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.BaseController;
import com.pixelogicmedia.delivery.api.mappers.ConnectionMapper;
import com.pixelogicmedia.delivery.api.v1.models.ConnectionResource;
import com.pixelogicmedia.delivery.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConnectionsController extends BaseController implements ConnectionsApi {

    private ConnectionService connectionService;

    private ConnectionMapper connectionMapper;

    @Autowired
    public ConnectionsController(final ConnectionService connectionService, final ConnectionMapper connectionMapper) {
        this.connectionService = connectionService;
        this.connectionMapper = connectionMapper;
    }

    @Override
    public ResponseEntity<List<ConnectionResource>> connectionsGet(final Integer offset, final Integer limit, final String filter, final String sort) {
        final var page = this.connectionService.listConnections(this.pageOf(offset, limit), this.specificationOf(filter, sort));

        return ResponseEntity.ok()
                .header(TOTAL_COUNT_HEADER, String.valueOf(page.getTotalElements()))
                .body(this.connectionMapper.map(page.getContent()));
    }

    @Override
    public ResponseEntity<ConnectionResource> createConnection(final ConnectionResource connectionResource) {
        final var connection = this.connectionService.createConnection(this.connectionMapper.map(connectionResource));
        return ResponseEntity.ok(this.connectionMapper.map(connection));
    }

    @Override
    public ResponseEntity<ConnectionResource> updateConnection(final Long id, final ConnectionResource connectionResource) {
        final var connection = this.connectionService.updateConnection(this.connectionMapper.map(connectionResource.id(id)));
        return ResponseEntity.ok(this.connectionMapper.map(connection));
    }
}
