package com.pixelogicmedia.delivery.data.repositories;

import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryJobRepository extends JpaRepository<DeliveryJob, Long>, JpaSpecificationExecutor<DeliveryJob> {

    @Query(value = """
            SELECT * FROM delivery_job
            WHERE status = 'CREATED'
            ORDER BY id ASC
            LIMIT 1
            FOR UPDATE SKIP LOCKED;
            """,
            nativeQuery = true)
    Optional<DeliveryJob> getNextJob();

    List<DeliveryJob> findByStatus(DeliveryJob.Status status);

    @Override
    @EntityGraph(attributePaths = {"contacts","contacts.contact", "assets", "profile", "profile.connection"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<DeliveryJob> findAll(final Specification<DeliveryJob> spec, final Pageable pageable);
}
