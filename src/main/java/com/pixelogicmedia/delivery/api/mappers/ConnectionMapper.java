package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.ConnectionResource;
import com.pixelogicmedia.delivery.api.v1.models.CreateConnectionResource;
import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.exceptions.EnumMappingException;
import com.pixelogicmedia.delivery.execution.ConnectionConfig;
import com.pixelogicmedia.delivery.utils.json.JsonMapper;
import org.mapstruct.EnumMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", unexpectedValueMappingException = EnumMappingException.class)
public abstract class ConnectionMapper {

    @Mapping(source = "config", target = "config", qualifiedByName = "mapConfig")
    @EnumMapping(unexpectedValueMappingException = EnumMappingException.class)
    public abstract Connection map(CreateConnectionResource contactResource);

    public abstract ConnectionResource map(Connection connection);

    public abstract List<ConnectionResource> map(List<Connection> connections);

    @Named("mapConfig")
    protected ConnectionConfig mapConfig(final Object object) {
        return JsonMapper.convertValueIgnoringEncryption(object, ConnectionConfig.class);
    }
}
