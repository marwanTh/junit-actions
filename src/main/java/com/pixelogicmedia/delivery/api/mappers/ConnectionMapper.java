package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.ConnectionResource;
import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.execution.ConnectionConfig;
import com.pixelogicmedia.delivery.utils.json.JsonMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ConnectionMapper {

    @Mapping(source = "config", target = "config", qualifiedByName = "mapConfig")
    public abstract Connection map(ConnectionResource contactResource);

    public abstract ConnectionResource map(Connection connection);

    public abstract List<ConnectionResource> map(List<Connection> connections);

    @Named("mapConfig")
    protected ConnectionConfig mapConfig(final Object object) {
        return JsonMapper.convertValueIgnoringEncryption(object, ConnectionConfig.class);
    }
}
