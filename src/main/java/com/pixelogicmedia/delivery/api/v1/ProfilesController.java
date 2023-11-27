package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.BaseController;
import com.pixelogicmedia.delivery.api.mappers.ProfileMapper;
import com.pixelogicmedia.delivery.api.v1.models.ProfileResource;
import com.pixelogicmedia.delivery.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProfilesController extends BaseController implements ProfilesApi {

    private ProfileService profileService;

    private ProfileMapper profileMapper;

    @Autowired
    public ProfilesController(final ProfileService profileService, final ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @Override
    public ResponseEntity<ProfileResource> createProfile(final ProfileResource profileResource) {
        final var profile = this.profileService.createProfile(this.profileMapper.map(profileResource));
        return ResponseEntity.ok(this.profileMapper.map(profile));
    }

    @Override
    public ResponseEntity<List<ProfileResource>> profilesGet(final Integer offset, final Integer limit, final String filter, final String sort) {
        final var page = this.profileService.listProfiles(this.pageOf(offset, limit), this.specificationOf(filter, sort));

        return ResponseEntity.ok()
                .header(TOTAL_COUNT_HEADER, String.valueOf(page.getTotalElements()))
                .body(this.profileMapper.map(page.getContent()));
    }

    @Override
    public ResponseEntity<ProfileResource> updateProfile(final Long id, final ProfileResource profileResource) {
        final var profile = this.profileService.updateProfile(this.profileMapper.map(profileResource.id(id)));
        return ResponseEntity.ok(this.profileMapper.map(profile));
    }
}
