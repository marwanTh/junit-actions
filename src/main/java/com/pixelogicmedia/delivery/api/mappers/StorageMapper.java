package com.pixelogicmedia.delivery.api.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pixelogicmedia.delivery.api.v1.models.CreateStorageResource;
import com.pixelogicmedia.delivery.api.v1.models.StorageResource;
import com.pixelogicmedia.delivery.data.entities.Storage;
import com.pixelogicmedia.delivery.exceptions.EnumMappingException;
import com.pixelogicmedia.delivery.utils.json.JsonMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", unexpectedValueMappingException = EnumMappingException.class)
public abstract class StorageMapper {

    @Mapping(source = "attributes", target = "attributes", qualifiedByName = "mapAttributesMap")
    public abstract StorageResource map(Storage storage);

    @Mapping(source = "attributes", target = "attributes", qualifiedByName = "mapAttributesObject")
    public abstract Storage map(CreateStorageResource storageResource);

    public abstract List<StorageResource> map(List<Storage> storages);

    @Named("mapAttributesObject")
    protected Storage.Attributes mapAttributesObject(final Map<String, Object> attributes) {
        return JsonMapper.convertValueIgnoringEncryption(attributes, Storage.Attributes.class);
    }

    @Named("mapAttributesMap")
    protected Map<String, Object> mapAttributesMap(final Storage.Attributes attributes) {
        return JsonMapper.convertValue(attributes, new TypeReference<Map<String, Object>>() {
        });
    }
}
