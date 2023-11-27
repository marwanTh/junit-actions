package com.pixelogicmedia.delivery.data.repositories;

import com.pixelogicmedia.delivery.data.entities.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long>, JpaSpecificationExecutor<Storage> {

    Optional<Storage> findByStorageId(String storageId);
}
