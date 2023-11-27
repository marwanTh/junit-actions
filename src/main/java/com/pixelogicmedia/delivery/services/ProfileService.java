package com.pixelogicmedia.delivery.services;

import com.pixelogicmedia.delivery.data.entities.Profile;
import com.pixelogicmedia.delivery.data.repositories.ProfileRepository;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Value("${default-completion-template-key}")
    private String defaultCompletionTemplateKey;

    @Value("${default-failure-template-key}")
    private String defaultFailureTemplateKey;

    @Autowired
    public ProfileService(final ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public Profile createProfile(final Profile profile) {
        return this.profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Page<Profile> listProfiles(final Pageable pageable, final Specification<Profile> spec) {
        return this.profileRepository.findAll(spec, pageable);
    }

    @Transactional
    public Profile updateProfile(final Profile profile) {
        if (Objects.isNull(profile.getId())) {
            throw BusinessException.badRequest("ID is required when updating objects");
        }
        if (profile.getCompletionTemplateKey() == null) {
            profile.setCompletionTemplateKey(defaultCompletionTemplateKey);
        }
        if (profile.getFailureTemplateKey() == null) {
            profile.setFailureTemplateKey(defaultFailureTemplateKey);
        }

        return this.profileRepository.save(profile);
    }
}
