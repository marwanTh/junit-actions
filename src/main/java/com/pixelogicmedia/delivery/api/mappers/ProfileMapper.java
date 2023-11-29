package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.CreateProfileResource;
import com.pixelogicmedia.delivery.api.v1.models.ProfileResource;
import com.pixelogicmedia.delivery.data.entities.Profile;
import com.pixelogicmedia.delivery.exceptions.EnumMappingException;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ConnectionMapper.class, unexpectedValueMappingException = EnumMappingException.class)
public interface ProfileMapper {

    Profile map(CreateProfileResource profileResource);

    ProfileResource map(Profile profile);

    List<ProfileResource> map(List<Profile> profiles);
}
