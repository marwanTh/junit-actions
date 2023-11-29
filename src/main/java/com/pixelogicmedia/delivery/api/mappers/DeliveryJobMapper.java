package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.CreateDeliveryJobResource;
import com.pixelogicmedia.delivery.api.v1.models.DeliveryJobResource;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.exceptions.EnumMappingException;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ConnectionMapper.class, unexpectedValueMappingException = EnumMappingException.class)
public interface DeliveryJobMapper {

    DeliveryJob map(CreateDeliveryJobResource jobResource);

    DeliveryJobResource map(DeliveryJob job);

    List<DeliveryJobResource> map(List<DeliveryJob> jobs);
}
