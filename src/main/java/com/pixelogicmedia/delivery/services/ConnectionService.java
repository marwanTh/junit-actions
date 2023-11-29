package com.pixelogicmedia.delivery.services;

import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.data.repositories.ConnectionRepository;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ConnectionService {
    private ConnectionRepository connectionRepository;

    @Autowired
    public ConnectionService(final ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    @Transactional
    public Connection createConnection(final Connection connection) {
        return this.connectionRepository.save(connection);
    }

    @Transactional(readOnly = true)
    public Page<Connection> listConnections(final Pageable pageable, final Specification<Connection> spec) {
        return this.connectionRepository.findAll(spec, pageable);
    }

    @Transactional
    public Connection updateConnection(final Long id, final Connection connection) {
        connection.setId(id);
        if (Objects.isNull(connection.getId())) {
            throw BusinessException.badRequest("ID is required when updating objects");
        }

        return this.connectionRepository.save(connection);
    }
}
