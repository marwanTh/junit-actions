package com.pixelogicmedia.delivery.services;

import com.pixelogicmedia.delivery.data.entities.Contact;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.data.entities.DeliveryJobContact;
import com.pixelogicmedia.delivery.data.entities.Profile;
import com.pixelogicmedia.delivery.data.repositories.*;
import com.pixelogicmedia.delivery.data.repositories.AssetRepository;
import com.pixelogicmedia.delivery.data.repositories.DeliveryJobRepository;
import com.pixelogicmedia.delivery.data.repositories.ProfileContactRepository;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.execution.PathResolverService;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeliveryJobService {

    private final DeliveryJobRepository deliveryJobRepository;

    private final PathResolverService pathResolverService;

    private final AssetRepository assetRepository;

    private final ProfileContactRepository profileContactRepository;

    private final ProfileRepository profileRepository;
    @Autowired
    public DeliveryJobService(final DeliveryJobRepository deliveryJobRepository, final PathResolverService pathResolverService, final AssetRepository assetRepository, final ProfileContactRepository profileContactRepository, final ProfileRepository profileRepository) {
        this.deliveryJobRepository = deliveryJobRepository;
        this.pathResolverService = pathResolverService;
        this.assetRepository = assetRepository;
        this.profileContactRepository = profileContactRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public DeliveryJob createJob(final DeliveryJob job, final Boolean allowDuplication) {

        job.setStatus(DeliveryJob.Status.CREATED);
        job.setProgress(0.0);
        job.setLastReportedProgress(0.0);
        Set<String> assetTitles = new HashSet<>();

        // TODO: Validation

        // Resolve asset paths
        // If the caller didn't provide asset Version, we should retrieve and lock on the current latest version in AM.
        this.pathResolverService.resolvePaths(job);

        // Deduplication detection
        for (final var asset : job.getAssets()) {
            asset.setDeliveryJob(job);
            if (!allowDuplication && this.assetRepository.isAssetDuplicate(asset.getPhelixAssetId(), asset.getPhelixAssetVersion())) {
                throw BusinessException.badRequest("Asset ID %d, version %d already submitted before."
                        .formatted(asset.getPhelixAssetId(), asset.getPhelixAssetVersion()));
            }
            assetTitles.add(asset.getTitleName());
        }
        if (job.getDeliveryJobContacts() == null) {
            final var profileContacts = profileContactRepository.findByProfileId(job.getProfile().getId());
            final var jobContact = profileContacts.stream().map(profileContact -> {
                var deliveryContact = new DeliveryJobContact();
                final var contact = new Contact();
                contact.setId(profileContact.getContact().getId());
                deliveryContact.setContact(contact);
                deliveryContact.setNotifyOn(profileContact.getNotifyOn());
                deliveryContact.setSendType(profileContact.getSendType());
                return deliveryContact;
            }).collect(Collectors.toSet());
            job.setDeliveryJobContacts(jobContact);
        }
        if (job.getEmailSubject() == null) {
            final Profile profile = profileRepository.findById(job.getProfile().getId()).orElseThrow();
            if (profile.getEmailSubjectTemplate() != null) {
                // TODO: enrich the context
                Map<String,String> context = new HashMap<>();
                if (assetTitles.size() > 2) {
                    context.put("title", "multiple");
                } else {
                    context.put("title", String.join(" - ", assetTitles.stream().toList()));
                }
                context.put("connection", profile.getConnection().getName());
                job.setEmailSubject(StringSubstitutor.replace(profile.getEmailSubjectTemplate(), context, "{{", "}}"));
            }
        }

        return this.deliveryJobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryJob> listJobs(final Pageable pageable, final Specification<DeliveryJob> spec) {
        return this.deliveryJobRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public DeliveryJob getJob(final Long id) {
        return this.deliveryJobRepository.findById(id).orElseThrow();
    }
}
