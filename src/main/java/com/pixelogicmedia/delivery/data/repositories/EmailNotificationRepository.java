package com.pixelogicmedia.delivery.data.repositories;

import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.data.entities.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long>, JpaSpecificationExecutor<EmailNotification> {

    @Query(value = """
            SELECT * FROM email_notification
            WHERE status = 'PENDING' OR status = 'SENDING'
            ORDER BY id ASC
            LIMIT 1
            FOR UPDATE SKIP LOCKED;
            """,
            nativeQuery = true)
    Optional<EmailNotification> getNextEmail();
}
