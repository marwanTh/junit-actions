package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.ProfileResource;
import com.pixelogicmedia.delivery.data.entities.Profile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ConnectionMapper.class)
public interface ProfileMapper {

    Profile map(ProfileResource profileResource);

    ProfileResource map(Profile profile);

    List<ProfileResource> map(List<Profile> profiles);
}
