package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.ProfileContactResource;
import com.pixelogicmedia.delivery.data.entities.ProfileContact;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ConnectionMapper.class)
public interface ProfileContactMapper {

    ProfileContact map(ProfileContactResource profileContactResource);

    ProfileContactResource map(ProfileContact profileContact);

    List<ProfileContactResource> map(List<ProfileContact> profileContacts);
}
