package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.DeliveryJobResource;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ConnectionMapper.class)
public interface DeliveryJobMapper {

    DeliveryJob map(DeliveryJobResource jobResource);

    DeliveryJobResource map(DeliveryJob job);

    List<DeliveryJobResource> map(List<DeliveryJob> jobs);
}
