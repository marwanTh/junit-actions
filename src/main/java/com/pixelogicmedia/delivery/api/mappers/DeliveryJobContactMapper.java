package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.CreateDeliveryJobContactResource;
import com.pixelogicmedia.delivery.api.v1.models.DeliveryJobContactResource;
import com.pixelogicmedia.delivery.data.entities.DeliveryJobContact;
import com.pixelogicmedia.delivery.exceptions.EnumMappingException;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ConnectionMapper.class, unexpectedValueMappingException = EnumMappingException.class)
public interface DeliveryJobContactMapper {

    DeliveryJobContact map(CreateDeliveryJobContactResource deliveryJobContactResource);

    DeliveryJobContactResource map(DeliveryJobContact deliveryJobContact);

    List<DeliveryJobContact> map(List<DeliveryJobContactResource> deliveryJobContactResources);

}
