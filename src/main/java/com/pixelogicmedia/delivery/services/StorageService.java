package com.pixelogicmedia.delivery.services;

import com.pixelogicmedia.delivery.data.entities.Storage;
import com.pixelogicmedia.delivery.data.repositories.StorageRepository;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class StorageService {

    private final StorageRepository storageRepository;

    @Autowired
    public StorageService(final StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Transactional
    public Storage createStorage(final Storage storage) {
        return this.storageRepository.save(storage);
    }

    @Transactional(readOnly = true)
    public Page<Storage> listStorages(final Pageable pageable, final Specification<Storage> spec) {
        return this.storageRepository.findAll(spec, pageable);
    }

    @Transactional
    public Storage updateStorage(final Storage storage) {
        if (Objects.isNull(storage.getId())) {
            throw BusinessException.badRequest("ID is required when updating objects");
        }

        return this.storageRepository.save(storage);
    }
}
